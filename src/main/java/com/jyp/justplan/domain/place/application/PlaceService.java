package com.jyp.justplan.domain.place.application;

import com.jyp.justplan.domain.city.domain.City;
import com.jyp.justplan.domain.place.domain.*;
import com.jyp.justplan.domain.place.dto.request.PlaceListRequest;
import com.jyp.justplan.domain.place.dto.request.PlaceRequest;
import com.jyp.justplan.domain.place.dto.request.PlaceUpdatesWrapper;
import com.jyp.justplan.domain.place.dto.response.PlaceResponse;
import com.jyp.justplan.domain.place.exception.NoSuchGooglePlaceException;
import com.jyp.justplan.domain.place.exception.NoSuchPlaceException;
import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.domain.PlanRepository;
import com.jyp.justplan.domain.plan.exception.NoSuchPlanException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final MemoRepository memoRepository;
    private final GooglePlaceRepository googlePlaceRepository;
    private final PlanRepository planRepository;

    /*CREATE*/
    @Transactional
    public void createPlace(PlaceListRequest placeListRequest, Long planId) {
        Plan findPlan = planRepository.findById(planId)
            .orElseThrow(() -> new NoSuchPlanException("존재하지 않는 planId 입니다: " + planId));
        for (PlaceRequest placeRequest : placeListRequest.getPlaces()) {

            // Memo 생성 로직을 각 Place에 대해 반복
            Memo savedMemo = memoRepository.save(new Memo());

            // GooglePlace 찾기 또는 새로 생성
            GooglePlace googlePlace = Optional.ofNullable(placeRequest.getGooglePlaceId())
                .flatMap(googlePlaceRepository::findById)
                .orElseGet(() -> findOrCreateGooglePlace(placeRequest, findPlan.getRegion()));

            // 각 Place에 대한 인스턴스 생성 및 저장
            Place newPlace = new Place(0, 0, findPlan, savedMemo, googlePlace);
            placeRepository.save(newPlace);
        }
    }

    /*READ*/
    public PlaceResponse findPlaceById(Long placeId) {
        Place existingPlace = findPlace(placeId);
        return PlaceResponse.of(existingPlace);
    }

    /*UPDATE*/
    @Transactional
    public void updatePlaces(PlaceUpdatesWrapper updatesWrapper) {
        updatesWrapper.getUpdates().forEach((day, updates) -> {
            updates.forEach(update -> {
                Place existingPlace = findPlace(update.getId());

                Place updatedPlace = existingPlace.toBuilder()
                        .day(day)
                        .orderNum(update.getOrderNum())
                        .build();

                placeRepository.save(updatedPlace);
            });
        });
    }
    private Place findPlace(Long placeId) {
        return placeRepository.findById(placeId)
                .orElseThrow(NoSuchPlaceException::new);
    }

    private GooglePlace createGooglePlace(PlaceRequest request, City city) {
        // GooglePlace 객체 생성 및 초기화 로직
        GooglePlace newGooglePlace = GooglePlace.builder()
            .name(request.getName())
            .address(request.getFormattedAddress())
            .types(GoogleMapType.translateFromKorean(request.getTypes()))
            .latitude(request.getLatitude())
            .longitude(request.getLongitude())
            .photoReference(request.getPhotoReference())
            .city(city)
            .build();

        return googlePlaceRepository.save(newGooglePlace);
    }


    private GooglePlace findOrCreateGooglePlace(PlaceRequest request, City city) {
        // 위도와 경도를 기준으로 기존의 GooglePlace가 있는지 검사합니다.
        return googlePlaceRepository.findByLatitudeAndLongitude(request.getLatitude(), request.getLongitude())
            .orElseGet(() -> {
                GooglePlace newGooglePlace = createGooglePlace(request, city);
                return googlePlaceRepository.save(newGooglePlace);
            });
    }

    private GooglePlace findGooglePlace(Long googlePlaceId) {
        return googlePlaceRepository.findById(googlePlaceId)
                .orElseThrow(NoSuchGooglePlaceException::new);
    }
}