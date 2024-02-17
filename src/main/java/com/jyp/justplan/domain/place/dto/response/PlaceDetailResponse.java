package com.jyp.justplan.domain.place.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jyp.justplan.domain.mbti.domain.MbtiType;
import com.jyp.justplan.domain.place.domain.GoogleMapType;
import com.jyp.justplan.domain.place.domain.PlaceComment;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class PlaceDetailResponse {

    private Result result;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Result {

        private String name;
        private Double rating;

        @JsonProperty("user_ratings_total")
        private Integer userRatingsTotal;

        @JsonProperty("formatted_phone_number")
        private String formattedPhoneNumber;

        private List<String> types;

        @JsonProperty("opening_hours")
        private OpeningHours openingHours;

        @Setter
        private List<Photo> photos;

        @Setter
        private List<MbtiType> mbti;

        public void setTranslatedTypes() {
            this.types = types.stream().findFirst()
                .map(GoogleMapType::translateToKorean).stream().toList();
        }

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class OpeningHours {

            @JsonProperty("open_now")
            private Boolean openNow;
            private List<Period> periods;
            @JsonProperty("weekday_text")
            private List<String> weekdayText;
        }

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Period {
            private DayTime open;
            private DayTime close;
        }

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class DayTime {
            private Integer day;
            private String time;
        }

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Photo {
            private int height;
            private int width;
            @JsonProperty("photo_reference")
            private String photoReference;
            @JsonProperty("html_attributions")
            private List<String> htmlAttributions;
        }
    }
}