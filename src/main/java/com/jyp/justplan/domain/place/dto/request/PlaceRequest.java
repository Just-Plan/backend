package com.jyp.justplan.domain.place.dto.request;

import com.jyp.justplan.domain.place.domain.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlaceRequest {

    private Long googlePlaceId;
    private int day;
    private int orderNum;

    public Place toEntity() {
        return Place.builder()
                .day(day)
                .orderNum(orderNum)
                .build();
    }

}
