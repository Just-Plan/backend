package com.jyp.justplan.domain.place.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GooglePlacesSearchApiResponse {

    private List<GooglePlaceApiResultResponse> results;
    private String status;

}
