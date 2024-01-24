package com.jyp.justplan.domain.city.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jyp.justplan.domain.city.cityTestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

class CityControllerTest extends cityTestConfig {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @DisplayName("랜덤한 도시 정보 가져오기")
    @Test
    void 랜덤한_도시를_가져온다() throws Exception {
        // given
        int count = 5;

        // when then
        mockMvc.perform(get("/api/cities/random/{count}", count))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.data.cities.length()").value(count))
            .andDo(print());
    }

    @DisplayName("count 가 int가 아니면 4001 에러가 발생한다.")
    @Test
    void count가_int가_아니면_4001_에러가_발생한다() throws Exception {
        // given
        var count = "abc";

        // when then
        mockMvc.perform(get("/api/cities/random/{count}", count))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.code").value(4000))
            .andDo(print());
    }
}