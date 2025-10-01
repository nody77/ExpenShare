package com.expenshare.exception.handler;

import com.expenshare.exception.ConflictException;
import com.expenshare.model.dto.error.CustomError;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import jakarta.inject.Singleton;

@Singleton
@Produces
public class ConflictExceptionHandler implements ExceptionHandler<ConflictException, HttpResponse<CustomError>>{
    @Override
    public HttpResponse<CustomError> handle(HttpRequest request, ConflictException exception) {
        CustomError customError = new CustomError(exception.getMessage(), "CONFLICT", "");

        return HttpResponse.status(HttpStatus.CONFLICT).body(customError);
    }
}

