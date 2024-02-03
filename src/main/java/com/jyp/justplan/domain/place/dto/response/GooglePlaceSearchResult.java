package com.jyp.justplan.domain.place.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GooglePlaceSearchResult {
    private String place_id;

    public String getPlaceId() {
        return place_id;
    }
}
