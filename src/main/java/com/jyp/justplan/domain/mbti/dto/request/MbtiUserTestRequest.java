package com.jyp.justplan.domain.mbti.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.Getter;

@Schema(description = "MBTI 테스트 요청")
@Getter
public class MbtiUserTestRequest {
    @Schema(description = "답변 id 리스트", example = "[3,1,8,13,16,18,34,35,29,43,37,47]")
    private List<Long> answers;

    public MbtiUserTestRequest() {
    }

    public MbtiUserTestRequest(List<Long> answers) {
        this.answers = answers;
    }
}
