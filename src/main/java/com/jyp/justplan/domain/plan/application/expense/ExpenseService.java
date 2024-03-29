package com.jyp.justplan.domain.plan.application.expense;

import com.jyp.justplan.domain.plan.domain.expense.Expense;
import com.jyp.justplan.domain.plan.domain.expense.ExpenseRepository;
import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.dto.request.ExpenseUpdateRequest;
import com.jyp.justplan.domain.plan.dto.response.ExpenseResponse;
import com.jyp.justplan.domain.plan.exception.expense.ExpenseAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ExpenseService {
    private final ExpenseRepository expenseRepository;

    /* 지출 생성 */
    @Transactional
    public ExpenseResponse createExpense(Plan plan) {
        Expense expense = new Expense();
        plan.setExpense(expense);
        expenseRepository.save(expense);

        return ExpenseResponse.toDto(expense);
    }

    /* 지출 수정 */
    @Transactional
    public ExpenseResponse updateExpense(Expense expense, ExpenseUpdateRequest request) {
        expense.updateExpense(
                request.getFood(),
                request.getTransportation(),
                request.getLodging(),
                request.getShopping(),
                request.getEtc()
        );

        return ExpenseResponse.toDto(expense);
    }
}
