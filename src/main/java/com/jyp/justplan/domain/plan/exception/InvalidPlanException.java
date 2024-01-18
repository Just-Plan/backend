package com.jyp.justplan.domain.plan.exception;

import com.jyp.justplan.api.exception.BusinessException;
import com.jyp.justplan.api.response.ResponseCode;

public class InvalidPlanException extends BusinessException {
    public InvalidPlanException(ResponseCode responseCode) {
        super(responseCode);
    }
    public InvalidPlanException(ResponseCode responseCode, String customMessage) {
        super(responseCode, customMessage);
    }

    public InvalidPlanException(String customMessage) {
        super(ResponseCode.BAD_ERROR, customMessage);
    }
}
