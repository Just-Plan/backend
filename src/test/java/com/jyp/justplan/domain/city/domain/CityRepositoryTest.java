package com.jyp.justplan.domain.city.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.jyp.justplan.domain.city.cityTestConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

//@DataJpaTest
class CityRepositoryTest extends cityTestConfig {

    @Autowired
    private CityRepository cityRepository;

    @DisplayName("랜덤한 도시를 가져온다.")
    @Test
    void 랜덤한_도시를_가져온다() {
        // given
        int count = 10;

        // when
        var randomCities = cityRepository.getRandomCities(count);

        // then
        Assertions.assertThat(randomCities.size()).isEqualTo(count);
    }
}