package com.jyp.justplan.domain.plan.exception;

import com.jyp.justplan.api.exception.BusinessException;
import com.jyp.justplan.api.response.ResponseCode;

public class PlanValidationException extends BusinessException {
    public PlanValidationException(ResponseCode responseCode) {
        super(responseCode);
    }

    public PlanValidationException(ResponseCode responseCode, String customMessage) {
        super(responseCode, customMessage);
    }

    public PlanValidationException(String customMessage) {
        super(ResponseCode.VALIDATION_ERROR, customMessage);
    }
}
