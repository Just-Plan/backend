package com.jyp.justplan.domain.user.exception;

import com.jyp.justplan.api.exception.BusinessException;
import com.jyp.justplan.api.response.ResponseCode;

public class UserNotFoundException extends BusinessException {
    public UserNotFoundException(String customMessage) {
        super(ResponseCode.NOT_FOUND, customMessage);
    }

}
