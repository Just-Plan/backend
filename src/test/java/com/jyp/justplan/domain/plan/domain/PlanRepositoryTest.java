package com.jyp.justplan.domain.plan.domain;

import com.jyp.justplan.domain.city.domain.City;
import com.jyp.justplan.domain.city.domain.CityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PlanRepositoryTest {
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private CityRepository cityRepository;

    private final String 일정_이름 = "일정 이름";
    private final ZonedDateTime 일정_시작_날짜 = ZonedDateTime.of(2024, 1, 19, 0, 0, 0 ,0, ZonedDateTime.now().getZone());
    private final ZonedDateTime 일정_종료_날짜 = ZonedDateTime.of(2024, 1, 20, 0, 0, 0 ,0, ZonedDateTime.now().getZone());
    private final Long 일정_지역_아이디 = 1L;

    @Test
    @DisplayName("일정을 저장한다.")
    void 일정을_저장한다 () {
        /* given */
        Plan plan = new Plan(일정_이름, 일정_시작_날짜, 일정_종료_날짜);
        City city = cityRepository.getById(일정_지역_아이디);
        plan.setRegion(city);

        /* when */
        Plan savedPlan = planRepository.save(plan);

        /* then */
        assertAll(
                () -> assertEquals(일정_이름, savedPlan.getTitle()),
                () -> assertEquals(일정_시작_날짜, savedPlan.getStartDate()),
                () -> assertEquals(일정_종료_날짜, savedPlan.getEndDate()),
                () -> assertEquals(city, savedPlan.getRegion()));
    }
    @Test
    @DisplayName("일정을 조회한다.")
    void 일정을_조회한다 () {
        /* given */
        Plan plan = new Plan(일정_이름, 일정_시작_날짜, 일정_종료_날짜);
        City city = cityRepository.getById(일정_지역_아이디);

        plan.setRegion(city);

        /* when */
        Plan savedPlan = planRepository.save(plan);
        Plan foundPlan = planRepository.getById(savedPlan.getId());

        /* then */
        assertAll(
                () -> assertEquals(일정_이름, foundPlan.getTitle()),
                () -> assertEquals(일정_시작_날짜, foundPlan.getStartDate()),
                () -> assertEquals(일정_종료_날짜, foundPlan.getEndDate()),
                () -> assertEquals(city, foundPlan.getRegion()));
    }
}