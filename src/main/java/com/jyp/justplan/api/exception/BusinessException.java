package com.jyp.justplan.api.exception;

import com.jyp.justplan.api.response.ResponseCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ResponseCode responseCode;
    private final String customMessage;

    public BusinessException(ResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
        this.customMessage = null;
    }

    public BusinessException(ResponseCode responseCode, String customMessage) {
        super(customMessage);
        this.responseCode = responseCode;
        this.customMessage = customMessage;
    }
}
