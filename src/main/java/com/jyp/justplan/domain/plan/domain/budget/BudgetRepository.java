package com.jyp.justplan.domain.plan.domain.budget;

import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.exception.budget.NoSuchBudgetException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
    boolean existsByPlan(Plan plan);
    Optional<Budget> findByPlan(Plan plan);
    default Budget getByPlan(final Plan plan) {
        return findByPlan(plan)
                .orElseThrow(() -> new NoSuchBudgetException("존재하지 않는 예산입니다."));
    }
}
