package com.jyp.justplan.domain.mbti.dto.response;

import com.jyp.justplan.domain.mbti.domain.MbtiAnswer;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MbtiAnswerResponse {
    private Long answerId;
    private String answer;

    public MbtiAnswerResponse() {
    }

    public MbtiAnswerResponse(final MbtiAnswer mbtiAnswer) {
        this.answerId = mbtiAnswer.getId();
        this.answer = mbtiAnswer.getAnswer();
    }
}
