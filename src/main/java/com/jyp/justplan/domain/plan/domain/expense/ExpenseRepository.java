package com.jyp.justplan.domain.plan.domain.expense;

import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.exception.expense.NoSuchExpenseException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    boolean existsByPlan(Plan plan);
    Optional<Expense> findByPlan(Plan plan);
    default Expense getByPlan(final Plan plan) {
        return findByPlan(plan)
                .orElseThrow(() -> new NoSuchExpenseException("존재하지 않는 지출입니다."));
    }
}
