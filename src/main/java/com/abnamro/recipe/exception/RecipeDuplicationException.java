package com.abnamro.recipe.exception;

import org.springframework.http.HttpStatus;

public class RecipeDuplicationException extends RuntimeException implements CustomRecipeException {

    private HttpStatus status = HttpStatus.NOT_FOUND;

    public RecipeDuplicationException() {
        super();
    }

    public RecipeDuplicationException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public RecipeDuplicationException(String message) {
        super(message);
    }

    public RecipeDuplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public RecipeDuplicationException(Throwable cause) {
        super(cause);
    }

    protected RecipeDuplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public HttpStatus getStatus() {
        return status;
    }
}


