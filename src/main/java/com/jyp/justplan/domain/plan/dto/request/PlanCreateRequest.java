package com.jyp.justplan.domain.plan.dto.request;

import com.jyp.justplan.domain.plan.domain.Plan;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;

import static com.jyp.justplan.domain.Expression.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "일정 생성 요청")
public class PlanCreateRequest {
    @Schema(description = "일정 제목", example = "대전 가보자고")
    @NotBlank(message = "일정 제목은 필수 값입니다.")
    @Size(max = PLAN_TITLE_MAX_LENGTH, message = "일정 제목은 최대 " + PLAN_TITLE_MAX_LENGTH + "자까지 입력 가능합니다.")
    private String title;

    @Schema(description = "일정 태그", example = "[\"빵순이\", \"대전\"]")
    @NotNull(message = "일정 태그는 필수 값입니다.")
    private Set<String> tags;

    @Schema(description = "일정 시작 날짜", example = "2024-08-01T00:00:00+09:00")
    @NotNull(message = "일정 시작 날짜는 필수 값입니다.")
    private ZonedDateTime startDate;

    @Schema(description = "일정 종료 날짜", example = "2024-08-02T00:00:00+09:00")
    @NotNull(message = "일정 종료 날짜는 필수 값입니다.")
    private ZonedDateTime endDate;

    // TODO: 지역 추가 시, 해당 enum이나 테이블 값으로 변경
    @Schema(description = "일정 지역", example = "대전")
    @NotNull(message = "일정 지역은 필수 값입니다.")
    private String region;

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

    public Plan toEntity() {
        return new Plan(title, startDate, endDate, region);
    }
}
