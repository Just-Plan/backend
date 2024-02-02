package com.jyp.justplan.domain.mbti.exception;

import com.jyp.justplan.api.exception.BusinessException;
import com.jyp.justplan.api.response.ResponseCode;

public class MbtiNotFoundException extends BusinessException {
    public MbtiNotFoundException(String customMessage) {
        super(ResponseCode.NOT_FOUND, customMessage);
    }

}
