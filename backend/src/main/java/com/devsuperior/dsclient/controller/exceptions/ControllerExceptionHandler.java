package com.devsuperior.dsclient.controller.exceptions;

import com.devsuperior.dsclient.service.exceptions.ClientNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@ControllerAdvice
public class ControllerExceptionHandler {


    @ExceptionHandler(ClientNotFoundException.class)
    public ResponseEntity<StandardError> clientNotFound(ClientNotFoundException exception, HttpServletRequest request) {
        StandardError standardError = new StandardError();
        standardError.setTimestamp(LocalDateTime.now());
        standardError.setStatus(HttpStatus.NOT_FOUND.value());
        standardError.setError("Id not found");
        standardError.setMessage("Client not found");
        standardError.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(standardError);
    }

}
