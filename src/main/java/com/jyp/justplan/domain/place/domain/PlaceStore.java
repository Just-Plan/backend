package com.jyp.justplan.domain.place.domain;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlaceStore {
    private final PlaceRepository placeRepository;

    public Place getRandomPlaceById(long planId) {
        Place place = placeRepository.findRandomByPlanId(planId)
                .orElse(null);
        return place;
    }
}
