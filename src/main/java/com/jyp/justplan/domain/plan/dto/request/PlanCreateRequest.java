package com.jyp.justplan.domain.plan.dto.request;

import com.jyp.justplan.domain.plan.domain.Plan;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.Set;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "일정 생성 요청")
public class PlanCreateRequest {
    @Schema(description = "일정 제목", example = "대전 가보자고")
    private String title;

    @Schema(description = "일정 태그", example = "[\"빵순이\", \"대전\"]")
    private Set<String> tags;

    @Schema(description = "일정 시작 날짜", example = "2024-08-01T00:00:00+09:00")
    private ZonedDateTime startDate;

    @Schema(description = "일정 종료 날짜", example = "2024-08-02T00:00:00+09:00")
    private ZonedDateTime endDate;

    // TODO: 지역 추가 시, 해당 enum이나 테이블 값으로 변경
    @Schema(description = "일정 지역", example = "대전")
    private String region;

    public Plan toEntity() {
        return new Plan(title, startDate, endDate, region);
    }
}
