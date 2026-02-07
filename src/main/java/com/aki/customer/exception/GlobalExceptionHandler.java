package com.aki.customer.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
    Map<String, String> errors = new HashMap<>();
    HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

    if (ex instanceof MethodArgumentNotValidException target) {
      // バリデーションエラー
      status = HttpStatus.BAD_REQUEST;
      target
          .getBindingResult()
          .getFieldErrors()
          .forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));
    } else if (ex instanceof DataIntegrityViolationException) {
      // DB制約違反（重複など）
      status = HttpStatus.CONFLICT;
      errors.put("error", "データ整合性エラーが発生しました");
    } else {
      // その他（未知のエラー）
      errors.put("error", "予期せぬエラーが発生しました");
    }

    return ResponseEntity.status(status).body(errors);
  }
}
