package com.jyp.justplan.domain.user.domain;

import com.jyp.justplan.domain.mbti.domain.Mbti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    void updateByMbti(Mbti mbti);
}
