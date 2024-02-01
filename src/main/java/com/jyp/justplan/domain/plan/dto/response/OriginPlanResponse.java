package com.jyp.justplan.domain.plan.dto.response;

import com.jyp.justplan.domain.plan.domain.Plan;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OriginPlanResponse {
    private long planId;
    private String title;
    private List<UserPlanResponse> users;

    private OriginPlanResponse (long planId, String title, List<UserPlanResponse> users) {
        this.planId = planId;
        this.title = title;
        this.users = users;
    }

    public static OriginPlanResponse toDto (Plan plan, List<UserPlanResponse> users) {
        return new OriginPlanResponse(plan.getId(), plan.getTitle(), users);
    }
}
