package com.abnamro.recipe.exception;


import com.abnamro.recipe.config.RecipeErrorCodeConfig;
import com.abnamro.recipe.config.RecipeValidationMessageConfig;
import com.abnamro.recipe.model.error.ErrorSeverityLevelCodeType;
import com.abnamro.recipe.model.error.RecipeErrorModel;
import com.abnamro.recipe.model.error.RecipeRequestErrorModel;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
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


import java.util.HashMap;
import java.util.Map;

/**
 * This is the custom exception handler class
 */
@ControllerAdvice
@RestController
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }

        return new ResponseEntity(body, headers, status);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        final RecipeRequestErrorModel error = new RecipeRequestErrorModel(errors, RecipeErrorCodeConfig.INVALID_INPUT, ErrorSeverityLevelCodeType.ERROR);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(RecipeNotFoundException.class)
    @ResponseBody
    public ResponseEntity<Object> handleNotFoundException(RecipeNotFoundException ex) {
        HttpStatus status = ex.getStatus() == null ? HttpStatus.NOT_FOUND : ex.getStatus();
        final RecipeErrorModel error = new RecipeErrorModel(ex.getMessage(), RecipeErrorCodeConfig.INVALID_INPUT, ErrorSeverityLevelCodeType.ERROR);
        return new ResponseEntity<>(error, status);
    }
    @ExceptionHandler(UserException.class)
    @ResponseBody
    public ResponseEntity<Object> handleUserException(UserException ex) {
        HttpStatus status = ex.getStatus() == null ? HttpStatus.BAD_REQUEST : ex.getStatus();
        final RecipeErrorModel error = new RecipeErrorModel(ex.getMessage(), RecipeErrorCodeConfig.INVALID_INPUT, ErrorSeverityLevelCodeType.ERROR);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(RecipeDuplicationException.class)
    @ResponseBody
    public ResponseEntity<Object> handleNotFoundException(RecipeDuplicationException ex) {
        HttpStatus status = ex.getStatus() == null ? HttpStatus.NOT_FOUND : ex.getStatus();
        final RecipeErrorModel error = new RecipeErrorModel(ex.getMessage(), RecipeErrorCodeConfig.INVALID_INPUT, ErrorSeverityLevelCodeType.ERROR);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler(IngredientDuplicationException.class)
    @ResponseBody
    public ResponseEntity<Object> handleNotFoundException(IngredientDuplicationException ex) {
        HttpStatus status = ex.getStatus() == null ? HttpStatus.NOT_FOUND : ex.getStatus();
        final RecipeErrorModel error = new RecipeErrorModel(ex.getMessage(), RecipeErrorCodeConfig.INVALID_INPUT, ErrorSeverityLevelCodeType.ERROR);
        return new ResponseEntity<>(error, status);
    }

    @ExceptionHandler({IllegalArgumentException.class, InvalidDataAccessApiUsageException.class})
    @ResponseBody
    public ResponseEntity<Object> handleArgumentException(Exception ex) {
        final RecipeErrorModel error = new RecipeErrorModel(ex.getMessage(), RecipeErrorCodeConfig.INVALID_INPUT, ErrorSeverityLevelCodeType.ERROR);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseBody
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        String message = ex.getMessage();
        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException) {
            message = RecipeValidationMessageConfig.DB_CONSTRAINT_VIOLATED;
        }

        final RecipeErrorModel error = new RecipeErrorModel(message, RecipeErrorCodeConfig.DB_ERROR, ErrorSeverityLevelCodeType.ERROR);
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public final ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        final RecipeErrorModel error = new RecipeErrorModel(ex.getMessage(), RecipeErrorCodeConfig.DB_ERROR, ErrorSeverityLevelCodeType.ERROR);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
    public ResponseEntity<Object> handleRuntimeException(RuntimeException ex) {
        ex.printStackTrace();
        final RecipeErrorModel error = new RecipeErrorModel(ex.getMessage(), RecipeErrorCodeConfig.INTERNAL_ERROR, ErrorSeverityLevelCodeType.ERROR);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity<Object> handleGlobalException(Exception ex) {
        final RecipeErrorModel error = new RecipeErrorModel(ex.getMessage(), RecipeErrorCodeConfig.GLOBAL_ERROR, ErrorSeverityLevelCodeType.ERROR);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


}
