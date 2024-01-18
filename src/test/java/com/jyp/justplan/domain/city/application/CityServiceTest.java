package com.jyp.justplan.domain.city.application;

import static org.assertj.core.api.Assertions.*;

import com.jyp.justplan.domain.city.cityTestConfig;
import com.jyp.justplan.domain.city.dto.response.CitiesResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

//@Transactional
class CityServiceTest extends cityTestConfig {

    @Autowired
    private CityService cityService;

    @DisplayName("랜덤한 도시를 가져온다.")
    @Test
    void 랜덤한_도시를_가져온다() {
        // given
        int count = 5;
        // when
        CitiesResponse randomCities = cityService.getRandomCities(5);
        // then
        assertThat(randomCities.getCities().size()).isEqualTo(count);
    }
}