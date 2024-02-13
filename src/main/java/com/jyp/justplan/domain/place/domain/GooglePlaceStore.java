package com.jyp.justplan.domain.place.domain;

import com.jyp.justplan.domain.place.exception.NoSuchGooglePlaceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GooglePlaceStore {
    private final GooglePlaceRepository googlePlaceRepository;

    public GooglePlace getGooglePlaceById(Long googlePlaceId) {
        return googlePlaceRepository.findById(googlePlaceId)
            .orElseThrow(() -> new NoSuchGooglePlaceException("구글 장소를 찾을 수 없습니다: " + googlePlaceId));
    }
}
