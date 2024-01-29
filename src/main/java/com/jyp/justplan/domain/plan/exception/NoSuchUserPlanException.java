package com.jyp.justplan.domain.plan.exception;

import com.jyp.justplan.api.exception.BusinessException;
import com.jyp.justplan.api.response.ResponseCode;

public class NoSuchUserPlanException extends BusinessException {
    public NoSuchUserPlanException(ResponseCode responseCode) {
        super(responseCode);
    }

    public NoSuchUserPlanException(ResponseCode responseCode, String customMessage) {
        super(responseCode, customMessage);
    }

    public NoSuchUserPlanException(String customMessage) {
        super(ResponseCode.NOT_FOUND, customMessage);
    }
}
