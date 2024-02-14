package com.jyp.justplan.domain.plan.dto.response;

import com.jyp.justplan.domain.mbti.domain.Mbti;
import com.jyp.justplan.domain.mbti.dto.response.MbtiResponse;
import com.jyp.justplan.domain.plan.domain.UserPlan;
import com.jyp.justplan.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPlanResponse {
    private String email;
    private String name;
    private String profileUrl;
    private MbtiResponse mbti;
    private boolean owner;

    public static UserPlanResponse toDto(UserPlan userPlan) {
        User user = userPlan.getUser();
        MbtiResponse mbti = user.getMbti() == null ? null : MbtiResponse.toDto(user.getMbti());
        return new UserPlanResponse(user.getEmail(), user.getName(), user.getProfile(), mbti, userPlan.isOwner());
    }
}