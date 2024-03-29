package com.jyp.justplan.domain.plan.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZonedDateTime;
import java.util.List;

import static com.jyp.justplan.domain.Expression.*;
import static com.jyp.justplan.domain.Expression.MAX_DATE;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "일정 수정 요청")
public class PlanUpdateRequest {
    @Schema(description = "일정 ID", example = "1")
    @NotNull
    private long planId;

    @Schema(description = "일정 제목", example = "대전 떠나보자고")
    @NotBlank(message = "일정 제목은 필수 값입니다.")
    @Size(max = PLAN_TITLE_MAX_LENGTH, message = "일정 제목은 최대 " + PLAN_TITLE_MAX_LENGTH + "자까지 입력 가능합니다.")
    private String title;

    @Schema(description = "일정 태그", example = "[\"빵순이\", \"먹방\"]")
    @NotNull(message = "일정 태그는 필수 값입니다.")
    private List<String> tags;

    @Schema(description = "일정 시작 날짜", example = "2021-08-01T00:00:00+09:00")
    @NotNull(message = "일정 시작 날짜는 필수 값입니다.")
    private ZonedDateTime startDate;

    @Schema(description = "일정 종료 날짜", example = "2021-08-03T00:00:00+09:00")
    @NotNull(message = "일정 종료 날짜는 필수 값입니다.")
    private ZonedDateTime endDate;

    @Schema(description = "일정 공개 여부", example = "true")
    @NotNull(message = "일정 공개 여부는 필수 값입니다.")
    private boolean published;

    @Schema(description = "예산")
    @NotNull(message = "예산은 필수 값입니다.")
    private BudgetUpdateRequest budget;

    @Schema(description = "지출 내역 사용 여부", example = "true")
    @NotNull(message = "지출 내역 사용 여부는 필수 값입니다.")
    private boolean useExpense;

    @Schema(description = "지출 내역")
    @NotNull(message = "지출 내역은 필수 값입니다.")
    private ExpenseUpdateRequest expense;

    @AssertTrue(message = "일정 시작 날짜는 종료 날짜보다 늦을 수 없습니다.")
    private boolean isStartDateBeforeEndDate() {
        return startDate.isBefore(endDate);
    }

    @AssertTrue(message = "일정 날짜는 유효한 범위 내에 있어야 합니다.")
    private boolean isDateInRange() {
        return (startDate.isEqual(MIN_DATE) || startDate.isAfter(MIN_DATE)) &&
                (startDate.isEqual(MAX_DATE) || startDate.isBefore(MAX_DATE)) &&
                (endDate.isEqual(MIN_DATE) || endDate.isAfter(MIN_DATE)) &&
                (endDate.isEqual(MAX_DATE) || endDate.isBefore(MAX_DATE));
    }
}