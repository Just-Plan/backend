package com.jyp.justplan.domain.place.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GooglePlaceApiResultResponse{

    private Long Id;
    private String name;
    @JsonProperty("formatted_address")
    private String formattedAddress;
    private List<String> types;
    private GooglePlaceGeometry geometry;
    private List<GooglePhotosResponse> photos;

}
