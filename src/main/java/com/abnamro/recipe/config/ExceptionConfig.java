package com.abnamro.recipe.config;


import com.abnamro.recipe.exception.IngredientDuplicationException;
import com.abnamro.recipe.exception.RecipeNotFoundException;
import com.abnamro.recipe.model.error.ErrorSeverityLevelCodeType;
import com.abnamro.recipe.model.error.RecipeErrorModel;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestController
public class ExceptionConfig extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        return new ResponseEntity(body, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        final RecipeErrorModel error = new RecipeErrorModel(errors, RecipeErrorConstants.INVALID_INPUT, ErrorSeverityLevelCodeType.ERROR);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(RecipeNotFoundException.class)
    @ResponseBody
    public ResponseEntity<Object> handleNotFoundException(RecipeNotFoundException ex) {
        HttpStatus status = ex.getStatus() == null ? HttpStatus.NOT_FOUND : ex.getStatus();
        final RecipeErrorModel error = new RecipeErrorModel(ex.getMessage(), RecipeErrorConstants.INVALID_INPUT, ErrorSeverityLevelCodeType.ERROR);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(IngredientDuplicationException.class)
    @ResponseBody
    public ResponseEntity<Object> handleNotFoundException(IngredientDuplicationException ex) {
        HttpStatus status = ex.getStatus() == null ? HttpStatus.NOT_FOUND : ex.getStatus();
        final RecipeErrorModel error = new RecipeErrorModel(ex.getMessage(), RecipeErrorConstants.INVALID_INPUT, ErrorSeverityLevelCodeType.ERROR);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler({
            IllegalArgumentException.class,
            InvalidDataAccessApiUsageException.class
    })
    @ResponseBody
    public ResponseEntity<Object> handleArgumentException(Exception ex) {
        final RecipeErrorModel error = new RecipeErrorModel(ex.getMessage(), RecipeErrorConstants.INVALID_INPUT, ErrorSeverityLevelCodeType.ERROR);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message=ex.getMessage();
        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
             message = "";
        }

        final RecipeErrorModel error = new RecipeErrorModel(message, RecipeErrorConstants.DB_ERROR, ErrorSeverityLevelCodeType.ERROR);
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }



    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public final ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        final RecipeErrorModel error = new RecipeErrorModel(ex.getMessage(), RecipeErrorConstants.DB_ERROR, ErrorSeverityLevelCodeType.ERROR);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        final RecipeErrorModel error = new RecipeErrorModel(ex.getMessage(), RecipeErrorConstants.INTERNAL_ERROR, ErrorSeverityLevelCodeType.ERROR);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Object> handleGlobalException(Exception ex) {
        ex.printStackTrace();
        final RecipeErrorModel error = new RecipeErrorModel(ex.getMessage(), RecipeErrorConstants.GLOBAL_ERROR, ErrorSeverityLevelCodeType.ERROR);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


}
