package com.jyp.justplan.domain.place.presentation;

import com.jyp.justplan.api.response.ApiResponseDto;
import com.jyp.justplan.domain.place.application.PlaceService;
import com.jyp.justplan.domain.place.dto.request.PlaceRequest;
import com.jyp.justplan.domain.place.dto.request.PlaceUpdatesWrapper;
import com.jyp.justplan.domain.place.dto.response.PlaceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Place", description = "장소 API")
@RestController
@RequestMapping("/api/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    /*CREATE*/
    @Operation(summary = "장소 생성", description = "일정 내 장소를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @PostMapping
    public ApiResponseDto<PlaceResponse> createPlace(@RequestBody PlaceRequest placeRequest) {
        PlaceResponse response = placeService.createPlace(placeRequest);
        return ApiResponseDto.successResponse(response);
    }

    /*READ*/
    @Operation(summary = "단일 장소 조회", description = "단일 장소를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @GetMapping("/{id}")
    public ApiResponseDto<PlaceResponse> getPlace(@PathVariable Long id) {
        PlaceResponse response = placeService.findPlaceById(id);
        return ApiResponseDto.successResponse(response);
    }

    /*UPDATE*/
    @Operation(summary = "장소 업데이트", description = "장소를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @PatchMapping("/update")
    public ApiResponseDto<?> updatePlace(@RequestBody PlaceUpdatesWrapper updatesWrapper) {
        placeService.updatePlaces(updatesWrapper);
        return ApiResponseDto.successWithoutDataResponse();
    }

}
