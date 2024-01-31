package com.jyp.justplan.domain.mbti.dto.response;

import com.jyp.justplan.domain.mbti.domain.Mbti;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MbtiResponse {
    private Long id;
    private String type;

    public static MbtiResponse toDto(Mbti mbti) {
        return new MbtiResponse(mbti.getId(), mbti.getMbti());
    }
}
