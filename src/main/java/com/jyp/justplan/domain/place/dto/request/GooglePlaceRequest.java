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

    private String name;
    private String address;
    private List<String> types;
    private double lat;
    private double lng;
    private String photoReference;

    public GooglePlace toEntity() {
        return GooglePlace.builder()
                .name(name)
                .address(address)
                .types(types.toString())
                .lat(lat)
                .lng(lng)
                .photoReference(photoReference)
                .build();
    }
}
