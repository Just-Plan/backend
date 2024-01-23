package com.jyp.justplan.domain.place.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GooglePlaceLocation {

    private double lat;
    private double lng;

}
