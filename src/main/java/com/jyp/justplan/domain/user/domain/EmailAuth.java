package com.jyp.justplan.domain.user.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class EmailAuth {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    private String email;
    @Column(columnDefinition = "BINARY(16)")
    private UUID emailToken;
    private boolean emailVerified = false;
    private LocalDateTime expiredAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user = null;

    public EmailAuth(String email, UUID emailToken) {
        this.email = email;
        this.emailToken = emailToken;
        /* 생성 후 5분 뒤에 만료 */
        this.expiredAt = LocalDateTime.now().plusMinutes(5);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void verifyEmail() {
        this.emailVerified = true;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiredAt);
    }
}
