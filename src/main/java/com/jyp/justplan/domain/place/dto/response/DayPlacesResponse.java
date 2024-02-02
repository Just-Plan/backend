package com.jyp.justplan.domain.place.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DayPlacesResponse {
    private int day;
    private List<PlaceResponse> places;

    public static DayPlacesResponse of(int day, List<PlaceResponse> places) {
        return new DayPlacesResponse(day, places);
    }
}
