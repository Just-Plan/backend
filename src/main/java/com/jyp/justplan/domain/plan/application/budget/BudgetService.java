package com.jyp.justplan.domain.plan.application.budget;

import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.domain.budget.Budget;
import com.jyp.justplan.domain.plan.domain.budget.BudgetRepository;
import com.jyp.justplan.domain.plan.dto.request.BudgetUpdateRequest;
import com.jyp.justplan.domain.plan.dto.response.BudgetResponse;
import com.jyp.justplan.domain.plan.exception.budget.BudgetAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BudgetService {
    private final BudgetRepository budgetRepository;

    /* 예산 생성, 기본 값 0원 */
    @Transactional
    public BudgetResponse createBudget(Plan plan) {
        Budget budget = new Budget();
        plan.setBudget(budget);
        budgetRepository.save(budget);

        return BudgetResponse.toDto(budget);
    }

    /* 예산 수정 */
    @Transactional
    public BudgetResponse updateBudget(Budget budget, BudgetUpdateRequest request) {
        budget.updateBudget(request.getCard(), request.getCash());
        return BudgetResponse.toDto(budget);
    }
}
