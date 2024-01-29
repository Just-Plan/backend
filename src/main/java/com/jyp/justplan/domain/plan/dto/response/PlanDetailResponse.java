package com.jyp.justplan.domain.plan.dto.response;

import com.jyp.justplan.domain.city.dto.response.CityResponse;
import com.jyp.justplan.domain.plan.domain.Plan;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanDetailResponse {
    private long planId;
    private String title;
    private List<UserPlanResponse> users;
    private CityResponse region;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private boolean published;
    private PlanResponse originPlan;
    private List<String> tags;

    /* 가계부 */
    private BudgetResponse budget;
    private boolean useExpense;
    private ExpenseResponse expense;

    /* TODO: 장소 */

    public static PlanDetailResponse toDto (
            Plan plan,
            List<UserPlanResponse> users,
            List<String> tags,
            CityResponse cityResponse,
            BudgetResponse budgetResponse,
            ExpenseResponse expenseResponse
    ) {
        return new PlanDetailResponse(
                plan.getId(),
                plan.getTitle(),
                users,
                cityResponse,
                plan.getStartDate(),
                plan.getEndDate(),
                plan.isPublished(),
                null,
                tags,
                budgetResponse,
                plan.isUseExpense(),
                expenseResponse
        );
    }

    public static PlanDetailResponse toDto (
            Plan plan,
            List<UserPlanResponse> users,
            List<String> tags,
            CityResponse cityResponse,
            PlanResponse originPlan,
            BudgetResponse budget,
            ExpenseResponse expense
    ) {
        return new PlanDetailResponse(
                plan.getId(),
                plan.getTitle(),
                users,
                cityResponse,
                plan.getStartDate(),
                plan.getEndDate(),
                plan.isPublished(),
                originPlan,
                tags,
                budget,
                plan.isUseExpense(),
                expense
        );
    }
}
