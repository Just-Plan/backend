package com.jyp.justplan.domain.plan.dto.request;

import com.jyp.justplan.domain.plan.domain.Plan;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Schema(description = "예산 수정 요청")
public class BudgetUpdateRequest {
    @Schema(description = "총 예산", example = "800000")
    private int cash;

    @Schema(description = "카드 예산", example = "500000")
    private int card;
}
