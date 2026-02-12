package com.aki.customer.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import com.aki.customer.dto.CustomerAccountRequest;

@ExtendWith(MockitoExtension.class)
public class GlobalExceptionHandlerTest {

  @InjectMocks private GlobalExceptionHandler globalExceptionHandler;

  @Test
  @DisplayName("MethodArgumentNotValidException")
  public void methodArgumentNotValidExceptionTest() {
    CustomerAccountRequest request = new CustomerAccountRequest();
    BeanPropertyBindingResult bindingResult =
        new BeanPropertyBindingResult(request, "customerAccountRequest");
    bindingResult.addError(
        new FieldError("customerAccountRequest", "email", "メールアドレスの形式が正しくありません"));
    bindingResult.addError(new FieldError("customerAccountRequest", "name", "名前は必須です"));
    Exception exception = new MethodArgumentNotValidException(null, bindingResult);
    ResponseEntity<Map<String, Object>> response =
        globalExceptionHandler.handleAllExceptions(exception);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("入力値に不備があります", response.getBody().get("message"));
    Map<String, String> errors = (Map<String, String>) response.getBody().get("errors");
    assertEquals("メールアドレスの形式が正しくありません", errors.get("email"));
    assertEquals("名前は必須です", errors.get("name"));
  }

  @Test
  @DisplayName("DataIntegrityViolationException")
  public void dataIntegrityViolationExceptionTest() {

    Exception exception = new DataIntegrityViolationException("制約違反です。");
    ResponseEntity<Map<String, Object>> response =
        globalExceptionHandler.handleAllExceptions(exception);
    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    assertEquals("データ整合性エラーが発生しました", response.getBody().get("error"));
  }

  @Test
  @DisplayName("ResponseStatusException")
  public void responseStatusExceptionTest() {

    Exception exception =
        new ResponseStatusException(HttpStatus.NOT_FOUND, "CustomerAccount not found. id=1");
    ResponseEntity<Map<String, Object>> response =
        globalExceptionHandler.handleAllExceptions(exception);
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("CustomerAccount not found. id=1", response.getBody().get("error"));
  }

  @Test
  @DisplayName("未定義のエラー")
  public void undefinedException() {
    Exception exception = new NullPointerException("NPE");
    ResponseEntity<Map<String, Object>> response =
        globalExceptionHandler.handleAllExceptions(exception);
    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("予期せぬエラーが発生しました", response.getBody().get("error"));
  }
}
