package com.aki.customer.controller;

import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.aki.customer.dto.CustomerCreateRequest;
import com.aki.customer.entity.CustomerAccount;
import com.aki.customer.service.CustomerAccountService;

@RestController
public class CustomerController {
  private final CustomerAccountService customerAccountService;

  public CustomerController(CustomerAccountService customerAccountService) {
    this.customerAccountService = customerAccountService;
  }

  @GetMapping("/api/customers/me")
  public Map<String, String> getMe(@RequestHeader("X-Auth-User-ID") String userId) {
    return Map.of("message", "Success", "received_user_id", userId);
  }

  @GetMapping("/api/customers/test")
  public String test() {
    return "Hello from Java Lambda!";
  }

  @PostMapping("/api/customers/create")
  public CustomerAccount create(@Valid @RequestBody CustomerCreateRequest request) {
    return this.customerAccountService.register(request);
  }
}
