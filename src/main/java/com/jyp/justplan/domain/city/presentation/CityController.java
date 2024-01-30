package com.jyp.justplan.domain.city.presentation;

import com.jyp.justplan.api.response.ApiResponseDto;
import com.jyp.justplan.domain.city.application.CityService;
import com.jyp.justplan.domain.city.dto.response.CitiesResponse;
import com.jyp.justplan.domain.city.dto.response.CityResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "cities", description = "cities API")
@RestController
@RequiredArgsConstructor
public class CityController {
    private final CityService cityService;

    // 랜덤한 count 개의 도시 정보를 가져온다.
    @Operation(summary = "랜덤한 도시 호출", description = "랜덤한 n 개의 도시를 호출합니다.")
    @ApiResponse(responseCode = "200", description = "랜덤한 도시 호출")
    @GetMapping("/api/cities/random/{count}")
    public ApiResponseDto<CitiesResponse> getCities(
        @Parameter(description = "도시 리스트 길이", example = "10", required = true)
        @Valid @PathVariable() int count
    ) {
        var randomCities = cityService.getRandomCities(count);
        return ApiResponseDto.successResponse(randomCities);
    }

    // 키워드로 도시 검색하기
    @Operation(summary = "도시 검색", description = "도시를 검색합니다.")
    @ApiResponse(responseCode = "200", description = "도시 검색")
    @GetMapping("/api/cities/search")
    public ApiResponseDto<CitiesResponse> getCityDetail(
        @RequestParam(name = "cityName")
        @Parameter(description = "도시 이름", example = "서울", required = true) String cityName
    ) {
        var cityDetail = cityService.getCityDetailByPartialName(cityName);
        return ApiResponseDto.successResponse(cityDetail);
    }
}
