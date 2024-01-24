package com.jyp.justplan.domain.user.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmailAuthRepository extends JpaRepository<EmailAuth, Long> {
    EmailAuth findByEmail(String email);
    EmailAuth findByEmailToken(UUID emailToken);
}
