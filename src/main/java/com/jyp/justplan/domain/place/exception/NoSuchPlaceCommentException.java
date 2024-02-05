package com.jyp.justplan.domain.place.exception;

import com.jyp.justplan.api.exception.BusinessException;
import com.jyp.justplan.api.response.ResponseCode;

public class NoSuchPlaceCommentException extends BusinessException {
    public NoSuchPlaceCommentException(ResponseCode responseCode) {
        super(responseCode);
    }

    public NoSuchPlaceCommentException(ResponseCode responseCode, String customMessage) {
        super(responseCode, customMessage);
    }

    public NoSuchPlaceCommentException(String customMessage) {
        super(ResponseCode.NOT_FOUND, customMessage);
    }
}
