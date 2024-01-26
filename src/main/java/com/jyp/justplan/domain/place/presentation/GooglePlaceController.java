package com.jyp.justplan.domain.place.presentation;

import com.jyp.justplan.api.response.ApiResponseDto;
import com.jyp.justplan.domain.place.application.GooglePlaceService;
import com.jyp.justplan.domain.place.dto.request.GooglePlaceRequest;
import com.jyp.justplan.domain.place.dto.response.GooglePlaceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/googlePlaces")
@RequiredArgsConstructor
public class GooglePlaceController {

    private final GooglePlaceService placeService;

    /*SEARCH*/
    @GetMapping("/search")
    public ApiResponseDto<List<GooglePlaceResponse>> getGooglePlaces(@RequestParam String query) {

        List<GooglePlaceResponse> response = placeService.getGooglePlace(query);
        return ApiResponseDto.successResponse(response);
    }

    /*CREATE*/
    @PostMapping
    public ApiResponseDto<GooglePlaceResponse> createGooglePlace(@RequestBody GooglePlaceRequest googlePlaceRequest) {
        GooglePlaceResponse response = placeService.createGooglePlace(googlePlaceRequest);
        return ApiResponseDto.successResponse(response);
    }

}
