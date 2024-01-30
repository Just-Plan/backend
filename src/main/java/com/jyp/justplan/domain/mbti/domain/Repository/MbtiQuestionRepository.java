package com.jyp.justplan.domain.mbti.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MbtiQuestionRepository extends JpaRepository<MbtiQuestion, Long> {

    @Query("select distinct q from MbtiQuestion q join fetch q.answers")
    List<MbtiQuestion> findAll();


}