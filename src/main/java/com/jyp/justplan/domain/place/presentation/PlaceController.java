package com.jyp.justplan.domain.place.presentation;

import com.jyp.justplan.api.response.ApiResponseDto;
import com.jyp.justplan.domain.place.application.PlaceService;
import com.jyp.justplan.domain.place.dto.request.PlaceListRequest;
import com.jyp.justplan.domain.place.dto.request.PlacePlanUpdateDto;
import com.jyp.justplan.domain.place.dto.response.SchedulePlacesResponse;
import com.jyp.justplan.domain.user.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PatchMapping("/place/update/planId/{planId}")
    public ApiResponseDto<?> updatePlace(
        @Parameter(description = "플랜 아이디", example = "1", required = true) @PathVariable Long planId,
        @RequestBody(
            description = "Update Place Request",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = PlacePlanUpdateDto.class),
                examples = {
                    @ExampleObject(
                        name = "Update Place Example",
                        value =
    """
        {
          "dayUpdates": {
            "1": [
              { "placeId": 1, "orderNum": 1, "memo": { "content": "Updated content", "color": "WHITE" } },
              { "placeId": 2, "orderNum": 2, "memo": { "content": "Another updated content", "color": "WHITE" } }
            ],
            "2": [
              { "placeId": 3, "orderNum": 1, "memo": { "content": "Content for another day", "color": "WHITE" } }
            ]
          },
          "placeDeleteIds": [4,5]
        }
    """
                    )
                }
            )
        )
        @org.springframework.web.bind.annotation.RequestBody PlacePlanUpdateDto placePlanUpdateDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        placeService.updatePlaces(placePlanUpdateDto, userDetails.getUserId(), planId);
        return ApiResponseDto.successWithoutDataResponse();
    }

    // 장소 상세 조회


}

