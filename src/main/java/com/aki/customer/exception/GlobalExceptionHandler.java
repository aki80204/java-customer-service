package com.aki.customer.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleAllExceptions(Exception ex) {
    Map<String, Object> errors = new HashMap<>();
    HttpStatusCode status = HttpStatus.INTERNAL_SERVER_ERROR;

    if (ex instanceof MethodArgumentNotValidException target) {
      status = HttpStatus.BAD_REQUEST;
      Map<String, String> fieldErrors = new HashMap<>();
      target
          .getBindingResult()
          .getFieldErrors()
          .forEach(e -> fieldErrors.put(e.getField(), e.getDefaultMessage()));
      errors.put("errors", fieldErrors);
      errors.put("message", "入力値に不備があります");

    } else if (ex instanceof DataIntegrityViolationException) {
      status = HttpStatus.CONFLICT;
      errors.put("error", "データ整合性エラーが発生しました");

    } else if (ex instanceof ResponseStatusException target) {
      status = target.getStatusCode();
      errors.put("error", target.getReason());

    } else {
      errors.put("error", "予期せぬエラーが発生しました");
      ex.printStackTrace();
    }

    return ResponseEntity.status(status).body(errors);
  }
}
