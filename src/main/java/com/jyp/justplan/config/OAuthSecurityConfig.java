package com.jyp.justplan.config;


import com.jyp.justplan.api.oauth.PrincipalOauth2UserService;
import com.jyp.justplan.jwt.JwtAccessDeniedHandler;
import com.jyp.justplan.jwt.JwtAuthenticationEntryPoint;
import com.jyp.justplan.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Form Login에 사용하는 Security Config
 */
/* Security Config(Jwt Token Login에서 사용)와 같이 사용하면 에러 발생
kakao Login 진행하기 위해서는 Security Config에 @EnableWebSecurity, @Configuration 어노테이션 주석 처리*/
@Configuration
@EnableWebSecurity

/* 다른 인증, 인가 방식 적용을 위한 어노테이션
@EnableGlobalMethodSecurity(prePostEnabled = true)
*/
@RequiredArgsConstructor
public class OAuthSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PrincipalOauth2UserService principalOauth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource()).and() // CORS 설정 추가
            .csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 인증
                //.antMatchers("/user/info").authenticated()
                // 권한별 인가
                // .antMatchers("/kakao/admin/**").hasAuthority(UserRole.ADMIN.name())
                .antMatchers("/api/user/signup", "/api/user/signin", "api/user/signout",
                        "/api/user/reset-password", "/api/user/reissue-token").permitAll()
                .antMatchers("/api/email-auth/**").permitAll()
                .antMatchers("/signaling/**").permitAll()
                .antMatchers("/swagger-ui","/swagger-ui/**","swagger-ui/index.html").permitAll()
                .antMatchers(
                        "/v2/api-docs",
                        "/configuration/ui",
                        "/swagger-resources",
                        "/configuration/security",
                        "/swagger-ui.html",
                        "/webjars/**",
                        "/swagger-resources/configuration/ui",
                        "/swagger-resources/configuration/security",
                        "/swagger-ui.html"
                ).permitAll()
                .antMatchers("/**").permitAll() // 테스트를 위해 임시로 추가 (추후 제거해야 함)
                .antMatchers("/api/kakao/**", "/api/login/oauth2/**").permitAll()
                .anyRequest().authenticated()
                .and()
                // Form Login 방식 적용
                .formLogin()
                // 로그인 할 때 사용할 파라미터들
                .usernameParameter("email")
                .passwordParameter("password")
                .loginPage("/api/kakao/login")     // 로그인 페이지 URL
                .defaultSuccessUrl("/api/kakao/home")   // 로그인 성공 시 이동할 URL
                .failureUrl("/api/kakao/login")    // 로그인 실패 시 이동할 URL
                .and()
                .logout()
                .logoutUrl("/api/kakao/logout")
                .logoutSuccessUrl("/api/kakao/unlink")
                .invalidateHttpSession(true).deleteCookies("JSESSIONID")
                // OAuth 로그인
                .and()
                .oauth2Login()
                .loginPage("/api/kakao/login")
                .defaultSuccessUrl("/api/kakao/home")
                .userInfoEndpoint()
                .userService(principalOauth2UserService)
        ;
        http
                .exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .accessDeniedHandler(new JwtAccessDeniedHandler());
    }

    // CORS 관련 설정
    private CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedOriginPattern("*"); // 모든 출처를 패턴으로 허용
        corsConfiguration.setAllowCredentials(true); // 크로스 도메인 요청에서 쿠키를 지원
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration
    ) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        //configuration.addAllowedOrigin("*");
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "PATCH", "PUT","DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}