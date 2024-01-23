package com.jyp.justplan.domain.mbti.exception;

import com.jyp.justplan.api.exception.BusinessException;
import com.jyp.justplan.api.response.ResponseCode;

public class MbtiAnswerCountMismatchException extends BusinessException {

    public MbtiAnswerCountMismatchException(String customMessage) {
        super(ResponseCode.TYPE_MISMATCH_ERROR, customMessage);
    }
}
