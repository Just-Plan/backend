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

    private Boolean scrapped;
    private long scrapCount;

    private CityResponse region;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private String photoUrl;

    private boolean published;
    private OriginPlanResponse originPlan;
    private List<String> tags;

    /* 가계부 */
    private BudgetResponse budget;
    private boolean useExpense;
    private ExpenseResponse expense;

    public static PlanDetailResponse toDto (
            Plan plan,
            List<UserPlanResponse> users,
            String photoUrl,
            Boolean scrapped,
            long scrapCount,
            List<String> tags,
            CityResponse cityResponse
    ) {
        return new PlanDetailResponse(
                plan.getId(),
                plan.getTitle(),
                users,
                scrapped,
                scrapCount,
                cityResponse,
                plan.getStartDate(),
                plan.getEndDate(),
                photoUrl,
                plan.isPublished(),
                null,
                tags,
                BudgetResponse.toDto(plan.getBudget()),
                plan.isUseExpense(),
                ExpenseResponse.toDto(plan.getExpense())
        );
    }

    public static PlanDetailResponse toDto (
            Plan plan,
            List<UserPlanResponse> users,
            String photoUrl,
            Boolean scrapped,
            long scrapCount,
            List<String> tags,
            CityResponse cityResponse,
            OriginPlanResponse originPlan
    ) {
        return new PlanDetailResponse(
                plan.getId(),
                plan.getTitle(),
                users,
                scrapped,
                scrapCount,
                cityResponse,
                plan.getStartDate(),
                plan.getEndDate(),
                photoUrl,
                plan.isPublished(),
                originPlan,
                tags,
                BudgetResponse.toDto(plan.getBudget()),
                plan.isUseExpense(),
                ExpenseResponse.toDto(plan.getExpense())
        );
    }
}
