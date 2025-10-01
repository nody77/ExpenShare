package com.expenshare.exception.handler;

import com.expenshare.exception.ValidationException;
import com.expenshare.model.dto.error.CustomError;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Singleton
@Produces
public class ValidationExceptionHandler implements ExceptionHandler<ValidationException, HttpResponse<CustomError>>{

    @Override
    public HttpResponse<CustomError> handle(HttpRequest request, ValidationException exception) {
        CustomError customError = new CustomError(exception.getMessage(), "VALIDATION_ERROR", "");

        return HttpResponse.status(HttpStatus.BAD_REQUEST).body(customError);
    }
}