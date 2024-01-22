package com.jyp.justplan.domain.plan.exception;

import com.jyp.justplan.api.exception.BusinessException;
import com.jyp.justplan.api.response.ResponseCode;

public class NoSuchPlanException extends BusinessException {
    public NoSuchPlanException(ResponseCode responseCode) {
        super(responseCode);
    }

    public NoSuchPlanException(ResponseCode responseCode, String customMessage) {
        super(responseCode, customMessage);
    }

    public NoSuchPlanException(String customMessage) {
        super(ResponseCode.NOT_FOUND, customMessage);
    }
}
