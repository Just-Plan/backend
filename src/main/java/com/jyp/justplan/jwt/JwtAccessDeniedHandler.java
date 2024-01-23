package com.jyp.justplan.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.jyp.justplan.api.response.ApiResponseDto;
import com.jyp.justplan.api.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
            log.info("error url : " + request.getPathInfo());

            ApiResponseDto<?> apiResponseDto = ApiResponseDto.errorResponse(ResponseCode.VALIDATION_ERROR,accessDeniedException);

            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(apiResponseDto));
    }
}