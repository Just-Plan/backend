package com.jyp.justplan.domain.mbti.domain;

import com.jyp.justplan.domain.mbti.exception.MbtiAnswerNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MbtiAnswerRepository extends JpaRepository<MbtiAnswer, Long> {
    default List<MbtiAnswer> findAndValidateByIds(List<Long> ids) {
        List<MbtiAnswer> answers = new ArrayList<>();
        for (Long id : ids) {
            MbtiAnswer answer = getById(id);
            answers.add(answer);
        }
        return answers;
    }

    default MbtiAnswer getById(Long id) {
        return findById(id).orElseThrow(
            () -> new MbtiAnswerNotFoundException("올바른 answerId가 아닙니다: " + id));
    }
}