package com.jyp.justplan.domain;

import com.jyp.justplan.api.exception.BusinessException;
import com.jyp.justplan.api.response.ApiResponseDto;
import com.jyp.justplan.api.response.ResponseCode;
import com.jyp.justplan.domain.s3.S3Service;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Test", description = "테스트 API")
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TestController {
    private final S3Service s3Service;

    @Operation(summary = "테스트 엔드포인트", description = "간단한 테스트 메시지를 반환합니다.")
    @GetMapping("/test")
    public ApiResponseDto<String> testEndpoint() {
        return ApiResponseDto.successResponse("Hello, this is a test endpoint!");
    }

    @Operation(summary = "에러 테스트 엔드포인트", description = "BusinessException을 발생시키는 테스트")
    @GetMapping("/testError")
    public void testError() {
        throw new BusinessException(ResponseCode.BAD_ERROR);
    }

    @Operation(summary = "유효성 검증 테스트 엔드포인트", description = "유효성 검증 실패시 BusinessException을 발생시킵니다.")
    @GetMapping("/testValidation")
    public void testValidation() {
        throw new BusinessException(ResponseCode.VALIDATION_ERROR, "에러 메시지를 직접 넣을 수 있습니다.");
    }

    @Operation(summary = "문자열 반복", description = "파라미터로 받은 문자열을 2번 반복합니다.")
    @Parameter(name = "str", description = "2번 반복할 문자열")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "문자열 반복",
            content = @Content(schema = @Schema(implementation = ApiResponseDto.class)))})

    @GetMapping("/returnStr")
    public ApiResponseDto<?> returnStr(@RequestParam String str) {
        String data = str + "\n" + str;
        return ApiResponseDto.successResponse(data);
    }

    @Operation(summary = "s3 테스트", description = "파라미터로 숫자를 받아서 s3에 업로드합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "s3 업로드 성공",
            content = @Content(schema = @Schema(implementation = ApiResponseDto.class)))})

    @GetMapping("/test-upload/{folderId}")
    public ApiResponseDto<String> testUpload(
        @Parameter(description = "폴더 아이디") @PathVariable Long folderId
    ) {
        return ApiResponseDto.successResponse(s3Service.uploadFile(folderId));
    }
}