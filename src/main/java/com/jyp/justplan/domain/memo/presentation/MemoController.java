package com.jyp.justplan.domain.memo.presentation;

import com.jyp.justplan.api.response.ApiResponseDto;
import com.jyp.justplan.domain.memo.dto.request.MemoRequestDto;
import com.jyp.justplan.domain.memo.dto.response.MemoResponseDto;
import com.jyp.justplan.domain.memo.application.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    /*CREATE*/
    @PostMapping
    public ApiResponseDto<MemoResponseDto> createMemo(@RequestBody MemoRequestDto memoRequestDto) {
        MemoResponseDto response = memoService.createMemo(memoRequestDto);
        return ApiResponseDto.successResponse(response);
    }

    /*READ*/
    @GetMapping("/{id}")
    public ApiResponseDto<MemoResponseDto> getMemo(@PathVariable Long id) {
        MemoResponseDto response = memoService.findMemoById(id);
        return ApiResponseDto.successResponse(response);
    }

    /*UPDATE*/
    @PatchMapping("/{id}")
    public ApiResponseDto<MemoResponseDto> updateMemo(@RequestBody MemoRequestDto memoRequestDto, @PathVariable Long id) {
        MemoResponseDto response = memoService.updateMemo(memoRequestDto, id);
        return ApiResponseDto.successResponse(response);
    }

    /*RESET*/
    @PatchMapping("/reset/{id}")
    public ApiResponseDto<MemoResponseDto> resetMemo(@PathVariable Long id) {
        MemoResponseDto response = memoService.resetMemo(id);
        return ApiResponseDto.successResponse(response);
    }

    /*DELETE*/
    @DeleteMapping("/{id}")
    public ApiResponseDto<?> deleteMemo(@PathVariable Long id) {
        memoService.deleteMemo(id);
        return ApiResponseDto.successWithoutDataResponse();
    }
}
