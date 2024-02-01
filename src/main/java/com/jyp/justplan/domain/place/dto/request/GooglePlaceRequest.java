package com.jyp.justplan.domain.place.dto.request;

import com.jyp.justplan.domain.place.domain.GooglePlace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class GooglePlaceRequest {

    private Long Id;
    private String name;
    private String formattedAddress;
    private String types;
    private double latitude;
    private double longitude;
    private String photoReference;

    public GooglePlace toEntity() {
        return GooglePlace.builder()
                .name(name)
                .address(formattedAddress)
                .types(types)
                .latitude(latitude)
                .longitude(longitude)
                .photoReference(photoReference)
                .build();
    }
}
