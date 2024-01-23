package com.jyp.justplan.domain.user.dto.response;

import com.jyp.justplan.domain.user.domain.User;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserResponse {
    private String email;
    private String name;

    public static UserResponse toDto(User user) {
        return new UserResponse(user.getEmail(), user.getName());
    }
}
