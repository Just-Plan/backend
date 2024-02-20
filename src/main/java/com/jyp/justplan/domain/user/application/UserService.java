package com.jyp.justplan.domain.user.application;


import antlr.StringUtils;
import com.jyp.justplan.domain.mbti.domain.Mbti;
import com.jyp.justplan.domain.mbti.domain.MbtiTestRepository;
import com.jyp.justplan.domain.plan.domain.UserPlanRepository;
import com.jyp.justplan.domain.plan.domain.scrap.ScrapRepository;
import com.jyp.justplan.domain.s3.S3Service;
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
import io.netty.util.internal.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final MbtiTestRepository mbtiTestRepository;
    private final EmailAuthRepository emailAuthRepository;
    private final ScrapRepository scrapRepository;
    private final UserPlanRepository userPlanRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;
    private final S3Service s3Service;

    /* 회원가입 */
    public UserResponse saveUser(UserSignUpRequest userSignUpRequest) {
        System.out.println("userSignUpRequest = " + userSignUpRequest);

        EmailAuth emailAuth = getEmailAuth(userSignUpRequest.getEmail(), userSignUpRequest.getAuthId());

        User user = new User(
                userSignUpRequest.getEmail(),
                userSignUpRequest.getName(),
//                passwordEncoder.encode("{bcrypt}"+userSignUpRequest.getPassword())
                passwordEncoder.encode(userSignUpRequest.getPassword())
        );

        User savedUser = userRepository.save(user);
        emailAuth.setUser(savedUser);

        return UserResponse.toDto(savedUser);
    }

    /* 유저 조회 */
    public User findByEmail(String email) {
        return userRepository.findByEmailAndDeletedAtIsNull(email)
                .orElseThrow(() -> new UserException("해당 유저가 존재하지 않습니다."));
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
            User user = userRepository.findByEmailAndDeletedAtIsNull(userSignInRequest.getEmail())
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
                    refreshToken,
                    user.getMbti(),
                    user.getProfile(),
                    user.getIntroduction()
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
        User user = userRepository.findByEmailAndDeletedAtIsNull(userDetails.getUsername())
                .orElseThrow(() -> new UserException("해당 유저가 존재하지 않습니다."));

        long totalScrap = scrapRepository.countByUser(user);
        long totalUserPlan = userPlanRepository.countByUser(user);

        user.updateName(userUpdateInfoRequest.getName());
        user.updateIntroduction(userUpdateInfoRequest.getIntroduction());

        Mbti mbti = user.getMbti();

        if(!StringUtil.isNullOrEmpty(userUpdateInfoRequest.getMbtiName())) {
            mbti = mbtiTestRepository.findByMbti(userUpdateInfoRequest.getMbtiName())
                    .orElseThrow(() -> new UserException("요청하신 MBTI가 존재하지 않습니다."));

            user.updateMbti(mbti);
        }

        //return UserResponse.toDto(user);
        return UserResponse.toTotDto(user, totalScrap, totalUserPlan, mbti);
    }

    /*프로필 수정*/
    public UserResponse updateProfile(UserUpdateProfileRequest userUpdateProfileRequest, UserDetailsImpl userDetails){
        User user = userRepository.findByEmailAndDeletedAtIsNull(userDetails.getUsername())
                .orElseThrow(() -> new UserException("해당 유저가 존재하지 않습니다."));

        user.updateProfile(userUpdateProfileRequest.getProfile());
        user.updateBackground(userUpdateProfileRequest.getBackground());
        //user.updateIntroduction(userUpdateProfileRequest.getIntroduction());

        return UserResponse.toDto(user);
    }


    /* 비밀번호 재설정 */
    // TODO : 이메일 인증없이 비밀번호 변경 진행
    public void resetPassword(UserUpdatePasswordRequest userUpdatePasswordRequest) {
        User user = userRepository.findByEmailAndDeletedAtIsNull(userUpdatePasswordRequest.getEmail())
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
        User user = userRepository.findByEmailAndDeletedAtIsNull(userDetails.getUsername())
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

    public UserResponse readUser(UserDetailsImpl userDetails) {

        log.info("user id : " + userDetails.getUserId());
        User user = userRepository.findByEmailAndDeletedAtIsNull(userDetails.getUsername())
                    .orElseThrow(() -> new UserException("해당 유저가 존재하지 않습니다."));

        long totalScrap = scrapRepository.countByUser(user);
        long totalUserPlan = userPlanRepository.countByUser(user);

        return UserResponse.toTotDto(user, totalScrap, totalUserPlan, user.getMbti());
    }

    /*프로필 사진 업로드*/
    public String uploadProfile(ProfileUploadRequest profileUploadRequest) {
        String uri = "";
        try {
            uri = s3Service.uploadProfilePicture(profileUploadRequest.getEmail(), profileUploadRequest.getFile());
        } catch (Exception e) {
            throw new UserException("이미지 업로드 중 오류가 발생하였습니다.");
        }

        User user = userRepository.findByEmailAndDeletedAtIsNull(profileUploadRequest.getEmail())
                .orElseThrow(() -> new UserException("해당 유저가 존재하지 않습니다."));

        user.updateProfile(uri);

        return uri;

    }

    /*배경사진 업로드*/
    public String uploadBackground(ProfileUploadRequest profileUploadRequest) {
        String uri = "";
        try {
            uri = s3Service.uploadBackgroundPicture(profileUploadRequest.getEmail(), profileUploadRequest.getFile());
        } catch (Exception e) {
            throw new UserException("이미지 업로드 중 오류가 발생하였습니다.");
        }

        User user = userRepository.findByEmailAndDeletedAtIsNull(profileUploadRequest.getEmail())
                .orElseThrow(() -> new UserException("해당 유저가 존재하지 않습니다."));

        user.updateBackground(uri);

        return uri;

    }
}
