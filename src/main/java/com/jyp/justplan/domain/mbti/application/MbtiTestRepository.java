package com.jyp.justplan.domain.mbti.application;

import com.jyp.justplan.domain.mbti.domain.Mbti;

import java.util.Optional;

public interface MbtiTestRepository {
    Optional<Mbti> findByMbti(String mbti);
}
