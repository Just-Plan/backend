package com.jyp.justplan.domain.plan.dto.response;

import com.jyp.justplan.domain.city.dto.response.CityResponse;
import com.jyp.justplan.domain.plan.domain.Plan;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanWithAccountBookResponse {
    private long planId;
    private String title;
    private CityResponse region;
    private BudgetResponse budget;
    private boolean useExpense;
    private ExpenseResponse expense;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private long nights;
    private long days;

    public static PlanWithAccountBookResponse toDto(
            Plan plan,
            BudgetResponse budget,
            ExpenseResponse expense
    ) {
        Duration duration = Duration.between(plan.getStartDate(), plan.getEndDate());
        return new PlanWithAccountBookResponse(
                plan.getId(),
                plan.getTitle(),
                new CityResponse(plan.getRegion()),
                budget,
                plan.isUseExpense(),
                expense,
                plan.getStartDate(),
                plan.getEndDate(),
                duration.toDays(),
                duration.toDays() + 1
        );
    }
}
