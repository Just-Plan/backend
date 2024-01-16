package com.jyp.justplan.domain.plan.dto.response;

import com.jyp.justplan.domain.plan.domain.Plan;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanResponse {
    private long planId;
    private String title;
    // TODO: 지역 추가 시, 해당 enum이나 테이블 값으로 변경
    private String region;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private boolean isPublic;
    private long originPlanId;

    public static PlanResponse toDto(Plan plan) {
        return new PlanResponse(plan.getPlanId(),
                plan.getTitle(),
                plan.getRegion(),
                plan.getStartDate(),
                plan.getEndDate(),
                plan.isPublic(),
                plan.getOriginPlan().getPlanId());
    }
}
