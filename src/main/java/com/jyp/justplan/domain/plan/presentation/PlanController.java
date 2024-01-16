package com.jyp.justplan.domain.plan.presentation;

import com.jyp.justplan.api.response.ApiResponseDto;
import com.jyp.justplan.domain.plan.application.PlanService;
import com.jyp.justplan.domain.plan.dto.request.PlanCopyRequest;
import com.jyp.justplan.domain.plan.dto.request.PlanCreateRequest;
import com.jyp.justplan.domain.plan.dto.request.PlanUpdateRequest;
import com.jyp.justplan.domain.plan.dto.response.PlanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plan")
public class PlanController {
    private final PlanService planService;

    /* 플랜 조회 */
    @GetMapping("/{planId}")
    public ApiResponseDto<PlanResponse> getPlan (
            @PathVariable Long planId
    ) {
        PlanResponse response = planService.getPlan(planId);
        return ApiResponseDto.successResponse(response);
    }

    /* 플랜 생성 */
    @PostMapping
    public ApiResponseDto<PlanResponse> createPlan (
            @RequestBody PlanCreateRequest request
    ) {
        PlanResponse response = planService.savePlan(request);
        return ApiResponseDto.successResponse(response);
    }

    /* 플랜 복제 (가져오기) */
    @PostMapping("/copy")
    public ApiResponseDto<PlanResponse> copyPlan (
            @RequestBody PlanCopyRequest request
    ) {
        PlanResponse response = planService.copyPlan(request);
        return ApiResponseDto.successResponse(response);
    }

    /* 플랜 수정 (제목, 여행 일자) */
    @PatchMapping
    public ApiResponseDto<PlanResponse> updatePlan (
            @RequestBody PlanUpdateRequest request
    ) {
        PlanResponse response = planService.updatePlan(request);
        return ApiResponseDto.successResponse(response);
    }

    /* 플랜 삭제 */
    @DeleteMapping("/{planId}")
    public ApiResponseDto<?> deletePlan (
            @PathVariable Long planId
    ) {
        planService.deletePlan(planId);
        return ApiResponseDto.successWithoutDataResponse();
    }
}
