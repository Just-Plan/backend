package com.jyp.justplan.domain.place.dto.request;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlacePlanUpdateDto {
    private Map<Integer, List<PlaceUpdateRequest>> dayUpdates;
    private List<Long> placeDeleteIds;
}