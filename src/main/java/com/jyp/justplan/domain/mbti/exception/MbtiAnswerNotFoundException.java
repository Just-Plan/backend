package com.jyp.justplan.domain.mbti.exception;

import com.jyp.justplan.api.exception.BusinessException;
import com.jyp.justplan.api.response.ResponseCode;

public class MbtiAnswerNotFoundException extends BusinessException {

    public MbtiAnswerNotFoundException(String customMessage) {
        super(ResponseCode.VALIDATION_ERROR, customMessage);
    }
}
