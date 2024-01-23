package com.jyp.justplan.domain.user.dto.request;

import com.jyp.justplan.domain.user.domain.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserSignUpRequest {
    private String email;
    private String name;
    private String password;
    private long authId;

    public User toEntity() {
        return new User(email, name, password);
    }
}
