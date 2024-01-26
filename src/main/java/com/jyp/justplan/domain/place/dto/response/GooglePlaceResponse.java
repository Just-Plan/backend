package com.jyp.justplan.domain.place.dto.response;

import com.jyp.justplan.domain.place.domain.GooglePlace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GooglePlaceResponse {

    private Long id;
    private String name;
    private String formattedAddress;
    private List<String> types;
    private double lat;
    private double lng;
    private String photoReference;

    public static GooglePlaceResponse of(GooglePlace googlePlace) {
        return new GooglePlaceResponse(
                googlePlace.getId(),
                googlePlace.getName(),
                googlePlace.getAddress(),
                Collections.singletonList(googlePlace.getTypes()),
                googlePlace.getLat(),
                googlePlace.getLng(),
                googlePlace.getPhotoReference()
        );
    }
}
