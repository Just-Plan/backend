package com.jyp.justplan.domain.place.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class PlaceListRequest {
    List<PlaceRequest> places;

}
