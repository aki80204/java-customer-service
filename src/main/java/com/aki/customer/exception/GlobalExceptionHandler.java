package com.aki.customer.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {

    // AWSのログに強制的に書き出す
    System.out.println("DEBUG: GlobalExceptionHandler caught MethodArgumentNotValidException!");

    Map<String, String> errors = new HashMap<>();
    try {
      ex.getBindingResult()
          .getFieldErrors()
          .forEach(
              error -> {
                System.out.println(
                    "DEBUG: Field error - " + error.getField() + ": " + error.getDefaultMessage());
                errors.put(error.getField(), error.getDefaultMessage());
              });
    } catch (Exception e) {
      System.out.println("DEBUG: Error inside handler: " + e.getMessage());
    }

    return ResponseEntity.badRequest().body(errors);
  }
}
