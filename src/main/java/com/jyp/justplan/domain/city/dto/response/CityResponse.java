package com.jyp.justplan.domain.city.dto.response;


import com.jyp.justplan.domain.city.domain.City;
import lombok.Getter;

@Getter
public class CityResponse {
    private Long id;
    private String koreanName;
    private String englishName;
    private String introduction;
    private String countryKoreanName;
    private String countryEnglishName;

    public CityResponse() {
    }

    public CityResponse(final City city) {
        this.id = city.getId();
        this.koreanName = city.getKoreanName();
        this.englishName = city.getEnglishName();
        this.introduction = city.getIntroduction();
        this.countryKoreanName = city.getCountry().getKoreanName();
        this.countryEnglishName = city.getCountry().getEnglishName();
    }
}
