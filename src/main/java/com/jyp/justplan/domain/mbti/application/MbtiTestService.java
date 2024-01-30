package com.jyp.justplan.domain.mbti.application;

import com.jyp.justplan.domain.mbti.domain.MbtiAnswer;
import com.jyp.justplan.domain.mbti.domain.Repository.MbtiAnswerRepository;
import com.jyp.justplan.domain.mbti.domain.MbtiQuestion;
import com.jyp.justplan.domain.mbti.domain.Repository.MbtiQuestionRepository;
import com.jyp.justplan.domain.mbti.domain.MbtiResult;
import com.jyp.justplan.domain.mbti.domain.MbtiType;
import com.jyp.justplan.domain.mbti.dto.response.MbtiQuestionResponse;
import com.jyp.justplan.domain.mbti.exception.MbtiAnswerCountMismatchException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MbtiTestService {
    private final MbtiQuestionRepository mbtiQuestionRepository;
    private final MbtiAnswerRepository mbtiAnswerRepository;

    public List<MbtiQuestionResponse> getRandomQuestions() {
        List<MbtiQuestion> mbtiQuestions = MbtiQuestion.selectRandomQuestions(mbtiQuestionRepository.findAll(), 3);
        return mbtiQuestions.stream().map(MbtiQuestionResponse::new).toList();
    }

    public MbtiType submitMbtiTest(List<Long> request) {
        if (request.size() != 12) {
            throw new MbtiAnswerCountMismatchException("답변은 12개여야 합니다.");
        }
        List<MbtiAnswer> findAnswers = mbtiAnswerRepository.findAndValidateByIds(request);
        MbtiResult result = new MbtiResult(findAnswers);

        String mbtiType = result.determineMbtiType();
        return MbtiType.valueOf(mbtiType.toLowerCase());
    }
}