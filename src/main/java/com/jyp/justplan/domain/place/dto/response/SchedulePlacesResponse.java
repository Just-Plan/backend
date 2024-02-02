package com.jyp.justplan.domain.place.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SchedulePlacesResponse {
    private List<DayPlacesResponse> daysPlaces;

    public static SchedulePlacesResponse of(List<DayPlacesResponse> daysPlaces) {
        return new SchedulePlacesResponse(daysPlaces);
    }
}