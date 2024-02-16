package com.jyp.justplan.domain.user.dto.response;

import com.jyp.justplan.domain.mbti.domain.Mbti;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserSignInResponseInfo {
    private Long id;
    private String email;
    private String name;
    private String accessToken;
    private String refreshToken;
    private String mbtiName;
    private String profile;

    public static UserSignInResponseInfo toInfo(Long id, String email, String name, String accessToken, String refreshToken, Mbti mbti, String profile) {
        return new UserSignInResponseInfo(id, email, name, accessToken, refreshToken, (mbti == null) ? "" : mbti.getMbti(), profile);
    }
    public static UserSignInResponseInfo toInfo(Long id, String email, String name, String accessToken, String refreshToken) {
        return new UserSignInResponseInfo(id, email, name, accessToken, refreshToken,"", "");
    }
}