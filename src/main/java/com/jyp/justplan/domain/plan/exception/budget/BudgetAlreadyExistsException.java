package com.jyp.justplan.domain.plan.exception.budget;

import com.jyp.justplan.api.exception.BusinessException;
import com.jyp.justplan.api.response.ResponseCode;

public class BudgetAlreadyExistsException extends BusinessException {
    public BudgetAlreadyExistsException(ResponseCode responseCode) {
        super(responseCode);
    }

    public BudgetAlreadyExistsException(ResponseCode responseCode, String customMessage) {
        super(responseCode, customMessage);
    }

    public BudgetAlreadyExistsException(String customMessage) {
        super(ResponseCode.BAD_ERROR, customMessage);
    }
}
