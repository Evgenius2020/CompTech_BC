package ru.comptech.bc.server.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ControllerExceptionHandler {

  @ExceptionHandler(RestException.class)
  public ResponseEntity<?> errorHandler(RestException e) {
    return new ResponseEntity<>(Map.of("error", e.getMessage()), e.getStatus());
  }
}
