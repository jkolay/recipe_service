package com.abnamro.recipe.exception;

import org.springframework.http.HttpStatus;

/**
 * Interface for Recipe Exception
 */
public interface CustomRecipeException {
    HttpStatus getStatus();
}