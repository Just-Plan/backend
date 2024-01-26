package com.jyp.justplan.domain.place.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GooglePhotoReferenceResponse {

    @JsonProperty("photo_reference")
    private String photoReference;

}
