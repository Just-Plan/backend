package com.jyp.justplan.domain.plan.domain;

import com.jyp.justplan.domain.plan.exception.NoSuchPlanException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    default Plan getById(final Long id) {
        // TODO: Exception Handling
        return findById(id)
                .map(plan -> {
                    if (plan.isDeleted()) {
                        throw new NoSuchPlanException("삭제된 일정입니다.");
                    }
                    return plan;
                })
                .orElseThrow(() -> new NoSuchPlanException("존재하지 않는 일정입니다."));
    }
}
