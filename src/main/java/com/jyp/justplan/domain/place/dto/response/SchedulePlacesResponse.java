package com.jyp.justplan.domain.place.dto.response;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SchedulePlacesResponse {
    private Map<Integer, List<PlaceResponse>> daysPlaces;

    public static SchedulePlacesResponse of(Map<Integer, List<PlaceResponse>> daysPlaces) {
        return new SchedulePlacesResponse(daysPlaces);
    }
}