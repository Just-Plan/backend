package com.jyp.justplan.domain.plan.dto.response;

import com.jyp.justplan.domain.plan.domain.expense.Expense;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExpenseResponse {
    private int food;
    private int transportation;
    private int lodging;
    private int shopping;
    private int etc;

    public static ExpenseResponse toDto(Expense expense) {
        return new ExpenseResponse(
                expense.getFood(),
                expense.getTransportation(),
                expense.getLodging(),
                expense.getShopping(),
                expense.getEtc()
        );
    }
}
