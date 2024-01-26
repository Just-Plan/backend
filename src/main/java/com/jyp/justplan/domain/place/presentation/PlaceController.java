package com.jyp.justplan.domain.place.presentation;

import com.jyp.justplan.api.response.ApiResponseDto;
import com.jyp.justplan.domain.place.application.PlaceService;
import com.jyp.justplan.domain.place.dto.request.PlaceRequest;
import com.jyp.justplan.domain.place.dto.request.PlaceUpdatesWrapper;
import com.jyp.justplan.domain.place.dto.response.PlaceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/places")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;

    /*CREATE*/
    @PostMapping
    public ApiResponseDto<PlaceResponse> createPlace(@RequestBody PlaceRequest placeRequest) {
        PlaceResponse response = placeService.createPlace(placeRequest);
        return ApiResponseDto.successResponse(response);
    }

    /*READ*/
    @GetMapping("/{id}")
    public ApiResponseDto<PlaceResponse> getPlace(@PathVariable Long id) {
        PlaceResponse response = placeService.findPlaceById(id);
        return ApiResponseDto.successResponse(response);
    }

    /*UPDATE*/
    @PatchMapping("/update")
    public ApiResponseDto<?> updatePlace(@RequestBody PlaceUpdatesWrapper updatesWrapper) {
        placeService.updatePlaces(updatesWrapper);
        return ApiResponseDto.successWithoutDataResponse();
    }

}
