package com.aki.customer.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import com.aki.customer.entity.CustomerAccount;
import com.aki.customer.service.CustomerAccountService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(CustomerAccountController.class)
class CustomerAccountControllerTest {
  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper objectMapper;
  @MockitoBean private CustomerAccountService service;

  @Test
  @DisplayName("GET.正常")
  void getAccountTest01() throws Exception {
    CustomerAccount account =
        CustomerAccount.builder().id(1L).name("test").email("test@test.com").build();
    doReturn(account).when(service).getAccount(1L);

    mockMvc
        .perform(get("/api/customers/account").param("id", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("test"))
        .andExpect(jsonPath("$.email").value("test@test.com"));
  }

  @Test
  @DisplayName("GET.異常")
  void getAccountTest02() throws Exception {
    doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "CustomerAccount not found. id=1"))
        .when(service)
        .getAccount(1L);

    mockMvc
        .perform(get("/api/customers/account").param("id", "1"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.error").value("CustomerAccount not found. id=1"));
  }

  @Test
  @DisplayName("POST.正常")
  void createAccountTest01() throws Exception {
    CustomerAccount account =
        CustomerAccount.builder().id(1L).name("test").email("test@test.com").build();
    doReturn(account).when(service).registerAccount(any());

    String request = "{\"name\":\"test\", \"email\":\"test@test.com\"}";
    mockMvc
        .perform(
            post("/api/customers/account").contentType(MediaType.APPLICATION_JSON).content(request))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("test"))
        .andExpect(jsonPath("$.email").value("test@test.com"));
  }

  @Test
  @DisplayName("POST.異常")
  void createAccountTest02() throws Exception {
    String request = "{\"name\":\"\", \"email\":\"test\"}";
    mockMvc
        .perform(
            post("/api/customers/account").contentType(MediaType.APPLICATION_JSON).content(request))
        .andExpect(status().isBadRequest());
    verify(service, never()).registerAccount(any());
  }

  @Test
  @DisplayName("PUT.正常")
  void updateAccountTest01() throws Exception {
    CustomerAccount account =
        CustomerAccount.builder().id(1L).name("update").email("update@test.com").build();
    doReturn(account).when(service).updateAccount(any(), any());

    String request = "{\"name\":\"test\", \"email\":\"test@test.com\"}";
    mockMvc
        .perform(
            put("/api/customers/account")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1))
        .andExpect(jsonPath("$.name").value("update"))
        .andExpect(jsonPath("$.email").value("update@test.com"));
  }

  @Test
  @DisplayName("PUT.異常")
  void updateAccountTest02() throws Exception {
    String request = "{\"name\":\"\", \"email\":\"test.com\"}";
    mockMvc
        .perform(
            put("/api/customers/account")
                .param("id", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request))
        .andExpect(status().isBadRequest());
    verify(service, never()).updateAccount(any(), any());
  }

  @Test
  @DisplayName("DELETE.正常")
  void deleteAccountTest01() throws Exception {
    mockMvc
        .perform(delete("/api/customers/account").param("id", "1"))
        .andExpect(status().isNoContent());
  }
}
