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