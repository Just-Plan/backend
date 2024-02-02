package com.jyp.justplan.domain.place.presentation;

import com.jyp.justplan.api.response.ApiResponseDto;
import com.jyp.justplan.domain.place.application.PlaceService;
import com.jyp.justplan.domain.place.dto.request.PlaceListRequest;
import com.jyp.justplan.domain.place.dto.request.PlaceRequest;
import com.jyp.justplan.domain.place.dto.request.PlaceUpdatesWrapper;
import com.jyp.justplan.domain.place.dto.response.PlaceResponse;
import com.jyp.justplan.domain.place.dto.response.SchedulePlacesResponse;
import com.jyp.justplan.domain.user.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Place", description = "장소 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class PlaceController {

    private final PlaceService placeService;

    /*CREATE*/
    @Operation(summary = "장소 생성", description = "장소보관함에 장소를 추가합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @PostMapping("/place/planId/{planId}")
    public ApiResponseDto<?> createPlace(
        @Parameter(description = "플랜 아이디", example = "1", required = true) @PathVariable Long planId,
        @RequestBody PlaceListRequest placeRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        placeService.createPlace(placeRequest, planId, userDetails.getUserId());
        return ApiResponseDto.successWithoutDataResponse();
    }

    // 일정에 대한 장소 전체 조회
    @Operation(summary = "전체 장소 조회", description = "일정에 대한 전체 장소를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @GetMapping("/place/planId/{planId}")
    public ApiResponseDto<SchedulePlacesResponse> getPlacesByPlanId(
        @PathVariable Long planId
    ) {
        return ApiResponseDto.successResponse(placeService.findPlacesByPlanId(planId));
    }


    // 장소 업데이트
    @Operation(summary = "장소 수정", description = "장소 수정합니다.(day, orderNum, memo)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @PatchMapping("/place/update")
    public ApiResponseDto<?> updatePlace(@RequestBody PlaceUpdatesWrapper updatesWrapper) {

        placeService.updatePlaces(updatesWrapper);

        return ApiResponseDto.successWithoutDataResponse();
    }

    // 장소 상세 조회
}