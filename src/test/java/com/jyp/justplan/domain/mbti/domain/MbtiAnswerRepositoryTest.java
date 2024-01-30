package com.jyp.justplan.domain.mbti.domain;

import static org.assertj.core.api.Assertions.*;

import com.jyp.justplan.domain.mbti.domain.Repository.MbtiAnswerRepository;
import com.jyp.justplan.domain.mbti.exception.MbtiAnswerNotFoundException;
import com.jyp.justplan.domain.mbti.MbtiTestConfig;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MbtiAnswerRepositoryTest extends MbtiTestConfig {

    @Autowired
    private MbtiAnswerRepository mbtiAnswerRepository;

    @Test
    void 사용자에게_입력받은_리스트로_결과리스트를_조회한다() {
        // given
        List<Long> request = List.of(3L, 1L, 8L, 13L, 16L, 18L, 34L, 35L, 29L, 43L, 37L, 47L);
        // when
        List<MbtiAnswer> findByIds = mbtiAnswerRepository.findAndValidateByIds(request);
        // then
        assertThat(findByIds).hasSize(12);
    }

    @Test
    void 결과아이디가_아닌_값이_있는경우_에러를_발생시킨다() {
        // given
        List<Long> request = List.of(3L, 1L, 8L, 13L, 16L, 18L, 34L, 35L, 29L, 43L, 37L, 4700L);
        // when then
        assertThatThrownBy(() -> mbtiAnswerRepository.findAndValidateByIds(request))
            .isInstanceOf(MbtiAnswerNotFoundException.class)
            .hasMessage("올바른 answerId가 아닙니다: 4700");
    }
}