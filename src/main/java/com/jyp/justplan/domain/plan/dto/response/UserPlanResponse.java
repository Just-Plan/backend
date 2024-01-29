package com.jyp.justplan.domain.plan.dto.response;

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
    private String mbti;
    private boolean owner;

    public static UserPlanResponse toDto(UserPlan userPlan) {
        User user = userPlan.getUser();
        return new UserPlanResponse(user.getEmail(), user.getName(), user.getMbti(), userPlan.isOwner());
    }
}
