package com.jyp.justplan.domain.memo.exception;

import com.jyp.justplan.api.exception.BusinessException;
import com.jyp.justplan.api.response.ResponseCode;
import lombok.Getter;

@Getter
public class NoSuchMemoException extends BusinessException {

    private static final ResponseCode responseCode = ResponseCode.RESOURCE_NOT_FOUND;
    private static final String defaultMessage = "존재하지 않는 메모입니다.";

    public NoSuchMemoException() {
        super(responseCode, defaultMessage);
    }

    public NoSuchMemoException(String customMessage) {
        super(responseCode, customMessage);
    }
}
