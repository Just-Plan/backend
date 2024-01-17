package com.jyp.justplan.domain.plan.dto.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlanUpdateRequest {
    private long planId;
    private String title;
    private Set<String> tags;
    // TODO: 지역 추가 시, 해당 enum이나 테이블 값으로 변경
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
}