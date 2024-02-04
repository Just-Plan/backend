package com.jyp.justplan.domain.place.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jyp.justplan.domain.mbti.domain.Mbti;
import com.jyp.justplan.domain.mbti.domain.MbtiType;
import com.jyp.justplan.domain.place.domain.GoogleMapType;
import com.jyp.justplan.domain.place.domain.PlaceComment;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

        private List<MbtiType> mbti;
        private List<PlaceComment> comment;

        // 타입 변환을 위한 setter
        public void setTranslatedTypes() {
            this.types = types.stream().findFirst()
                .map(GoogleMapType::translateToKorean).stream().toList();
        }

        public void setMbti(List<MbtiType> mbti) {
            this.mbti = mbti;
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
    }
}