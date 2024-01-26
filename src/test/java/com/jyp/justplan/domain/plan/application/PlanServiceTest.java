package com.jyp.justplan.domain.plan.application;

import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.domain.PlanRepository;
import com.jyp.justplan.domain.plan.dto.request.*;
import com.jyp.justplan.domain.plan.dto.response.PlanDetailResponse;
import com.jyp.justplan.domain.plan.dto.response.PlanResponse;
import com.jyp.justplan.domain.plan.exception.NoSuchPlanException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PlanServiceTest {
    @Autowired
    private PlanService planService;

    @Autowired
    private PlanRepository planRepository;

    private final String 일정_이름 = "일정 이름";
    private final List<String> 일정_태그 = List.of("태그1", "태그2");
    private final ZonedDateTime 일정_시작_날짜 = ZonedDateTime.of(2024, 1, 19, 0, 0, 0 ,0, ZonedDateTime.now().getZone());
    private final ZonedDateTime 일정_종료_날짜 = ZonedDateTime.of(2024, 1, 20, 0, 0, 0 ,0, ZonedDateTime.now().getZone());
    private final String 일정_지역 = "서울";
    private final BudgetUpdateRequest 일정_예산_수정_요청 = new BudgetUpdateRequest(10000, 20000);
    private final ExpenseUpdateRequest 일정_지출_수정_요청 = new ExpenseUpdateRequest(10000, 20000, 30000, 40000, 50000);

    /* 플랜 생성 */
    @Test
    @DisplayName("일정을 생성한다.")
    void 일정을_생성한다 () {
        /* given */
        PlanCreateRequest request = new PlanCreateRequest(일정_이름, 일정_태그, 일정_시작_날짜, 일정_종료_날짜, 일정_지역);

        /* when */
        PlanDetailResponse actual = planService.savePlan(request);

        /* then */
        assertAll(
                () -> assertEquals(일정_이름, actual.getTitle()),
                () -> assertEquals(일정_시작_날짜, actual.getStartDate()),
                () -> assertEquals(일정_종료_날짜, actual.getEndDate()),
                () -> assertEquals(일정_지역, actual.getRegion()),
                () -> assertEquals(true, actual.isPublished())
        );
    }

    /* 플랜 조회 */
    @Test
    @DisplayName("일정을 조회한다.")
    void 일정을_조회한다 () {
        /* given */
        PlanCreateRequest request = new PlanCreateRequest(일정_이름, 일정_태그, 일정_시작_날짜, 일정_종료_날짜, 일정_지역);
        PlanDetailResponse response = planService.savePlan(request);

        /* when */
        PlanDetailResponse actual = planService.getPlan(response.getPlanId());

        /* then */
        assertAll(
                () -> assertEquals(일정_이름, actual.getTitle()),
                () -> assertEquals(일정_시작_날짜, actual.getStartDate()),
                () -> assertEquals(일정_종료_날짜, actual.getEndDate()),
                () -> assertEquals(일정_지역, actual.getRegion()),
                () -> assertEquals(true, actual.isPublished())
        );
    }

    @Test
    @DisplayName("존재하지 않는 일정을 조회하면 예외가 발생한다.")
    void 존재하지_않는_일정을_조회하면_예외가_발생한다 () {
        /* given */
        /* when */
        /* then */
        assertThatThrownBy(() -> planService.getPlan(0L))
                .isInstanceOf(NoSuchPlanException.class);

    }

    @Test
    @DisplayName("삭제된 일정을 조회하면 예외가 발생한다.")
    void 삭제된_일정을_조회하면_예외가_발생한다 () {
        /* given */
        PlanCreateRequest request = new PlanCreateRequest(일정_이름, 일정_태그, 일정_시작_날짜, 일정_종료_날짜, 일정_지역);
        PlanDetailResponse response = planService.savePlan(request);
        planService.deletePlan(response.getPlanId());

        /* when */
        /* then */
        assertThatThrownBy(() -> planService.getPlan(response.getPlanId()))
                .isInstanceOf(NoSuchPlanException.class);
    }

    /* 플랜 복제 */
    @Test
    @DisplayName("일정을 복제한다.")
    void 일정을_복제한다 () {
        /* given */
        PlanCreateRequest request = new PlanCreateRequest(일정_이름, 일정_태그, 일정_시작_날짜, 일정_종료_날짜, 일정_지역);
        PlanDetailResponse response = planService.savePlan(request);
        PlanIdRequest planIdRequest = new PlanIdRequest(response.getPlanId());

        /* when */
        PlanResponse actual = planService.copyPlan(planIdRequest);

        /* then */
        assertAll(
                () -> assertEquals(일정_이름 + " 복사본", actual.getTitle()),
                () -> assertEquals(일정_태그, actual.getTags()),
                () -> assertEquals(일정_시작_날짜, actual.getStartDate()),
                () -> assertEquals(일정_종료_날짜, actual.getEndDate()),
                () -> assertEquals(일정_지역, actual.getRegion()),
                () -> assertEquals(true, actual.isPublished())
        );
    }

    /* 플랜 수정 (제목, 여행 일자, 태그) */
    @Test
    @DisplayName("일정을 수정한다.")
    void 일정을_수정한다 () {
        /* given */
        PlanCreateRequest request = new PlanCreateRequest(일정_이름, 일정_태그, 일정_시작_날짜, 일정_종료_날짜, 일정_지역);

        PlanDetailResponse response = planService.savePlan(request);

        Plan savedPlan = planRepository.getById(response.getPlanId());

        PlanUpdateRequest planUpdateRequest = new PlanUpdateRequest(
                savedPlan.getId(),
                "수정된 일정 이름",
                List.of("수정된 태그1", "수정된 태그2"),
                일정_시작_날짜,
                일정_종료_날짜,
                true,
                일정_예산_수정_요청,
                true,
                일정_지출_수정_요청
        );

        /* when */
        PlanDetailResponse actual = planService.updatePlan(planUpdateRequest);

        /* then */
        assertAll(
                () -> assertEquals("수정된 일정 이름", actual.getTitle()),
                () -> assertEquals(List.of("수정된 태그1", "수정된 태그2"), actual.getTags()),
                () -> assertEquals(일정_시작_날짜, actual.getStartDate()),
                () -> assertEquals(일정_종료_날짜, actual.getEndDate()),
                () -> assertEquals(일정_지역, actual.getRegion()),
                () -> assertEquals(true, actual.isPublished())
        );
    }

    /* 플랜 삭제 */
    @Test
    @DisplayName("일정을 삭제한다.")
    void 일정을_삭제한다 () {
        /* given */
        PlanCreateRequest request = new PlanCreateRequest(일정_이름, 일정_태그, 일정_시작_날짜, 일정_종료_날짜, 일정_지역);
        PlanDetailResponse response = planService.savePlan(request);

        /* when */
        planService.deletePlan(response.getPlanId());

        /* then */
        assertThatThrownBy(() -> planService.getPlan(response.getPlanId()))
                .isInstanceOf(NoSuchPlanException.class);
    }

}