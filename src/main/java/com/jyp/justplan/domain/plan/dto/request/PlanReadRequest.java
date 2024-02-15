package com.jyp.justplan.domain.plan.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Schema(description = "일정 조회 요청")
public class PlanReadRequest {
    @Schema(description = "일정 필터링 MBTI", example = "[\"ENFJ\", \"ENFP\"]")
    @NotNull
    private List<String> mbti;
}
