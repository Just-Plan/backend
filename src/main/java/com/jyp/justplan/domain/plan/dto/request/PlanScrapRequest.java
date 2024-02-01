package com.jyp.justplan.domain.plan.dto.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanScrapRequest {
    private Long planId;
    private boolean scrap;
}
