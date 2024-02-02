package com.jyp.justplan.domain.place.dto.response;

import com.jyp.justplan.domain.place.domain.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PlaceResponse {
    private Long placeId;
    private String name;
    private String formattedAddress;
    private String types;
    private double latitude;
    private double longitude;
    private String photoReference;
    private MemoResponse memo;
    private int orderNum;

    public static PlaceResponse of(Place place) {
        return new PlaceResponse(
                place.getId(),
                place.getGooglePlace().getName(),
                place.getGooglePlace().getAddress(),
                place.getGooglePlace().getTypes(),
                place.getGooglePlace().getLatitude(),
                place.getGooglePlace().getLongitude(),
                place.getGooglePlace().getPhotoReference(),
                MemoResponse.of(place.getMemo()),
                place.getOrderNum()
        );
    }
}