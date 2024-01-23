package com.jyp.justplan.api.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ApiResponseDto<T> {
    private int code;
    private T data;
    private String message;

    private ApiResponseDto(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }
    public static <T> ApiResponseDto<T> successResponse(T data) {
        return new ApiResponseDto<>(ResponseCode.SUCCESS.getCode(), data, null);
    }

    public static ApiResponseDto<?> successWithoutDataResponse() {
        return new ApiResponseDto<>(ResponseCode.SUCCESS.getCode(), null, null);
    }

    public static ApiResponseDto<?> errorResponse(ResponseCode code, Exception e) {
        return new ApiResponseDto<>(code.getCode(), null, e.getMessage());
    }

    public static ApiResponseDto<?> exceptionResponse(ResponseCode code, String message) {
        return new ApiResponseDto<>(code.getCode(), null, message);
    }

    public static ApiResponseDto<?> failResponse(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        bindingResult.getAllErrors().forEach(error -> {
            if (error instanceof FieldError) {
                FieldError fieldError = (FieldError) error;
                errors.put(fieldError.getField(), fieldError.getDefaultMessage());
            } else {
                errors.put(error.getObjectName(), error.getDefaultMessage());
            }
        });
        return new ApiResponseDto<>(ResponseCode.BAD_ERROR.getCode(), errors, ResponseCode.BAD_ERROR.getMessage());
    }
}