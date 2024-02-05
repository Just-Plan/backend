package com.jyp.justplan.domain.place.presentation;

import com.jyp.justplan.api.response.ApiResponseDto;
import com.jyp.justplan.domain.place.application.GooglePlaceService;
import com.jyp.justplan.domain.place.dto.response.GooglePlaceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Tag(name = "Place", description = "장소 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GooglePlaceController {

    private final GooglePlaceService placeService;

    /*SEARCH*/
    @Operation(summary = "장소 키워드 검색", description = "키워드에 해당하는 장소를 검색합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @GetMapping("/search/place/cityId/{cityId}")
    public Mono<ApiResponseDto<List<GooglePlaceResponse>>> getGooglePlaces(
        @Parameter(description = "도시 아이디", example = "1", required = true) @PathVariable Long cityId,
        @Parameter(description = "장소 키워드", example = "카페", required = true) @RequestParam String query
    ) {
        return placeService.getGooglePlace(query, cityId)
            .collectList()
            .map(ApiResponseDto::successResponse);
    }
}