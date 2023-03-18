package com.abnamro.recipe.exception;

import org.springframework.http.HttpStatus;

public class UserException extends  RuntimeException implements CustomRecipeException {

    private HttpStatus status = HttpStatus.NOT_FOUND;

    public UserException(String message) {
    }

    @Override
    public HttpStatus getStatus() {
        return status;
    }

    public UserException(HttpStatus status) {
        this.status = status;
    }

    public UserException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public UserException(String message, Throwable cause, HttpStatus status) {
        super(message, cause);
        this.status = status;
    }

    public UserException(Throwable cause, HttpStatus status) {
        super(cause);
        this.status = status;
    }

    public UserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, HttpStatus status) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.status = status;
    }
}
