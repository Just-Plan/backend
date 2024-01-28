package com.jyp.justplan.domain.plan.dto.response;

import com.jyp.justplan.domain.city.dto.response.CityResponse;
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
    private CityResponse region;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
    private boolean published;
    private List<String> tags;

    public static PlanResponse toDto(Plan plan, List<String> tags) {
        return new PlanResponse(plan.getId(),
                plan.getTitle(),
                new CityResponse(plan.getRegion()),
                plan.getStartDate(),
                plan.getEndDate(),
                plan.isPublished(),
                tags
        );
    }
}
