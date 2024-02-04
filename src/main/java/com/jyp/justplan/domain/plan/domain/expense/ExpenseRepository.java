package com.jyp.justplan.domain.plan.domain.expense;

import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.exception.expense.NoSuchExpenseException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}
