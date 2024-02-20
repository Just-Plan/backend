package com.jyp.justplan.domain.user.domain;

import com.jyp.justplan.domain.BaseEntity;
import com.jyp.justplan.domain.mbti.domain.Mbti;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.SQLDelete;

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
@SQLDelete(sql = "UPDATE user SET deleted_at = current_timestamp WHERE id = ?")
public class User extends BaseEntity {
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

    @ColumnDefault("'https://just-plan.s3.ap-northeast-2.amazonaws.com/profiles/test%40naver.com/profile_1708407311433.jpg'")
    private String profile;

    @ColumnDefault("'https://just-plan.s3.ap-northeast-2.amazonaws.com/background/test%40naver.com_1708407598191.jpg'")
    private String background;

    private String introduction;

    // OAuth 로그인에 사용
    private String provider;
    private String providerId;

    //mbti
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private Mbti mbti;

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

    public void updateProfile(String profile) {this.profile = profile; }

    public void updateBackground(String background) {this.background = background;}

    public void updateIntroduction(String introduction) {this.introduction = introduction;}

    public void updateMbti(Mbti mbti) {this.mbti = mbti;}
}