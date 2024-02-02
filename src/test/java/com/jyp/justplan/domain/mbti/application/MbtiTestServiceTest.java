package com.jyp.justplan.domain.mbti.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.jyp.justplan.domain.mbti.domain.MbtiType;
import com.jyp.justplan.domain.mbti.dto.response.MbtiQuestionResponse;
import com.jyp.justplan.domain.mbti.exception.MbtiAnswerCountMismatchException;
import com.jyp.justplan.domain.mbti.MbtiTestConfig;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class MbtiTestServiceTest extends MbtiTestConfig {
    @Autowired
    private MbtiTestService mbtiTestService;

    @Test
    void 랜덤한_질문을_가져온다() {
        // given when
        List<MbtiQuestionResponse> allQuestions = mbtiTestService.getRandomQuestions();
        // then
        assertThat(allQuestions).hasSize(12);
    }

    @Test
    void 입력된_정답_아이디로_MBTI를_결정한다() {
        // given
        List<Long> request = List.of(3L, 1L, 8L, 13L, 16L, 18L, 34L, 35L, 29L, 43L, 37L, 47L);
        // when
        MbtiType mbtiType = mbtiTestService.submitMbtiTest(request, null);
        // then
        assertThat(mbtiType).isEqualTo(MbtiType.intj);
    }

    @Test
    void 리스트의_길이가_12개가_아니면_에러가_발생한다() {
        // given
        List<Long> request = List.of(3L, 1L, 8L, 13L, 16L, 18L, 34L);
        // when then
        assertThatThrownBy(() -> mbtiTestService.submitMbtiTest(request, null))
            .isInstanceOf(MbtiAnswerCountMismatchException.class)
            .hasMessage("답변은 12개여야 합니다.");
    }
}