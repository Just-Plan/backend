package com.jyp.justplan.domain.place.dto.response;

import com.jyp.justplan.domain.mbti.domain.MbtiType;
import com.jyp.justplan.domain.place.domain.GoogleMapType;
import com.jyp.justplan.domain.place.domain.GooglePlace;
import com.jyp.justplan.domain.place.dto.response.GooglePlacesSearchApiResponse.GooglePlaceApiResultResponse;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Slf4j
public class GooglePlaceResponse {

    private Long googlePlaceId;
    private String name;
    private String formattedAddress;
    private String types;
    private double latitude;
    private double longitude;
    private String photoReference;
    private List<MbtiType> mbti = new ArrayList<>();

    public static GooglePlaceResponse of(GooglePlace googlePlace, List<MbtiType> list) {
        String translatedType = GoogleMapType.translateToKorean(googlePlace.getTypes());

        return new GooglePlaceResponse(
            googlePlace.getId(),
            googlePlace.getName(),
            googlePlace.getAddress(),
            translatedType,
            googlePlace.getLatitude(),
            googlePlace.getLongitude(),
            googlePlace.getPhotoReference(),
            list
        );
    }

    public static GooglePlaceResponse of(GooglePlaceApiResultResponse result, String photoUrl) {
        String firstType = result.getTypes() != null && !result.getTypes().isEmpty()
            ? result.getTypes().get(0) : null;

        String translatedType = GoogleMapType.translateToKorean(firstType);

        return new GooglePlaceResponse(
            result.getId(),
            result.getName(),
            result.getFormattedAddress(),
            translatedType,
            result.getGeometry().getLocation().getLat(),
            result.getGeometry().getLocation().getLng(),
            photoUrl,
            new ArrayList<>()
        );
    }
}
