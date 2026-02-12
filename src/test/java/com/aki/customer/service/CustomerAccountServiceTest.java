package com.aki.customer.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.aki.customer.dto.CustomerAccountRequest;
import com.aki.customer.entity.CustomerAccount;
import com.aki.customer.repository.CustomerAccountRepository;

@SpringBootTest
public class CustomerAccountServiceTest {
  @Autowired private CustomerAccountService service;
  @Autowired private CustomerAccountRepository customerAccountRepository;

  @BeforeEach
  void setUp() {
    customerAccountRepository.deleteAllInBatch();
  }

  @Test
  @DisplayName("アカウント取得.正常")
  public void getAccountTest01() {
    CustomerAccount expected =
        CustomerAccount.builder().name("test").email("test@test.com").build();
    expected = customerAccountRepository.save(expected);

    CustomerAccount response = service.getAccount(expected.getId());
    assertEquals(expected.getId(), response.getId());
    assertEquals(expected.getName(), response.getName());
    assertEquals(expected.getEmail(), response.getEmail());
  }

  @Test
  @DisplayName("アカウント取得.異常.NotFound")
  public void getAccountTest02() {

    ResponseStatusException exception =
        assertThrows(
            ResponseStatusException.class,
            () -> {
              service.getAccount(9999999l);
            });

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertTrue(exception.getReason().contains("CustomerAccount not found. id=9999999"));
  }

  @Test
  @DisplayName("アカウント作成.正常")
  public void registerAccountTest01() {
    CustomerAccount expected =
        CustomerAccount.builder().name("test").email("test@test.com").build();

    CustomerAccountRequest request =
        CustomerAccountRequest.builder().name("test").email("test@test.com").build();

    CustomerAccount response = service.registerAccount(request);
    assertEquals(expected.getName(), response.getName());
    assertEquals(expected.getEmail(), response.getEmail());
  }

  @Test
  @DisplayName("アカウント更新.名前.正常")
  public void updateAccountTest01() {
    CustomerAccount entity = CustomerAccount.builder().name("test").email("test@test.com").build();
    entity = customerAccountRepository.save(entity);

    CustomerAccount expected =
        CustomerAccount.builder().id(entity.getId()).name("updated").email("test@test.com").build();

    CustomerAccountRequest request =
        CustomerAccountRequest.builder().name("updated").email("test@test.com").build();

    CustomerAccount response = service.updateAccount(request, entity.getId());
    assertEquals(expected.getId(), response.getId());
    assertEquals(expected.getName(), response.getName());
    assertEquals(expected.getEmail(), response.getEmail());
  }

  @Test
  @DisplayName("アカウント更新.メール.正常")
  public void updateAccountTest02() {
    CustomerAccount entity = CustomerAccount.builder().name("test").email("test@test.com").build();
    entity = customerAccountRepository.save(entity);

    CustomerAccount expected =
        CustomerAccount.builder().id(entity.getId()).name("test").email("updated@test.com").build();

    CustomerAccountRequest request =
        CustomerAccountRequest.builder().name("test").email("updated@test.com").build();

    CustomerAccount response = service.updateAccount(request, entity.getId());
    assertEquals(expected.getId(), response.getId());
    assertEquals(expected.getName(), response.getName());
    assertEquals(expected.getEmail(), response.getEmail());
  }

  @Test
  @DisplayName("アカウント更新.名前.メール.正常")
  public void updateAccountTest03() {
    CustomerAccount entity = CustomerAccount.builder().name("test").email("test@test.com").build();
    entity = customerAccountRepository.save(entity);

    CustomerAccount expected =
        CustomerAccount.builder()
            .id(entity.getId())
            .name("updated")
            .email("updated@test.com")
            .build();

    CustomerAccountRequest request =
        CustomerAccountRequest.builder().name("updated").email("updated@test.com").build();

    CustomerAccount response = service.updateAccount(request, entity.getId());
    assertEquals(expected.getId(), response.getId());
    assertEquals(expected.getName(), response.getName());
    assertEquals(expected.getEmail(), response.getEmail());
  }

  @Test
  @DisplayName("アカウント更新.異常.NotFound")
  public void updateAccountTest04() {
    CustomerAccount entity = CustomerAccount.builder().name("test").email("test@test.com").build();
    customerAccountRepository.save(entity);

    CustomerAccountRequest request =
        CustomerAccountRequest.builder().name("updated").email("updated@test.com").build();

    ResponseStatusException exception =
        assertThrows(
            ResponseStatusException.class,
            () -> {
              service.updateAccount(request, 9999999l);
            });

    assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    assertTrue(exception.getReason().contains("CustomerAccount not found. id=9999999"));
  }

  @Test
  @DisplayName("アカウント削除.正常")
  public void deleteAccountTest01() {
    CustomerAccount entity = CustomerAccount.builder().name("test").email("test@test.com").build();
    entity = customerAccountRepository.save(entity);

    service.deleteAccount(entity.getId());

    assertEquals(0, customerAccountRepository.findAll().size());
  }
}
