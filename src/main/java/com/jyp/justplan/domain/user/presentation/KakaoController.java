package com.jyp.justplan.domain.user.presentation;


import com.jyp.justplan.api.response.ApiResponseDto;
import com.jyp.justplan.domain.user.application.UserService;
import com.jyp.justplan.domain.user.domain.User;
import com.jyp.justplan.domain.user.domain.UserRepository;
import com.jyp.justplan.domain.user.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Tag(name = "kakao", description = "카카오로그인 관련 API Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class KakaoController {
    private final UserRepository userRepository;
    final UserService userService;
    // private final String frontHost = "http://localhost:8080";

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String iClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String iRedirectUri;

    @Value("${spring.security.oauth2.client.registration.kakao.logout-redirect-uri}")
    private String iLogoutRedirectUri;

    @Tag(name = "kakao", description = "카카오 로그인 관련 API Controller")
    @Operation(summary = "카카오 로그인 redirect", description = "카카오 로그인 후 메인화면 redirect 호출.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @GetMapping("/kakao/home")
    public void home(HttpServletResponse response) throws IOException {

        response.sendRedirect(iRedirectUri);
    }

    @Tag(name = "kakao", description = "카카오 로그인 관련 API Controller")
    @Operation(summary = "카카오 로그인", description = "카카오 로그인을 수행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @GetMapping("/kakao/login")
    public void signin(HttpServletResponse response) throws IOException {
        String url = "https://justplan.site:8080/oauth2/authorization/kakao";
        response.sendRedirect(url);
    }

    @Tag(name = "kakao", description = "카카오 로그인 관련 API Controller")
    @Operation(summary = "카카오 로그아웃 redirect", description = "카카오 로그아웃 후 redirect 수행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @GetMapping("/security-login/login")
    public void kakaoLogout(HttpServletResponse response) throws Exception {
        response.sendRedirect("/kakao/login");
    }

    // redirect
    @Tag(name = "kakao", description = "카카오 로그인 관련 API Controller")
    @Operation(summary = "카카오 로그아웃", description = "카카오 로그아웃을 수행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @GetMapping("/kakao/unlink")
    public void getKakaoAuthUrl(HttpServletResponse response) throws Exception {

            String url = "https://kauth.kakao.com/oauth/logout"
                            +"?client_id="+iClientId+"&logout_redirect_uri="+iLogoutRedirectUri;

            response.sendRedirect(url);
    }
}