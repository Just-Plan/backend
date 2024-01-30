package com.jyp.justplan.domain.mbti.domain;

import static org.assertj.core.api.Assertions.*;

import com.jyp.justplan.domain.mbti.MbtiTestConfig;
import com.jyp.justplan.domain.mbti.domain.Repository.MbtiQuestionRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MbtiQuestionRepositoryTest extends MbtiTestConfig {
    @Autowired
    private MbtiQuestionRepository mbtiQuestionRepository;

    @Test
    void 모든_질문을_가져온다() {
        // given when
        List<MbtiQuestion> all = mbtiQuestionRepository.findAll();
        // then
        assertThat(all).hasSize(24);
    }
}