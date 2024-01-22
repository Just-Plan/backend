package com.jyp.justplan.domain.plan.dto.response;

import com.jyp.justplan.domain.plan.domain.Plan;
import com.jyp.justplan.domain.plan.domain.tag.Tag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

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
    private List<String> tags;

    /* 마지막 plan일 경우, originPlan은 null, 이를 처리하기 위한 생성자 */
    // TODO: 유지보수를 위해, depth를 유연하게 처리할 수 있도록 변경 (서비스 레이어에서 처리)
    public PlanResponse(long planId, String title, String region, ZonedDateTime startDate, ZonedDateTime endDate, boolean isPublic, List<String> tags) {
        this.planId = planId;
        this.title = title;
        this.region = region;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isPublic = isPublic;
        this.originPlan = null;
        this.tags = tags;
    }

    public static PlanResponse toDto(Plan plan, List<String> tags, PlanResponse originPlan) {
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

    public static PlanResponse toDto(Plan plan, List<String> tags) {
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
