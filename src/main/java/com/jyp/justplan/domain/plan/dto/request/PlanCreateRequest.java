package com.jyp.justplan.domain.plan.dto.request;

import com.jyp.justplan.domain.plan.domain.Plan;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlanCreateRequest {
    private String title;
    // TODO: 지역 추가 시, 해당 enum이나 테이블 값으로 변경
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private String region;

    public Plan toEntity() {
        return new Plan(title, startDate, endDate, region);
    }
}
