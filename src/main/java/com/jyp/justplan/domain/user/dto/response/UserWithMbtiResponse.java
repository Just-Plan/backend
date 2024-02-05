package com.jyp.justplan.domain.user.dto.response;

import com.jyp.justplan.domain.mbti.dto.response.MbtiResponse;
import com.jyp.justplan.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserWithMbtiResponse extends UserResponse {
    private MbtiResponse mbti;

    public UserWithMbtiResponse(String email, String name, MbtiResponse mbti) {
        super(email, name);
        this.mbti = mbti;
    }

    public static UserWithMbtiResponse toDto (User user) {
        return new UserWithMbtiResponse(
                user.getEmail(),
                user.getName(),
                user.getMbti() == null ? null : MbtiResponse.toDto(user.getMbti())
        );
    }
}
