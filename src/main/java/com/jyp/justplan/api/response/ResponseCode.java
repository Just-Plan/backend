package com.jyp.justplan.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseCode {
    SUCCESS(2000, "success"),
    BAD_ERROR(4000, "bad error"),
    VALIDATION_ERROR(4001, "Validation Error"),
    TYPE_MISMATCH_ERROR(4002, "Type Mismatch Error"),
    METHOD_NOT_ALLOWED(4003, "Method Not Allowed"),
    ACCESS_DENIED(4004, "Access Denied"),
    INTERNAL_SERVER_ERROR(5000, "Internal Server Error"),
    RESOURCE_NOT_FOUND(5001, "Resource Not Found"),
    DATA_INTEGRITY_VIOLATION(5002, "Data Integrity Violation");

    private final int code;
    private final String message;
}