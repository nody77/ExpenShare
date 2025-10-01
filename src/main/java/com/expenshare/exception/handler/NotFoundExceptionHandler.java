package com.expenshare.exception.handler;

import com.expenshare.exception.NotFoundException;
import com.expenshare.model.dto.error.CustomError;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.server.exceptions.ExceptionHandler;

import jakarta.inject.Singleton;

@Singleton
@Produces
public class NotFoundExceptionHandler implements ExceptionHandler<NotFoundException, HttpResponse<CustomError>> {

    @Override
    public HttpResponse<CustomError> handle(HttpRequest request, NotFoundException exception) {
        CustomError customError = new CustomError(exception.getMessage(), "NOT_FOUND", "");

        return HttpResponse.status(HttpStatus.NOT_FOUND).body(customError);
    }
}
