package com.jyp.justplan.domain.plan.exception.tag;

import com.jyp.justplan.api.exception.BusinessException;
import com.jyp.justplan.api.response.ResponseCode;

public class NoSuchTagException extends BusinessException {
    public NoSuchTagException(ResponseCode responseCode) {
        super(responseCode);
    }

    public NoSuchTagException(ResponseCode responseCode, String customMessage) {
        super(responseCode, customMessage);
    }

    public NoSuchTagException(String customMessage) {
        super(ResponseCode.NOT_FOUND, customMessage);
    }
}
