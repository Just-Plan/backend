package com.jyp.justplan.domain.plan.presentation;

import com.jyp.justplan.api.response.ApiResponseDto;
import com.jyp.justplan.domain.plan.application.PlanService;
import com.jyp.justplan.domain.plan.dto.request.PlanIdRequest;
import com.jyp.justplan.domain.plan.dto.request.PlanCreateRequest;
import com.jyp.justplan.domain.plan.dto.request.PlanUpdateRequest;
import com.jyp.justplan.domain.plan.dto.response.PlanDetailResponse;
import com.jyp.justplan.domain.plan.dto.response.PlanResponse;
import com.jyp.justplan.domain.plan.dto.response.PlansResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.awt.print.Pageable;
import java.util.List;

@Tag(name = "Plan", description = "일정 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/plan")
public class PlanController {
    private final PlanService planService;

    /* 전체 플랜 조회 */
    @Operation(summary = "일정 전체 조회", description = "전체 일정을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @GetMapping
    public ApiResponseDto<PlansResponse> getPlans (
            @RequestParam(required = false, defaultValue = "") String type,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sort
    ) {
        PlansResponse response = planService.getPlans(type, page, size, sort);

        return ApiResponseDto.successResponse(response);
    }

    /* 플랜 단일 조회 */
    @Operation(summary = "일정 조회", description = "일정 아이디에 대한 일정을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @Parameter(name = "planId", description = "조회할 일정의 아이디", required = true, example = "1")
    @GetMapping("/{planId}")
    public ApiResponseDto<PlanDetailResponse> getPlan (
            @PathVariable Long planId
    ) {
        PlanDetailResponse response = planService.getPlan(planId);
        return ApiResponseDto.successResponse(response);
    }

    /* 플랜 생성 */
    @Operation(summary = "일정 생성", description = "일정을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @PostMapping
    public ApiResponseDto<PlanDetailResponse> createPlan (
            @Parameter(description = "일정 생성을 위한 데이터", required = true)
            @Valid @RequestBody PlanCreateRequest request
    ) {
        PlanDetailResponse response = planService.savePlan(request);
        return ApiResponseDto.successResponse(response);
    }

    /* 플랜 복제 (가져오기) */
    @Operation(summary = "일정 복제", description = "일정을 복제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @Parameter(name = "planId", description = "복제할 일정의 아이디", required = true, example = "1")
    @PostMapping("/copy")
    public ApiResponseDto<PlanDetailResponse> copyPlan (
            @Valid @RequestBody PlanIdRequest request
    ) {
        PlanDetailResponse response = planService.copyPlan(request);
        return ApiResponseDto.successResponse(response);
    }

    /* 플랜 수정 (제목, 여행 일자, 태그) */
    @Operation(summary = "일정 수정", description = "일정을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @PatchMapping
    public ApiResponseDto<PlanDetailResponse> updatePlan (
            @Parameter(description = "수정하고자 하는 일정 아이디와 수정할 데이터를 포함한 dto", required = true)
            @Valid @RequestBody PlanUpdateRequest request
    ) {
        PlanDetailResponse response = planService.updatePlan(request);
        return ApiResponseDto.successResponse(response);
    }

    /* 플랜 삭제 */
    @Operation(summary = "일정 삭제", description = "일정을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @Parameter(name = "planId", description = "삭제하고자 하는 일정의 아이디", required = true, example = "1")
    @DeleteMapping("/{planId}")
    public ApiResponseDto<?> deletePlan (
            @PathVariable Long planId
    ) {
        planService.deletePlan(planId);
        return ApiResponseDto.successWithoutDataResponse();
    }
}