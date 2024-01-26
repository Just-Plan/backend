package com.jyp.justplan.domain.place.application;

import com.jyp.justplan.domain.place.domain.*;
import com.jyp.justplan.domain.place.dto.request.PlaceRequest;
import com.jyp.justplan.domain.place.dto.request.PlaceUpdatesWrapper;
import com.jyp.justplan.domain.place.dto.response.PlaceResponse;
import com.jyp.justplan.domain.place.exception.NoSuchGooglePlaceException;
import com.jyp.justplan.domain.place.exception.NoSuchPlaceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceRepository placeRepository;
    private final MemoRepository memoRepository;
    private final GooglePlaceRepository googlePlaceRepository;

    /*CREATE*/
    @Transactional
    public PlaceResponse createPlace(PlaceRequest placeRequest) {

        GooglePlace googlePlace = findGooglePlace(placeRequest.getGooglePlaceId());

        Memo memo = Memo.builder()
                .content("")
                .color("WHITE")
                .build();

        Memo savedMemo = memoRepository.save(memo);

        Place place = placeRequest.toEntity().toBuilder()
                .googlePlace(googlePlace)
                .memo(savedMemo)
                .build();

        Place savedPlace = placeRepository.save(place);

        return PlaceResponse.of(savedPlace);
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

    private GooglePlace findGooglePlace(Long googlePlaceId) {
        return googlePlaceRepository.findById(googlePlaceId)
                .orElseThrow(NoSuchGooglePlaceException::new);
    }

}
