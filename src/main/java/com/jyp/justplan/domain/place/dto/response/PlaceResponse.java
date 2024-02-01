package com.jyp.justplan.domain.place.dto.response;

import com.jyp.justplan.domain.place.domain.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlaceResponse {

    private int day;
    private int orderNum;
    private Long memoId;
    private String content;
    private String color;
    private String name;
    private String formattedAddress;
    private List<String> types;
    private double latitude;
    private double longitude;
    private String photoReference;

    public static PlaceResponse of(Place place) {
        return new PlaceResponse(
                place.getDay(),
                place.getOrderNum(),
                place.getMemo().getId(),
                place.getMemo().getContent(),
                place.getMemo().getColor(),
                place.getGooglePlace().getName(),
                place.getGooglePlace().getAddress(),
                Collections.singletonList(place.getGooglePlace().getTypes()),
                place.getGooglePlace().getLatitude(),
                place.getGooglePlace().getLongitude(),
                place.getGooglePlace().getPhotoReference()
        );
    }

}
