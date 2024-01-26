package com.jyp.justplan.domain.place.presentation;

import com.jyp.justplan.api.response.ApiResponseDto;
import com.jyp.justplan.domain.place.application.GooglePlaceService;
import com.jyp.justplan.domain.place.dto.request.GooglePlaceRequest;
import com.jyp.justplan.domain.place.dto.response.GooglePlaceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Tag(name = "GooglePlace", description = "구글 장소 API")
@RestController
@RequestMapping("/api/googlePlace")
@RequiredArgsConstructor
public class GooglePlaceController {

    private final GooglePlaceService placeService;

    /*SEARCH*/
    @Operation(summary = "구글 장소 키워드 검색", description = "키워드에 해당하는 구글 장소를 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @GetMapping
    public ApiResponseDto<List<GooglePlaceResponse>> getGooglePlaces(@RequestParam String query) {

        List<GooglePlaceResponse> response = placeService.getGooglePlace(query);
        return ApiResponseDto.successResponse(response);
    }

    /*CREATE*/
    @Operation(summary = "구글 장소 생성", description = "구글 장소를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @PostMapping
    public ApiResponseDto<GooglePlaceResponse> createGooglePlace(@RequestBody GooglePlaceRequest googlePlaceRequest) {
        GooglePlaceResponse response = placeService.createGooglePlace(googlePlaceRequest);
        return ApiResponseDto.successResponse(response);
    }

}
