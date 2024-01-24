package com.jyp.justplan.domain.user;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class UserDetailsImpl implements UserDetails {
    private String username;
    private String password;

    private boolean isAccountNonExpired = true; // 계정 만료 없음
    private boolean isAccountNonLocked = true; // 계정 잠김 없음
    private boolean isCredentialsNonExpired = true; // 계정 비밀번호 만료 없음
    private boolean enabled = true; // 계정 활성화
    private Collection<? extends GrantedAuthority> authorities;

    /* 추가 정보 */
    private String name;
    private long userId;

    public UserDetailsImpl(String username, String password, long userId, String name, Collection<? extends GrantedAuthority> grantedAuthorities) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.userId = userId;
        this.authorities = grantedAuthorities;
    }
}
