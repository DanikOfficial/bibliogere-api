package com.pete.bibliogere.controller;

import com.pete.bibliogere.api.ApiResponseObject;
import com.pete.bibliogere.modelo.excepcoes.*;
import com.pete.bibliogere.security.excepcoes.CredenciaisInvalidasException;
import com.pete.bibliogere.security.excepcoes.InvalidTokenException;
import com.pete.bibliogere.security.excepcoes.UtilizadorNotFoundException;
import com.pete.bibliogere.utils.CustomValidationException;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class RestControllerAdvisor extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,  // Changed from HttpStatus
            WebRequest request) {

        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String message = "Campo(os) inválidos!";

        Map<String, Object> res = new ApiResponseObject().buildValidationError(message, "fail", errors);

        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = CustomValidationException.class)
    public ResponseEntity<Object> customExceptionHandling(
            CustomValidationException ex,
            WebRequest request) {

        Map<String, String> errors = new LinkedHashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String message = "Campo(os) inválidos!";

        Map<String, Object> res = new ApiResponseObject().buildValidationError(message, "fail", errors);

        return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {
            EmprestimoNotFoundException.class,
            ItemEmprestimoNotFoundException.class,
            ObraNotFoundException.class,
            EstanteNotFoundException.class,
            LocalizacaoNotFoundException.class,
            PermissaoNotFoundException.class,
            TipoEstanteNotFoundException.class,
            UtilizadorNotFoundException.class,
            EstanteException.class,
            QuestoesSegurancaNotFoundException.class
    })
    public ResponseEntity<Object> handleNotFoundException(
            RuntimeException ex,
            WebRequest request) {

        Map<String, Object> res = new ApiResponseObject().buildSimpleError(ex.getMessage(), "fail");

        return new ResponseEntity<>(res, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {
            EmprestimoAlreadyExistsException.class,
            EstanteAlreadyExistsException.class,
            ItemEmprestimoAlreadyExistsException.class,
            LocalizacaoAlreadyExistsException.class,
            ObraAlreadyExistsException.class,
            TipoEstanteAlreadyExistsException.class,
            DuplicateQuestionException.class,
            UtilizadorAlreadyExistsException.class
    })
    public ResponseEntity<Object> handleAlreadyExistsException(
            RuntimeException ex,
            WebRequest request) {

        Map<String, Object> res = new ApiResponseObject().buildSimpleError(ex.getMessage(), "fail");

        return new ResponseEntity<>(res, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<Object> handleExpiredSessionException(
            RuntimeException ex,
            WebRequest request) {

        Map<String, Object> res = new ApiResponseObject().buildSimpleError(ex.getMessage(), "error", 401);

        return new ResponseEntity<>(res, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({
            CredenciaisInvalidasException.class,
            InvalidTokenException.class
    })
    public ResponseEntity<Object> handleInvalidException(
            RuntimeException ex,
            WebRequest request) {

        Map<String, Object> res = new ApiResponseObject().buildSimpleError(ex.getMessage(), "error");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(res, headers, HttpStatus.FORBIDDEN);
    }
}