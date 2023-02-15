package com.abnamro.recipe.exception;

import org.springframework.http.HttpStatus;

public interface CustomRecipeException {
    HttpStatus getStatus();
}