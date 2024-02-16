package com.jyp.justplan.domain.plan.dto.response;

import com.jyp.justplan.domain.city.dto.response.CityResponse;
import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.domain.tag.Tag;
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
public class PlanResponse {
    private long planId;
    private String title;
    private List<UserPlanResponse> users;
    private Boolean scrapped;
    private long scrapCount;
    private BudgetResponse budget;
    private CityResponse region;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private String photoUrl;
    private long nights;
    private long days;
    private boolean published;
    private List<String> tags;

    public static PlanResponse toDto(
            Plan plan,
            List<UserPlanResponse> users,
            String photoUrl,
            Boolean scrapped,
            long scrapCount,
            List<String> tags
    ) {
        Duration duration = Duration.between(plan.getStartDate(), plan.getEndDate());
        return new PlanResponse(
                plan.getId(),
                plan.getTitle(),
                users,
                scrapped,
                scrapCount,
                BudgetResponse.toDto(plan.getBudget()),
                new CityResponse(plan.getRegion()),
                plan.getStartDate(),
                plan.getEndDate(),
                photoUrl,
                duration.toDays(),
                duration.toDays() + 1,
                plan.isPublished(),
                tags
        );
    }
}
