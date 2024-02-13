package com.jyp.justplan.jwt;


import com.jyp.justplan.domain.user.application.UserDetailServiceImpl;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private final long accessTokenExpiration = 1000L * 60 * 60 * 2; // 2시간
    private final long refreshTokenExpiration = 1000L * 60 * 60 * 24 * 7; // 7일

    final private UserDetailServiceImpl userDetailService;

    /* Access Token 생성 */
    public String createAccessToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + accessTokenExpiration);

        String jwts = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return jwts;
    }

    /* Refresh Token 생성 */
    public String createRefreshToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshTokenExpiration);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        redisTemplate.opsForValue().set(
                authentication.getName(),
                refreshToken,
                refreshTokenExpiration,
                TimeUnit.MILLISECONDS
        );

        return refreshToken;
    }

    public Authentication getAuthentication(String token) {
        String userPrincipal = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody().getSubject();

        UserDetails userDetails = userDetailService.loadUserByUsername(userPrincipal);

        return new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities()
        );
    }

    /* HTTP Header에서 Bearer Token 가져오기 */
    public String resolveToken (HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /* Token 검증 */
    public boolean validateToken(String token) {
        // TODO: Exception 처리
        Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
        return true;
//        try {
//            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
//            return true;
//        } catch (ExpiredJwtException e) {
//            throw new ExpiredJwtException();
//        } catch (JwtException e) {
//            throw new JwtException();
//        }
    }
}
