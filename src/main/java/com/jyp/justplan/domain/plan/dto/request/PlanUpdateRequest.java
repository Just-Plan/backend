package com.jyp.justplan.domain.plan.dto.request;

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
@Schema(description = "일정 수정 요청")
public class PlanUpdateRequest {
    @Schema(description = "일정 ID", example = "1")
    private long planId;

    @Schema(description = "일정 제목", example = "대전 떠나보자고")
    private String title;

    @Schema(description = "일정 태그", example = "[\"빵순이\", \"먹방\"]")
    private Set<String> tags;

    @Schema(description = "일정 시작 날짜", example = "2021-08-01T00:00:00+09:00")
    private ZonedDateTime startDate;

    @Schema(description = "일정 종료 날짜", example = "2021-08-03T00:00:00+09:00")
    private ZonedDateTime endDate;
}