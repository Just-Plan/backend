package com.jyp.justplan.domain.place.dto.response;

import com.jyp.justplan.domain.mbti.domain.MbtiType;
import com.jyp.justplan.domain.place.domain.Place;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SchedulePlacesResponse {
    private Map<Integer, List<PlaceResponse>> daysPlaces;

    public static SchedulePlacesResponse of(Map<Integer, List<PlaceResponse>> daysPlaces) {
        return new SchedulePlacesResponse(daysPlaces);
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlaceResponse {
        private Long placeId;
        private String name;
        private String formattedAddress;
        private String types;
        private double latitude;
        private double longitude;
        private String photoReference;
        private MemoResponse memo;
        private int orderNum;
        private List<MbtiType> mbti = new ArrayList<>();

        public static PlaceResponse of(Place place, List<MbtiType> mbti) {
            return new PlaceResponse(
                place.getId(),
                place.getGooglePlace().getName(),
                place.getGooglePlace().getAddress(),
                place.getGooglePlace().getTypes(),
                place.getGooglePlace().getLatitude(),
                place.getGooglePlace().getLongitude(),
                place.getGooglePlace().getPhotoReference(),
                MemoResponse.of(place.getMemo()),
                place.getOrderNum(),
                mbti
            );
        }
    }
}