package com.jyp.justplan.domain.user.application;


import com.jyp.justplan.domain.user.UserDetailsImpl;
import com.jyp.justplan.domain.user.domain.EmailAuth;
import com.jyp.justplan.domain.user.domain.EmailAuthRepository;
import com.jyp.justplan.domain.user.domain.User;
import com.jyp.justplan.domain.user.domain.UserRepository;
import com.jyp.justplan.domain.user.dto.request.*;
import com.jyp.justplan.domain.user.dto.response.UserResponse;
import com.jyp.justplan.domain.user.dto.response.UserSignInResponseInfo;
import com.jyp.justplan.domain.user.exception.UserException;
import com.jyp.justplan.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    /* 회원가입 */
    public UserResponse saveUser(UserSignUpRequest userSignUpRequest) {
        System.out.println("userSignUpRequest = " + userSignUpRequest);

        EmailAuth emailAuth = getEmailAuth(userSignUpRequest.getEmail(), userSignUpRequest.getAuthId());

        User user = new User(
                userSignUpRequest.getEmail(),
                userSignUpRequest.getName(),
                passwordEncoder.encode(userSignUpRequest.getPassword())
        );

        User savedUser = userRepository.save(user);
        emailAuth.setUser(savedUser);

        return UserResponse.toDto(savedUser);
    }

    private EmailAuth getEmailAuth(String email, long authId) {
        EmailAuth emailAuth = emailAuthRepository.findById(authId)
                .orElseThrow(() -> new UserException("해당 이메일 인증이 존재하지 않습니다."));

        if (!emailAuth.getEmail().equals(email)) {
            throw new UserException("이메일 인증이 일치하지 않습니다.");
        } else if (!emailAuth.isEmailVerified()) {
            throw new UserException("이메일 인증이 완료되지 않았습니다.");
        } else if (emailAuth.getUser() != null) {
            throw new UserException("이미 사용된 인증입니다.");
        }

        return emailAuth;
    }

    /* 로그인 */
    public UserSignInResponseInfo signin(UserSignInRequest userSignInRequest) {
        User user = userRepository.findByEmail(userSignInRequest.getEmail())
                .orElseThrow(() -> new UserException("존재하지 않는 이메일입니다."));

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userSignInRequest.getEmail(),
                            userSignInRequest.getPassword()
                    )
            );

            String accessToken = jwtTokenProvider.createAccessToken(authentication);
            String refreshToken = jwtTokenProvider.createRefreshToken(authentication);

            return UserSignInResponseInfo.toInfo(
                    user.getId(),
                    user.getEmail(),
                    user.getName(),
                    accessToken,
                    refreshToken
            );
        } catch (BadCredentialsException e) {
            throw new UserException("비밀번호가 일치하지 않습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserException("로그인에 실패하였습니다.");
        }
    }

    /* 회원정보 수정 */
    public UserResponse updateUser(UserUpdateInfoRequest userUpdateInfoRequest, UserDetailsImpl userDetails) {
        // TODO: Exception 처리
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserException("해당 유저가 존재하지 않습니다."));
        user.updateName(userUpdateInfoRequest.getName());

        return UserResponse.toDto(user);
    }

    /* 비밀번호 재설정 */
    public void resetPassword(UserUpdatePasswordRequest userUpdatePasswordRequest) {
        User user = userRepository.findByEmail(userUpdatePasswordRequest.getEmail())
                .orElseThrow(() -> new UserException("해당 유저가 존재하지 않습니다."));

        if("kakao".equals(user.getProvider())){
            throw new UserException();
        }

        EmailAuth emailAuth = getEmailAuth(userUpdatePasswordRequest.getEmail(), userUpdatePasswordRequest.getAuthId());
        emailAuth.setUser(user);

        user.updatePassword(passwordEncoder.encode(userUpdatePasswordRequest.getPassword()));
    }

    /* 회원 탈퇴 */
    public void deleteUser(UserDeleteRequest userDeleteRequest, UserDetailsImpl userDetails) {
        // TODO: Exception 처리
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UserException("해당 유저가 존재하지 않습니다."));

        if (!passwordEncoder.matches(userDeleteRequest.getPassword(), user.getPassword())) {
            throw new UserException("비밀번호가 일치하지 않습니다.");
        }

        userRepository.delete(user);
    }

    /* Token 재발급 */
    public UserSignInResponseInfo reissueToken (String refreshToken) {
        jwtTokenProvider.validateToken(refreshToken);

        Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);

        System.out.println("refreshToken = " + refreshToken);
        String redisRefreshToken = redisTemplate.opsForValue().get(authentication.getName());
        if (!refreshToken.equals(redisRefreshToken)) {
            throw new UserException("유효하지 않은 Refresh Token 입니다.");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        UserSignInResponseInfo userSignInResponseInfo = UserSignInResponseInfo.toInfo(
                userDetails.getUserId(),
                userDetails.getUsername(),
                userDetails.getName(),
                jwtTokenProvider.createAccessToken(authentication),
                jwtTokenProvider.createRefreshToken(authentication)
        );

        return userSignInResponseInfo;
    }
}
