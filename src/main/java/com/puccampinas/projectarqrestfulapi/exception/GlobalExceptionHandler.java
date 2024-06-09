package com.puccampinas.projectarqrestfulapi.exception;


import com.puccampinas.projectarqrestfulapi.dtos.api.ErrorMessage;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<Object> handleNotFoundException(ChangeSetPersister.NotFoundException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                "Not found",
                request.getDescription(false));
        return new ResponseEntity<>(errorMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorMessage, HttpStatus.FORBIDDEN);
    }
}