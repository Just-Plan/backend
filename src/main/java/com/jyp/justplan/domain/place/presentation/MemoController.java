package com.jyp.justplan.domain.place.presentation;

import com.jyp.justplan.api.response.ApiResponseDto;
import com.jyp.justplan.domain.place.dto.request.MemoRequest;
import com.jyp.justplan.domain.place.dto.response.MemoResponse;
import com.jyp.justplan.domain.place.application.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    /*CREATE*/
    @PostMapping
    public ApiResponseDto<MemoResponse> createMemo(@RequestBody MemoRequest memoRequest) {
        MemoResponse response = memoService.createMemo(memoRequest);
        return ApiResponseDto.successResponse(response);
    }

    /*READ*/
    @GetMapping("/{id}")
    public ApiResponseDto<MemoResponse> getMemo(@PathVariable Long id) {
        MemoResponse response = memoService.findMemoById(id);
        return ApiResponseDto.successResponse(response);
    }

    /*UPDATE*/
    @PatchMapping("/{id}")
    public ApiResponseDto<MemoResponse> updateMemo(@RequestBody MemoRequest memoRequest, @PathVariable Long id) {
        MemoResponse response = memoService.updateMemo(memoRequest, id);
        return ApiResponseDto.successResponse(response);
    }

    /*RESET*/
    @PatchMapping("/reset/{id}")
    public ApiResponseDto<MemoResponse> resetMemo(@PathVariable Long id) {
        MemoResponse response = memoService.resetMemo(id);
        return ApiResponseDto.successResponse(response);
    }

    /*DELETE*/
    @DeleteMapping("/{id}")
    public ApiResponseDto<?> deleteMemo(@PathVariable Long id) {
        memoService.deleteMemo(id);
        return ApiResponseDto.successWithoutDataResponse();
    }
}
