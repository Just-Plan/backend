package com.jyp.justplan.domain.place.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlaceRequest {
    private Long googlePlaceId;
    private String name;
    private String formattedAddress;
    private String types;
    private double latitude;
    private double longitude;
    private String photoReference;
}
