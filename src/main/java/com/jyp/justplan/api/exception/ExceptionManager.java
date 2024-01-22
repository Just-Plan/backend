package com.jyp.justplan.api.exception;

import com.amazonaws.services.kms.model.NotFoundException;
import com.jyp.justplan.api.response.ApiResponseDto;
import com.jyp.justplan.api.response.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class ExceptionManager {
    @ExceptionHandler(RuntimeException.class)
    public ApiResponseDto<?> handleRuntimeException(RuntimeException e) {
        log.info("RuntimeException : {}",e.getMessage());
        return ApiResponseDto.exceptionResponse(ResponseCode.BAD_ERROR, e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public ApiResponseDto<?> handleBusinessException(BusinessException e) {
        log.info("BusinessException :{}",e.getMessage());
        String message = e.getCustomMessage() != null ? e.getCustomMessage() : e.getResponseCode().getMessage();
        return ApiResponseDto.exceptionResponse(e.getResponseCode(), message);
    }

    // MethodArgumentNotValidException 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponseDto<?> handleValidationExceptions(MethodArgumentNotValidException e) {
        log.info("MethodArgumentNotValidException Validation error: {}", e.getMessage());
        return ApiResponseDto.failResponse(e.getBindingResult());
    }

    @ExceptionHandler(BindException.class)
    public ApiResponseDto<?> handleBindException(BindException e) {
        log.info("BindException: {}", e.getMessage());
        return ApiResponseDto.failResponse(e.getBindingResult());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponseDto<?> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.info("MethodArgumentTypeMismatchException: {}", e.getMessage());
        return ApiResponseDto.exceptionResponse(ResponseCode.BAD_ERROR, "Type Mismatch: " + e.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ApiResponseDto<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.info("HttpRequestMethodNotSupportedException: {}", e.getMessage());
        return ApiResponseDto.exceptionResponse(ResponseCode.METHOD_NOT_ALLOWED, "Method Not Allowed: " + e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ApiResponseDto<?> handleAccessDeniedException(NotFoundException e) {
        log.info("AccessDeniedException: {}", e.getMessage());
        return ApiResponseDto.exceptionResponse(ResponseCode.NOT_FOUND, "Not Found: " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponseDto<?> handleException(Exception e) {
        log.error("Exception: {}", e.getMessage());
        return ApiResponseDto.exceptionResponse(ResponseCode.INTERNAL_SERVER_ERROR, "Internal Server Error: " + e.getMessage());
    }
}
