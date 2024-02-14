package com.jyp.justplan.domain.user.presentation;


import com.amazonaws.util.StringUtils;
import com.jyp.justplan.api.response.ApiResponseDto;
import com.jyp.justplan.domain.user.UserDetailsImpl;
import com.jyp.justplan.domain.user.application.UserService;
import com.jyp.justplan.domain.user.domain.User;
import com.jyp.justplan.domain.user.dto.request.*;
import com.jyp.justplan.domain.user.dto.response.UserResponse;
import com.jyp.justplan.domain.user.dto.response.UserSignInResponseDto;
import com.jyp.justplan.domain.user.dto.response.UserSignInResponseInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Tag(name = "User", description = "사용자 관련 API Controller")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    final UserService userService;

    /* 회원가입 */
    @Tag(name = "User", description = "사용자 관련 API Controller")
    @Operation(summary = "회원가입", description = "회원가입을 수행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @PostMapping("/signup")
    public ApiResponseDto<UserResponse> signup(
            @RequestBody UserSignUpRequest userSignUpRequest
            ) {
        UserResponse user = userService.saveUser(userSignUpRequest);
        return ApiResponseDto.successResponse(user);
    }

    /* 로그인 */
    @Tag(name = "User", description = "사용자 관련 API Controller")
    @Operation(summary = "로그인", description = "로그인을 수행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @PostMapping("/signin")
    public ApiResponseDto<UserSignInResponseDto> signin(
            @RequestBody UserSignInRequest userSignInRequest,
            HttpServletResponse response
            ) {
        UserSignInResponseInfo user = userService.signin(userSignInRequest);

        System.out.println("user.getRefreshToken() = " + user.getRefreshToken());
        System.out.println("user.getAccessToken() = " + user.getAccessToken());

        Cookie cookie = new Cookie("refreshToken", user.getRefreshToken());
        cookie.setMaxAge(60 * 60 * 24 * 14);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);

        return ApiResponseDto.successResponse(UserSignInResponseDto.toDto(user));
    }


    /* 로그아웃 */
    @PostMapping("/signout")
    public ApiResponseDto<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setMaxAge(0);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);

        return ApiResponseDto.successWithoutDataResponse();
    }

    /* 회원정보 조회 */
    @Tag(name = "User", description = "사용자 관련 API Controller")
    @Operation(summary = "회원정보 조회", description = "회원정보를 조회한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @GetMapping("/read")
    public ApiResponseDto<UserResponse> readUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        UserResponse user = userService.readUser(userDetails);

        log.info("user plan cnt : " + user.getTotalUserPlan());

        return ApiResponseDto.successResponse(user);
    }

    /* 회원정보 수정 */
    @Tag(name = "User", description = "사용자 관련 API Controller")
    @Operation(summary = "회원정보 수정", description = "회원정보를 수정한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @PatchMapping("/update")
    public ApiResponseDto<UserResponse> updateUser(
            @RequestBody UserUpdateInfoRequest userUpdateInfoRequest,
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        UserResponse user = userService.updateUser(userUpdateInfoRequest, userDetails);
        return ApiResponseDto.successResponse(user);
    }

    /* 비밀번호 재설정 */
    @Tag(name = "User", description = "사용자 관련 API Controller")
    @Operation(summary = "비밀번호 재설정", description = "비밀번호를 재설정한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @PatchMapping("/reset-password")
    public ApiResponseDto<?> resetPassword(
            @RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest
            ) {
        userService.resetPassword(userUpdatePasswordRequest);
        return ApiResponseDto.successWithoutDataResponse();
    }

    /* 회원탈퇴 */
    @Tag(name = "User", description = "사용자 관련 API Controller")
    @Operation(summary = "회원탈퇴", description = "회원탈퇴를 수행한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @DeleteMapping("/delete")
    public ApiResponseDto<?> deleteUser(
            @RequestBody UserDeleteRequest userDeleteRequest,
            @Parameter(hidden = true)
            @AuthenticationPrincipal UserDetailsImpl userDetails
            ) {
        userService.deleteUser(userDeleteRequest, userDetails);
        return ApiResponseDto.successWithoutDataResponse();
    }

    /* Token 재발급 */
    @Tag(name = "User", description = "사용자 관련 API Controller")
    @Operation(summary = "Access Token 재발급", description = "Access Token을 재발급한다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = ApiResponseDto.class))),
    })
    @GetMapping("/reissue-token")
    public ApiResponseDto<UserSignInResponseDto> refreshToken(
            @CookieValue(value = "refreshToken") String refreshToken,
            HttpServletResponse response
            ) {
        UserSignInResponseInfo info = userService.reissueToken(refreshToken);

        Cookie cookie = new Cookie("refreshToken", info.getRefreshToken());
        cookie.setMaxAge(60 * 60 * 24 * 14);
        cookie.setHttpOnly(true);
        cookie.setPath("/");

        response.addCookie(cookie);

        return ApiResponseDto.successResponse(UserSignInResponseDto.toDto(info));
    }
}
