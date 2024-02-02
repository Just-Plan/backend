package com.jyp.justplan.domain.mbti.domain;

import com.jyp.justplan.domain.mbti.domain.Mbti;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MbtiTestRepository extends JpaRepository<Mbti, Long> {
    Optional<Mbti> findByMbti(String mbti);
}
