package com.jyp.justplan.domain.plan.exception.expense;

import com.jyp.justplan.api.exception.BusinessException;
import com.jyp.justplan.api.response.ResponseCode;

public class NoSuchExpenseException extends BusinessException {
    public NoSuchExpenseException(ResponseCode responseCode) {
        super(responseCode);
    }

    public NoSuchExpenseException(ResponseCode responseCode, String customMessage) {
        super(responseCode, customMessage);
    }

    public NoSuchExpenseException(String customMessage) {
        super(ResponseCode.NOT_FOUND, customMessage);
    }
}