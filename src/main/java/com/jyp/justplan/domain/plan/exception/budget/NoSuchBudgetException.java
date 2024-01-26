package com.jyp.justplan.domain.plan.exception.budget;

import com.jyp.justplan.api.exception.BusinessException;
import com.jyp.justplan.api.response.ResponseCode;

public class NoSuchBudgetException extends BusinessException {
    public NoSuchBudgetException(ResponseCode responseCode) {
        super(responseCode);
    }

    public NoSuchBudgetException(ResponseCode responseCode, String customMessage) {
        super(responseCode, customMessage);
    }

    public NoSuchBudgetException(String customMessage) {
        super(ResponseCode.NOT_FOUND, customMessage);
    }
}
