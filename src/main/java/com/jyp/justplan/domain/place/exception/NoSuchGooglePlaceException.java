package com.jyp.justplan.domain.place.exception;

import com.jyp.justplan.api.exception.BusinessException;
import com.jyp.justplan.api.response.ResponseCode;
import lombok.Getter;

@Getter
public class NoSuchGooglePlaceException extends BusinessException {

    private static final ResponseCode responseCode = ResponseCode.RESOURCE_NOT_FOUND;
    private static final String defaultMessage = "존재하지 않는 구글장소입니다.";

    public NoSuchGooglePlaceException() {
        super(responseCode, defaultMessage);
    }

    public NoSuchGooglePlaceException(String customMessage) {
        super(responseCode, customMessage);
    }
}
