package com.jyp.justplan.domain.place.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlaceUpdatesWrapper {
    private Map<Integer, List<PlaceUpdateRequest>> updates;
}
