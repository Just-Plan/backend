package com.jyp.justplan.domain.plan.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    default Plan getById(final Long id) {
        // TODO: Exception Handling
        return findById(id)
                .orElseThrow(() -> new RuntimeException("해당 플랜이 존재하지 않습니다."));
    }
}
