package com.jyp.justplan.domain.plan.dto.response;

import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.domain.tag.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

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
    private PlanResponse originPlan;
    private Set<String> tags;

    public PlanResponse(long planId, String title, String region, ZonedDateTime startDate, ZonedDateTime endDate, boolean isPublic, Set<String> tags) {
        this.planId = planId;
        this.title = title;
        this.region = region;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isPublic = isPublic;
        this.originPlan = null;
        this.tags = tags;
    }

    public static PlanResponse toDto(Plan plan, Set<String> tags, PlanResponse originPlan) {
        return new PlanResponse(plan.getId(),
                plan.getTitle(),
                plan.getRegion(),
                plan.getStartDate(),
                plan.getEndDate(),
                plan.isPublic(),
                originPlan,
                tags
                );
    }

    public static PlanResponse toDto(Plan plan, Set<String> tags) {
        return new PlanResponse(plan.getId(),
                plan.getTitle(),
                plan.getRegion(),
                plan.getStartDate(),
                plan.getEndDate(),
                plan.isPublic(),
                tags
        );
    }
}
