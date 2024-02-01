package com.jyp.justplan.domain.plan.exception.scrap;

import com.jyp.justplan.api.exception.BusinessException;
import com.jyp.justplan.api.response.ResponseCode;

public class NoSuchScrapException extends BusinessException {
    public NoSuchScrapException(ResponseCode responseCode) {
        super(responseCode);
    }

    public NoSuchScrapException(ResponseCode responseCode, String customMessage) {
        super(responseCode, customMessage);
    }

    public NoSuchScrapException(String customMessage) {
        super(ResponseCode.NOT_FOUND, customMessage);
    }
}