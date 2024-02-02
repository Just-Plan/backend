package com.jyp.justplan.domain.user.domain;

import com.jyp.justplan.domain.mbti.domain.Mbti;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class User {
    // TODO: 데이터 유효성 검사를 위한 어노테이션 추가
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String email;
    @NotNull
    private String name;
    @NotNull
    private String password;

    private UserRole role;

    private String profile;

    // OAuth 로그인에 사용
    private String provider;
    private String providerId;

    //mbti
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private Mbti mbti;
//    private long mbtiId;
//    private String mbti;

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateMbti(String mbti) {}
}