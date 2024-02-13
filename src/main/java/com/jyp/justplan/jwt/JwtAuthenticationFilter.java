package com.jyp.justplan.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jyp.justplan.api.response.ApiResponseDto;
import com.jyp.justplan.api.response.ResponseCode;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = jwtTokenProvider.resolveToken(request);
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (ExpiredJwtException e) {
            sendErrorResponse(response, ResponseCode.JWT_EXPIRED, "토큰 만료");
            return;
        }
        catch (JwtException e) {
            sendErrorResponse(response, ResponseCode.JWT_EXPIRED, "토큰 예외");
            return;
        } catch (RedisConnectionFailureException e) {
            SecurityContextHolder.clearContext();
            // TODO: Exception 처리
            throw new RuntimeException("Redis Connection Failure");
        } catch (Exception e) {
            throw new RuntimeException("JwtAuthenticationFilter.doFilterInternal: " + e.getMessage());
        }
        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, ResponseCode responseCode, String message) throws IOException {
        ApiResponseDto<?> responseDto = ApiResponseDto.exceptionResponse(responseCode, message);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        new ObjectMapper().writeValue(response.getWriter(), responseDto);
    }
}