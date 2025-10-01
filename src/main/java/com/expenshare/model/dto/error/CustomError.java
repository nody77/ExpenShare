package com.expenshare.model.dto.error;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class CustomError {

    private final String message;
    private final String errorCode;
    private final String details;


    public CustomError(String message, String errorCode, String details) {
        this.message = message;
        this.errorCode = errorCode;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getDetails() {
        return details;
    }
}
