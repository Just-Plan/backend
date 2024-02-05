package com.jyp.justplan.domain.place.dto.request;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class PlaceListRequest {
    List<PlaceRequest> places = new ArrayList<>();

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @ToString
    public static class PlaceRequest {
        private Long googlePlaceId;
        private String name;
        private String formattedAddress;
        private String types;
        private double latitude;
        private double longitude;
        private String photoReference;
    }
}
