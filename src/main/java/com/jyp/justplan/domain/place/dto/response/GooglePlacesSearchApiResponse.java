package com.jyp.justplan.domain.place.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GooglePlacesSearchApiResponse {

    private List<GooglePlaceApiResultResponse> results;
    private String status;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class GooglePlaceApiResultResponse {

        private Long Id;
        private String name;
        @JsonProperty("formatted_address")
        private String formattedAddress;
        private List<String> types;
        private GooglePlaceGeometry geometry;
        private List<GooglePhotosResponse> photos;

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        public static class GooglePlaceGeometry {
            private GooglePlaceLocation location;
        }

        @AllArgsConstructor
        @NoArgsConstructor
        @Getter
        public static class GooglePhotosResponse {

            @JsonProperty("photo_reference")
            private String photoReference;

        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class GooglePlaceLocation {
        private double lat;
        private double lng;
    }
}
