package com.jyp.justplan.domain.plan.domain.tag;

import com.jyp.justplan.domain.plan.domain.Plan;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PlanTagRepositoryTest {
    @Autowired
    private PlanTagRepository planTagRepository;

    private final String 일정_이름 = "일정 이름";
    private final ZonedDateTime 일정_시작_날짜 = ZonedDateTime.of(2024, 1, 19, 0, 0, 0 ,0, ZonedDateTime.now().getZone());
    private final ZonedDateTime 일정_종료_날짜 = ZonedDateTime.of(2024, 1, 20, 0, 0, 0 ,0, ZonedDateTime.now().getZone());
    private final String 일정_지역 = "서울";
    private final String 태그_이름 = "태그 이름";

    @Test
    @DisplayName("일정 태그를 저장한다.")
    void 일정_태그를_저장한다 () {
        /* given */
        Plan plan = new Plan(일정_이름, 일정_시작_날짜, 일정_종료_날짜, 일정_지역);
        Tag tag = new Tag(태그_이름);

        PlanTag planTag = new PlanTag(plan, tag);

        /* when */
        PlanTag savedPlanTag = planTagRepository.save(planTag);

        /* then */
        assertAll(
                () -> assertEquals(일정_이름, savedPlanTag.getPlan().getTitle()),
                () -> assertEquals(태그_이름, savedPlanTag.getTag().getName())
        );
    }

    @Test
    @DisplayName("일정 태그를 조회한다.")
    void 일정_태그를_조회한다 () {
        /* given */
        Plan plan = new Plan(일정_이름, 일정_시작_날짜, 일정_종료_날짜, 일정_지역);
        Tag tag = new Tag(태그_이름);

        PlanTag planTag = new PlanTag(plan, tag);

        /* when */
        PlanTag savedPlanTag = planTagRepository.save(planTag);
        PlanTag foundPlanTag = planTagRepository.getById(savedPlanTag.getId());

        /* then */
        assertAll(
                () -> assertEquals(일정_이름, foundPlanTag.getPlan().getTitle()),
                () -> assertEquals(태그_이름, foundPlanTag.getTag().getName())
        );
    }

    @Test
    @DisplayName("일정을 통해 일정-태그를 조회한다.")
    @Transactional
    void 일정을_통해_일정_태그를_조회한다 () {
        /* given */
        Plan plan = new Plan(일정_이름, 일정_시작_날짜, 일정_종료_날짜, 일정_지역);
        Tag tag = new Tag(태그_이름);

        PlanTag planTag = new PlanTag(plan, tag);

        /* when */
        PlanTag savedPlanTag = planTagRepository.save(planTag);
        List<PlanTag> foundPlanTag = planTagRepository.findByPlan(plan);

        /* then */
        assertAll(
                () -> assertEquals(일정_이름, foundPlanTag.iterator().next().getPlan().getTitle()),
                () -> assertEquals(태그_이름, foundPlanTag.iterator().next().getTag().getName())
        );
    }

    @Test
    @DisplayName("일정과 태그를 통해 일정-태그를 조회한다.")
    @Transactional
    void 일정과_태그를_통해_일정_태그를_조회한다 () {
        /* given */
        Plan plan = new Plan(일정_이름, 일정_시작_날짜, 일정_종료_날짜, 일정_지역);
        Tag tag = new Tag(태그_이름);

        PlanTag planTag = new PlanTag(plan, tag);

        /* when */
        PlanTag savedPlanTag = planTagRepository.save(planTag);
        PlanTag foundPlanTag = planTagRepository.findByPlanAndTag(plan, tag).get();

        /* then */
        assertAll(
                () -> assertEquals(일정_이름, foundPlanTag.getPlan().getTitle()),
                () -> assertEquals(태그_이름, foundPlanTag.getTag().getName())
        );
    }

    @Test
    @DisplayName("일정을 통해 일정-태그를 삭제한다.")
    @Transactional
    void 일정을_통해_일정_태그를_삭제한다 () {
        /* given */
        Plan plan = new Plan(일정_이름, 일정_시작_날짜, 일정_종료_날짜, 일정_지역);
        Tag tag = new Tag(태그_이름);

        PlanTag planTag = new PlanTag(plan, tag);

        /* when */
        PlanTag savedPlanTag = planTagRepository.save(planTag);
        planTagRepository.deleteByPlan(plan);

        /* then */
        assertAll(
                () -> assertEquals(0, planTagRepository.findByPlan(plan).size())
        );
    }

    @Test
    @DisplayName("태그를 통해 일정-태그가 존재하는지 확인한다.")
    @Transactional
    void 태그를_통해_일정_태그가_존재하는지_확인한다 () {
        /* given */
        Plan plan = new Plan(일정_이름, 일정_시작_날짜, 일정_종료_날짜, 일정_지역);
        Tag tag = new Tag(태그_이름);

        PlanTag planTag = new PlanTag(plan, tag);

        /* when */
        PlanTag savedPlanTag = planTagRepository.save(planTag);
        boolean isExist = planTagRepository.existsByTag(tag);

        /* then */
        assertAll(
                () -> assertTrue(isExist)
        );
    }
}