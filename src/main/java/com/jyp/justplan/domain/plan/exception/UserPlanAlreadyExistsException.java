package com.jyp.justplan.domain.plan.exception;

import com.jyp.justplan.api.exception.BusinessException;
import com.jyp.justplan.api.response.ResponseCode;

public class UserPlanAlreadyExistsException extends BusinessException {
    public UserPlanAlreadyExistsException(ResponseCode responseCode) {
        super(responseCode);
    }

    public UserPlanAlreadyExistsException(ResponseCode responseCode, String customMessage) {
        super(responseCode, customMessage);
    }

    public UserPlanAlreadyExistsException(String customMessage) {
        super(ResponseCode.BAD_ERROR, customMessage);
    }
}
