package com.jyp.justplan.domain.place.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GooglePlacesSearchResponse {
    private List<GooglePlaceSearchResult> results;
    private String status;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GooglePlaceSearchResult {
        private String place_id;

        public String getPlaceId() {
            return place_id;
        }
    }
}
