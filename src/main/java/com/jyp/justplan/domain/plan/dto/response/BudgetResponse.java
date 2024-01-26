package com.jyp.justplan.domain.plan.dto.response;

import com.jyp.justplan.domain.plan.domain.budget.Budget;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BudgetResponse {
    private int card;
    private int cash;

    public static BudgetResponse toDto(Budget budget) {
        return new BudgetResponse(budget.getCard(), budget.getCash());
    }
}
