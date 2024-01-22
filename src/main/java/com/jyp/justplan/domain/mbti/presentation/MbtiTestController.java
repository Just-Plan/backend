package com.jyp.justplan.domain.mbti.presentation;

import com.jyp.justplan.api.response.ApiResponseDto;
import com.jyp.justplan.domain.mbti.application.MbtiTestService;
import com.jyp.justplan.domain.mbti.domain.MbtiType;
import com.jyp.justplan.domain.mbti.dto.request.MbtiUserTestRequest;
import com.jyp.justplan.domain.mbti.dto.response.MbtiQuestionResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "MBTI 테스트", description = "MBTI 테스트 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/mbti")
public class MbtiTestController {
    private final MbtiTestService mbtiQuestionService;

    // 질문과 답변 리스트 조회
    @Operation(summary = "MBTI 질문 조회", description = "랜덤한 MBTI 질문 12개를 조회한다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "성공",
            content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @GetMapping("/questions")
    public ApiResponseDto<List<MbtiQuestionResponse>> getQuestions() {
        List<MbtiQuestionResponse> randomQuestions = mbtiQuestionService.getRandomQuestions();
        return ApiResponseDto.successResponse(randomQuestions);
    }

    // 선택한 답변으로 mbti 결과 조회
    @Operation(summary = "MBTI 결과 조회", description = "선택한 답변으로 MBTI 결과를 조회한다.")
    @PostMapping("/result")
    public ApiResponseDto<MbtiType> submitMbtiTest(@RequestBody final MbtiUserTestRequest request) {
        MbtiType mbtiType = mbtiQuestionService.submitMbtiTest(request.getAnswers());
        return ApiResponseDto.successResponse(mbtiType);
    }
}