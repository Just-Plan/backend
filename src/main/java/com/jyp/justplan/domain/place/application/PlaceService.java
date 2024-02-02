package com.jyp.justplan.domain.place.application;

import com.jyp.justplan.domain.city.domain.City;
import com.jyp.justplan.domain.place.domain.*;
import com.jyp.justplan.domain.place.dto.request.MemoUpdateDto;
import com.jyp.justplan.domain.place.dto.request.PlaceListRequest;
import com.jyp.justplan.domain.place.dto.request.PlaceRequest;
import com.jyp.justplan.domain.place.dto.request.PlacePlanUpdateDto;
import com.jyp.justplan.domain.place.dto.request.PlaceUpdateRequest;
import com.jyp.justplan.domain.place.dto.response.PlaceResponse;
import com.jyp.justplan.domain.place.dto.response.SchedulePlacesResponse;
import com.jyp.justplan.domain.place.exception.NoSuchGooglePlaceException;
import com.jyp.justplan.domain.place.exception.NoSuchPlaceException;
import com.jyp.justplan.domain.plan.application.PlanService;
import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.domain.PlanRepository;
import com.jyp.justplan.domain.plan.exception.NoSuchPlanException;
import com.jyp.justplan.domain.plan.exception.NoSuchUserPlanException;
import com.jyp.justplan.domain.user.domain.User;
import com.jyp.justplan.domain.user.domain.UserRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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
    private final PlanService planService;
    private final UserRepository userRepository;

    // 일정 생성
    @Transactional
    public void createPlace(PlaceListRequest placeListRequest, Long planId, Long userId) {
        Plan findPlan = getPlan(planId);
        extracted(userId, planId);

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

    // 일정에 대한 전체 장소 조회
    public SchedulePlacesResponse findPlacesByPlanId(Long planId) {
        planRepository.findById(planId).orElseThrow(() -> new NoSuchPlanException("존재하지 않는 planId 입니다: " + planId));

        List<Place> places = placeRepository.findByPlanId(planId);

        // day와 orderNum 기준으로 Place 객체 정렬
        List<Place> sortedPlaces = places.stream()
            .sorted(Comparator.comparingInt(Place::getDay).thenComparingInt(Place::getOrderNum)).toList();

        // Place들을 day 기준으로 그룹화하고 PlaceResponse 객체로 변환
        Map<Integer, List<PlaceResponse>> groupedByDay = sortedPlaces.stream().collect(Collectors.groupingBy(Place::getDay,
            LinkedHashMap::new, Collectors.mapping(PlaceResponse::of, Collectors.toList())));
        return SchedulePlacesResponse.of(groupedByDay);
    }

    // 전체 장소 수정
    @Transactional
    public void updatePlaces(PlacePlanUpdateDto placePlanUpdateDto, Long userId, Long planId) {
        extracted(userId, planId);

        // Place 삭제
        List<Long> placeDeleteIds = placePlanUpdateDto.getPlaceDeleteIds();
        List<Place> placesToDelete = new ArrayList<>();

        placeDeleteIds.forEach(placeId -> {
            Optional<Place> optionalPlace = placeRepository.findById(placeId);
            optionalPlace.ifPresent(placesToDelete::add);
        });


        placeRepository.deleteAll(placesToDelete);

        placePlanUpdateDto.getDayUpdates().forEach((day, updates) -> {
            updates.forEach(updateRequest -> {
                // Place 찾기
                Place place = getPlace(updateRequest);
                // orderNum과 day, memo 업데이트
                Memo memo = place.getMemo();
                memo.update(new MemoUpdateDto(updateRequest.getMemo().getContent(), updateRequest.getMemo().getColor()));
                place.update(updateRequest.getOrderNum(), day, memo);
            });
        });
    }

    private Place getPlace(PlaceUpdateRequest updateRequest) {
        return placeRepository.findById(updateRequest.getPlaceId())
            .orElseThrow(() -> new NoSuchPlaceException("Place not found with id: " + updateRequest.getPlaceId()));
    }

    private void extracted(Long userId, Long planId) {
        Plan findPlan = getPlan(planId);
        User findUser = getUser(userId);
        planService.validateUserOfPlan(findPlan, findUser);
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
            .orElseThrow(() -> new NoSuchUserPlanException("존재하지 않는 userId 입니다: " + userId));
    }

    private Plan getPlan(Long planId) {
        return planRepository.findById(planId)
            .orElseThrow(() -> new NoSuchPlanException("존재하지 않는 planId 입니다: " + planId));
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
}