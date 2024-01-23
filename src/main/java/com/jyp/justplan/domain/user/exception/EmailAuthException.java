package com.jyp.justplan.domain.user.exception;

import com.jyp.justplan.api.exception.BusinessException;
import com.jyp.justplan.api.response.ResponseCode;

public class EmailAuthException extends BusinessException {
    private static final ResponseCode responseCode = ResponseCode.DATA_INTEGRITY_VIOLATION;
    private static final String defaultMessage = "이미 존재하는 이메일입니다.";

    public EmailAuthException() {
        super(responseCode, defaultMessage);
    }

    public EmailAuthException(String customMessage) {
        super(responseCode, customMessage);
    }
}
