package com.jyp.justplan.domain.user.exception;

import com.jyp.justplan.api.exception.BusinessException;
import com.jyp.justplan.api.response.ResponseCode;

public class UserException extends BusinessException {
    private static final ResponseCode responseCode = ResponseCode.RESOURCE_NOT_FOUND;
    private static final String defaultMessage = "카카오계정은 비밀번호 변경이 불가합니다.";

    public UserException() {
        super(responseCode, defaultMessage);
    }

    public UserException(String customMessage) {
        super(responseCode, customMessage);
    }
}
