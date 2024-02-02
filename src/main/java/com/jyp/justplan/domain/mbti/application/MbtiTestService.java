package com.jyp.justplan.domain.mbti.application;

import com.jyp.justplan.domain.mbti.domain.*;
import com.jyp.justplan.domain.mbti.dto.response.MbtiQuestionResponse;
import com.jyp.justplan.domain.mbti.exception.MbtiAnswerCountMismatchException;
import com.jyp.justplan.domain.mbti.exception.MbtiNotFoundException;
import com.jyp.justplan.domain.user.domain.User;
import com.jyp.justplan.domain.user.exception.UserNotFoundException;
import java.util.List;

import com.jyp.justplan.domain.mbti.exception.MbtiAnswerNotFoundException;
import com.jyp.justplan.domain.user.domain.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MbtiTestService {
    private final MbtiQuestionRepository mbtiQuestionRepository;
    private final MbtiAnswerRepository mbtiAnswerRepository;
    private final MbtiTestRepository mbtiTestRepository;
    private final UserRepository userRepository;

    public List<MbtiQuestionResponse> getRandomQuestions() {
        List<MbtiQuestion> mbtiQuestions = MbtiQuestion.selectRandomQuestions(mbtiQuestionRepository.findAll(), 3);
        return mbtiQuestions.stream().map(MbtiQuestionResponse::new).toList();
    }

    @Transactional
    public MbtiType submitMbtiTest(List<Long> request, Long userId) {
        if (request.size() != 12) {
            throw new MbtiAnswerCountMismatchException("답변은 12개여야 합니다.");
        }
        List<MbtiAnswer> findAnswers = mbtiAnswerRepository.findAndValidateByIds(request);
        MbtiResult result = new MbtiResult(findAnswers);

        String mbtiType = result.determineMbtiType();
        if (userId == null) {
            return MbtiType.valueOf(mbtiType.toLowerCase());
        }

        User findUser = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("사용자 정보를 찾을 수 없습니다: " + userId));
        Mbti findMbti = mbtiTestRepository.findByMbti(mbtiType).orElseThrow(() -> new MbtiNotFoundException("MBTI 정보를 찾을 수 없습니다: " + mbtiType));
        findUser.setMbti(findMbti);
        return MbtiType.valueOf(mbtiType.toLowerCase());
    }
}