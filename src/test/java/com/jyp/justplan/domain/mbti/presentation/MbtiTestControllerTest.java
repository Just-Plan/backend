package com.jyp.justplan.domain.mbti.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jyp.justplan.domain.mbti.dto.request.MbtiUserTestRequest;
import com.jyp.justplan.domain.mbti.MbtiTestConfig;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;


class MbtiTestControllerTest extends MbtiTestConfig {

    @Autowired
    private MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    // 테스트에 대한 의도가 분명해야한다.
    // test 스펙 입력, 출력
    // mockMvc -> 통합테스트 -> 서비스단에 핵심 로직이 있다.
    // 호출을 대신 하는건 별로
    // controller 테스트 -> 인증 테스트 -> hasRole, ... 헤더의 쿠키값을 인증하는 테스트많이 사용함 -> controller unit test

    @Test
    void 랜덤한_질문을_조회한다() throws Exception {
        // when then
        mockMvc.perform(get("/api/mbti/questions"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.data.length()").value(12))
            .andDo(print());
    }

    @Test
    void 입력값으로_MBTI를_분석한다() throws Exception{
        // given
        MbtiUserTestRequest request = new MbtiUserTestRequest(List.of(3L ,1L ,8L ,13L ,16L ,18L ,34L ,35L ,29L ,43L ,37L ,47L));
        // when then
        mockMvc.perform(post("/api/mbti/result")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.data").value("intj"))
            .andDo(print());
    }
}