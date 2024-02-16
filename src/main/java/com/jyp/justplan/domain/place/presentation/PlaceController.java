package com.jyp.justplan.domain.place.presentation;

import com.jyp.justplan.api.response.ApiResponseDto;
import com.jyp.justplan.domain.place.application.PlaceService;
import com.jyp.justplan.domain.place.dto.request.PlaceListRequest;
import com.jyp.justplan.domain.place.dto.request.PlacePlanUpdateDto;
import com.jyp.justplan.domain.place.dto.response.PlaceDetailResponse;
import com.jyp.justplan.domain.place.dto.response.SchedulePlacesResponse;
import com.jyp.justplan.domain.place.dto.response.SchedulePlacesResponse.PlaceResponse;
import com.jyp.justplan.domain.user.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

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
        return ApiResponseDto.successResponse(placeService.createPlace(placeRequest, planId,
            userDetails.getUserId()));
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
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
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
    @Operation(summary = "장소 상세 조회", description = "장소에 대한 상셍 정보를 조회합니다. 구글 장소 아이디 or 이름, 위도, 경도로 조회")
    @GetMapping("/place/detail")
    public Mono<ApiResponseDto<PlaceDetailResponse>> getPlace(
        @Parameter(description = "장소 이름", example = "카페 델문도함덕점") @RequestParam(required = false) String name,
        @Parameter(description = "장소 위도", example = "33.5437787") @RequestParam(required = false) String latitude,
        @Parameter(description = "장소 경도", example = "126.6688353") @RequestParam(required = false) String longitude,
        @Parameter(description = "구글 장소 아이디", example = "ChIJf721YZkeDTURpGO42wZUb-E") @RequestParam(required = false) String googlePlaceId
    ) {
        return placeService.getPlaceDetail(name, latitude, longitude, googlePlaceId)
            .map(ApiResponseDto::successResponse);
    }


    // 장소 복제
    @Operation(summary = "장소 복제", description = "플랜 아이디로 장소를 복제합니다.")
    @PostMapping("/place/copy")
    public ApiResponseDto<?> clonePlace(
        @Parameter(description = "복제할 플랜 아이디", example = "1", required = true) @RequestParam Long originPlanId,
        @Parameter(description = "새로 만든 플랜 아이디", example = "2", required = true) @RequestParam Long newPlanId,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        var schedulePlacesResponse = placeService.clonePlace(userDetails,
            originPlanId, newPlanId);
        return ApiResponseDto.successResponse(schedulePlacesResponse);
    }

}