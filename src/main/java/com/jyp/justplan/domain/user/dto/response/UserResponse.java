package com.jyp.justplan.domain.user.dto.response;

import com.jyp.justplan.domain.mbti.domain.Mbti;
import com.jyp.justplan.domain.user.domain.User;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserResponse {
    private String email;
    private String name;
    private long totalScrap;
    private long totalUserPlan;
    private String introduction;
    private String mbtiName;
    private String profile;
    private String background;

    public UserResponse(String email, String name) {
        this.email = email;
        this.name = name;
    }


    public static UserResponse toDto(User user) {
        return new UserResponse(user.getEmail(), user.getName());
    }

    public static UserResponse toTotDto(User user, long totalScrap, long totalUserPlan, Mbti mbti) {
        return new UserResponse(user.getEmail(), user.getName(), totalScrap, totalUserPlan, user.getIntroduction(), mbti.getMbti(), user.getProfile(), user.getBackground());
    }
}
