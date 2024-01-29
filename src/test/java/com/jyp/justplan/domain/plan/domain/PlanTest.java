package com.jyp.justplan.domain.plan.domain;

import com.jyp.justplan.domain.city.domain.City;
import com.jyp.justplan.domain.city.domain.CityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.ZonedDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PlanTest {
    @Autowired
    private CityRepository cityRepository;

    private final String 일정_이름 = "일정 이름";
    private final ZonedDateTime 일정_시작_날짜 = ZonedDateTime.of(2024, 1, 19, 0, 0, 0 ,0, ZonedDateTime.now().getZone());
    private final ZonedDateTime 일정_종료_날짜 = ZonedDateTime.of(2024, 1, 20, 0, 0, 0 ,0, ZonedDateTime.now().getZone());
    private final Long 일정_지역_아이디 = 1L;

    @Test
    @DisplayName("일정을 생성한다.")
    void 일정을_생성한다 () {
        /* given */
        Plan plan = new Plan(일정_이름, 일정_시작_날짜, 일정_종료_날짜);
        City city = cityRepository.getById(일정_지역_아이디);
        plan.setRegion(city);

        /* when */
        /* then */
        assertAll(
                () -> assertEquals(일정_이름, plan.getTitle()),
                () -> assertEquals(일정_시작_날짜, plan.getStartDate()),
                () -> assertEquals(일정_종료_날짜, plan.getEndDate()),
                () -> assertEquals(city, plan.getRegion())
        );
    }

    @Test
    @DisplayName("일정을 수정한다.")
    void 일정을_수정한다 () {
        /* given */
        Plan plan = new Plan(일정_이름, 일정_시작_날짜, 일정_종료_날짜);
        City city = cityRepository.getById(일정_지역_아이디);
        plan.setRegion(city);

        /* when */
        String 수정_일정_이름 = "수정 일정 이름";
        ZonedDateTime 수정_일정_시작_날짜 = ZonedDateTime.of(2024, 2, 19, 0, 0, 0 ,0, ZonedDateTime.now().getZone());
        ZonedDateTime 수정_일정_종료_날짜 = ZonedDateTime.of(2024, 2, 20, 0, 0, 0 ,0, ZonedDateTime.now().getZone());
        plan.updatePlan(수정_일정_이름, 수정_일정_시작_날짜, 수정_일정_종료_날짜);

        /* then */
        assertAll(
                () -> assertEquals(수정_일정_이름, plan.getTitle()),
                () -> assertEquals(수정_일정_시작_날짜, plan.getStartDate()),
                () -> assertEquals(수정_일정_종료_날짜, plan.getEndDate())
        );
    }

    @Test
    @DisplayName("일정 공개 여부를 수정한다.")
    void 일정_공개_여부를_수정한다 () {
        /* given */
        // 일정 생성 시, 기본 공개 여부는 true
        Plan plan = new Plan(일정_이름, 일정_시작_날짜, 일정_종료_날짜);
        City city = cityRepository.getById(일정_지역_아이디);
        plan.setRegion(city);

        /* when */
        plan.updatePublic(!plan.isPublished());

        /* then */
        assertThat(plan.isPublished()).isFalse();
    }

    @Test
    @DisplayName("일정의 원본 일정을 설정한다.")
    void 일정의_원본_일정을_설정한다 () {
        /* given */
        Plan plan = new Plan(일정_이름, 일정_시작_날짜, 일정_종료_날짜);
        City city = cityRepository.getById(일정_지역_아이디);
        plan.setRegion(city);

        Plan originPlan = new Plan("원본 일정", 일정_시작_날짜, 일정_종료_날짜);
        originPlan.setRegion(city);

        /* when */
        plan.setOriginPlan(originPlan);

        /* then */
        assertThat(plan.getOriginPlan()).isEqualTo(originPlan);
    }
}