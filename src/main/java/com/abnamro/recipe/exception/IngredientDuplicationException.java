package com.abnamro.recipe.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception class for Duplicate Ingredient
 */
public class IngredientDuplicationException extends RuntimeException implements CustomRecipeException {

    private HttpStatus status = HttpStatus.NOT_FOUND;

    public IngredientDuplicationException() {

        super();
    }

    public IngredientDuplicationException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public IngredientDuplicationException(String message) {

        super(message);
    }

    public IngredientDuplicationException(String message, Throwable cause) {

        super(message, cause);
    }

    public IngredientDuplicationException(Throwable cause) {

        super(cause);
    }

    protected IngredientDuplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public HttpStatus getStatus() {

        return status;
    }
}

