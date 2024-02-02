package com.jyp.justplan.domain.mbti.application;

import com.jyp.justplan.domain.mbti.domain.*;
import com.jyp.justplan.domain.mbti.dto.response.MbtiQuestionResponse;
import com.jyp.justplan.domain.mbti.exception.MbtiAnswerCountMismatchException;
import java.util.List;

import com.jyp.justplan.domain.mbti.exception.MbtiAnswerNotFoundException;
import com.jyp.justplan.domain.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MbtiTestService {
    private final MbtiQuestionRepository mbtiQuestionRepository;
    private final MbtiAnswerRepository mbtiAnswerRepository;

//    private final MbtiTestRepository mbtiTestRepository;
//    private final UserRepository userRepository;

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

//        Mbti mbti = mbtiTestRepository.findByMbti(mbtiType).orElseThrow(() -> new MbtiAnswerNotFoundException("해당하는 MBTI가 존재하지 않습니다."));
//
//        try {
//            userRepository.updateByMbti(mbti);
//        } catch (Exception e) {
//            throw new MbtiAnswerNotFoundException("Mbti 등록 중 오류가 발생하였습니다.");
//        }

        return MbtiType.valueOf(mbtiType.toLowerCase());
    }
}