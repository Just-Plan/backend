package com.jyp.justplan.domain.plan.presentation;

import com.jyp.justplan.domain.city.domain.City;
import com.jyp.justplan.domain.city.domain.CityRepository;
import com.jyp.justplan.domain.city.domain.Country;
import com.jyp.justplan.domain.plan.application.PlanService;
import com.jyp.justplan.domain.plan.domain.PlanRepository;
import com.jyp.justplan.domain.plan.dto.request.*;
import com.jyp.justplan.domain.plan.dto.response.PlanDetailResponse;
import com.jyp.justplan.domain.plan.dto.response.PlanResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PlanController.class)
@Transactional
class PlanControllerTest {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private PlanService planService;
    @MockBean
    private PlanRepository planRepository;
    @MockBean
    private CityRepository cityRepository;

    private final String 일정_이름 = "일정 이름";
    private final List<String> 일정_태그 = List.of("태그1", "태그2");
    private final ZonedDateTime 일정_시작_날짜 = ZonedDateTime.of(2024, 1, 19, 0, 0, 0 ,0, ZonedDateTime.now().getZone());
    private final ZonedDateTime 일정_종료_날짜 = ZonedDateTime.of(2024, 1, 20, 0, 0, 0 ,0, ZonedDateTime.now().getZone());
    private final long 일정_지역_아이디 = 1L;
    private final BudgetUpdateRequest 일정_예산_수정_요청 = new BudgetUpdateRequest(10000, 20000);
    private final ExpenseUpdateRequest 일정_지출_수정_요청 = new ExpenseUpdateRequest(10000, 20000, 30000, 40000, 50000);

    @BeforeEach
    void setUp () {
        Country country = new Country("테스트 국가", "Test Country", "Asia", "220");
        City city = new City("테스트 도시", "Test City", "Test city of Test country", "0.0", "0.0", "Asia/Seoul",  country, 9);

        cityRepository.save(city);
    }

    /* 플랜 조회 */
    @Test
    @DisplayName("일정을 조회한다.")
    void 일정을_조회한다 () throws Exception {
        /* given */
        PlanCreateRequest planCreateRequest = new PlanCreateRequest(일정_이름, 일정_태그, 일정_시작_날짜, 일정_종료_날짜, 일정_지역_아이디);
        PlanDetailResponse planResponse = planService.savePlan(planCreateRequest);

        /* when */
        /* then */
        mvc.perform(get("/api/plan/{id}", planResponse.getPlanId())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    /* 플랜 생성 */
    @Test
    @DisplayName("일정을 생성한다.")
    void 일정을_생성한다 () throws Exception {
        /* given */
        PlanCreateRequest planCreateRequest = new PlanCreateRequest(일정_이름, 일정_태그, 일정_시작_날짜, 일정_종료_날짜, 일정_지역_아이디);

        /* when */
        /* then */
        mvc.perform(post("/api/plan")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(planCreateRequest.toString()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("일정 생성 요청이 잘못된 경우 예외를 발생시킨다.")
    void 일정_생성_요청이_잘못된_경우_예외를_발생시킨다 () throws Exception {
        /* given */
        PlanCreateRequest planCreateRequest = new PlanCreateRequest(" ", 일정_태그, 일정_시작_날짜, 일정_종료_날짜, 일정_지역_아이디);

        /* when */
        /* then */
        mvc.perform(post("/api/plan")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(planCreateRequest.toString())
                )
                .andDo(print())
                .andExpect(status().isBadRequest());

    }

    /* 플랜 복제 (가져오기) */
    @Test
    @DisplayName("일정을 복제한다.")
    void 일정을_복제한다 () throws Exception {
        /* given */
        PlanIdRequest planIdRequest = new PlanIdRequest(1L);

        /* when */
        /* then */
        mvc.perform(post("/api/plan/copy")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(planIdRequest.toString()))
                .andDo(print())
                .andExpect(status().isOk());
    }


    /* 플랜 수정 */
    @Test
    @DisplayName("일정을 수정한다.")
    void 일정을_수정한다 () throws Exception {
        /* given */
        PlanUpdateRequest planUpdateRequest = new PlanUpdateRequest(
                1L,
                "수정된 일정 이름",
                일정_태그,
                일정_시작_날짜,
                일정_종료_날짜,
                true,
                일정_예산_수정_요청,
                true,
                일정_지출_수정_요청
        );

        /* when */
        /* then */
        mvc.perform(patch("/api/plan")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(planUpdateRequest.toString()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("일정 수정 요청이 잘못된 경우 예외를 발생시킨다.")
    void 일정_수정_요청이_잘못된_경우_예외를_발생시킨다 () throws Exception {
        /* given */
        PlanUpdateRequest planUpdateRequest = new PlanUpdateRequest(
                1L,
                " ",
                일정_태그,
                일정_시작_날짜,
                일정_종료_날짜,
                true,
                일정_예산_수정_요청,
                true,
                일정_지출_수정_요청
        );

        /* when */
        /* then */
        mvc.perform(patch("/api/plan")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(planUpdateRequest.toString()))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("일정 수정 시 지출 내역을 사용하는 경우 지출 내역이 null일 시 예외를 발생시킨다.")
    void 일정_수정_시_지출_내역을_사용하는_경우_지출_내역이_null일_시_예외를_발생시킨다 () {
        /* given */

        /* when */

        /* then */

    }


    /* 플랜 삭제 */
    @Test
    @DisplayName("일정을 삭제한다.")
    void 일정을_삭제한다 () throws Exception {
        /* given */
        /* when */
        /* then */
        mvc.perform(delete("/api/plan/{id}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }
}