package com.jyp.justplan.domain.mbti.dto.response;

import com.jyp.justplan.domain.mbti.domain.MbtiQuestion;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class MbtiQuestionResponse {
    private String question;
    private List<MbtiAnswerResponse> answers = new ArrayList<>();

    public MbtiQuestionResponse() {
    }

    public MbtiQuestionResponse(final MbtiQuestion mbtiQuestion) {
        this.question = mbtiQuestion.getQuestion();
        mbtiQuestion.getAnswers().forEach(mbtiAnswer -> {
            this.answers.add(new MbtiAnswerResponse(mbtiAnswer));
        });
    }
}
