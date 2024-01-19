package com.jyp.justplan.domain.plan.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Schema(description = "일정 ID 요청")
public class PlanIdRequest {
    @Schema(description = "일정 ID", example = "1")
    @NotNull
    private long originPlanId;
}
