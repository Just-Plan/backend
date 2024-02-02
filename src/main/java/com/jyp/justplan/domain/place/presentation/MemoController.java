package com.jyp.justplan.domain.place.presentation;

import com.jyp.justplan.api.response.ApiResponseDto;
import com.jyp.justplan.domain.place.dto.request.MemoRequest;
import com.jyp.justplan.domain.place.dto.response.MemoResponse;
import com.jyp.justplan.domain.place.application.MemoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Memo", description = "메모 API")
@RestController
@RequestMapping("/api/memo")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    /*READ*/
    @Operation(summary = "메모 조회", description = "장소에 해당하는 메모를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @GetMapping("/{id}")
    public ApiResponseDto<MemoResponse> getMemo(@PathVariable Long id) {
        MemoResponse response = memoService.findMemoById(id);
        return ApiResponseDto.successResponse(response);
    }

    /*UPDATE*/
    @Operation(summary = "메모 업데이트", description = "메모를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @PatchMapping("/{id}")
    public ApiResponseDto<MemoResponse> updateMemo(@RequestBody MemoRequest memoRequest, @PathVariable Long id) {
        MemoResponse response = memoService.updateMemo(memoRequest, id);
        return ApiResponseDto.successResponse(response);
    }

    /*RESET*/
    @Operation(summary = "메모 리셋", description = "메모를 리셋합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @PatchMapping("/reset/{id}")
    public ApiResponseDto<MemoResponse> resetMemo(@PathVariable Long id) {
        MemoResponse response = memoService.resetMemo(id);
        return ApiResponseDto.successResponse(response);
    }

    /*DELETE*/
    @Operation(summary = "메모 삭제", description = "메모를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @DeleteMapping("/{id}")
    public ApiResponseDto<?> deleteMemo(@PathVariable Long id) {
        memoService.deleteMemo(id);
        return ApiResponseDto.successWithoutDataResponse();
    }
}
