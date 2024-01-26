package com.jyp.justplan.domain.plan.exception.expense;

import com.jyp.justplan.api.exception.BusinessException;
import com.jyp.justplan.api.response.ResponseCode;

public class ExpenseAlreadyExistsException extends BusinessException {
    public ExpenseAlreadyExistsException(ResponseCode responseCode) {
        super(responseCode);
    }

    public ExpenseAlreadyExistsException(ResponseCode responseCode, String customMessage) {
        super(responseCode, customMessage);
    }

    public ExpenseAlreadyExistsException(String customMessage) {
        super(ResponseCode.BAD_ERROR, customMessage);
    }
}
