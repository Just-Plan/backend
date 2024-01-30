package com.jyp.justplan.domain.city.exception;

import com.jyp.justplan.api.exception.BusinessException;
import com.jyp.justplan.api.response.ResponseCode;

public class CitySearchException extends BusinessException {
    public CitySearchException(String customMessage) {
        super(ResponseCode.NOT_FOUND, customMessage);
    }
}
