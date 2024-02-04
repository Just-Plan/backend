package com.jyp.justplan.domain.plan.domain.budget;

import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.exception.budget.NoSuchBudgetException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long> {
}
