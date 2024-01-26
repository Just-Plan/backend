package com.jyp.justplan.domain.plan.dto.request;

import com.jyp.justplan.domain.plan.domain.Plan;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "지출 내역 수정 요청")
public class ExpenseUpdateRequest {
    @Schema(description = "식비", example = "200000")
    @NotNull
    private int food;

    @Schema(description = "교통비", example = "100000")
    @NotNull
    private int transportation;

    @Schema(description = "숙박비", example = "200000")
    @NotNull
    private int lodging;

    @Schema(description = "쇼핑비", example = "300000")
    @NotNull
    private int shopping;

    @Schema(description = "기타", example = "100000")
    @NotNull
    private int etc;
}
