package com.aki.customer.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aki.customer.dto.CustomerAccountRequest;
import com.aki.customer.entity.CustomerAccount;
import com.aki.customer.service.CustomerAccountService;

@RestController
public class CustomerAccountController {
  private final CustomerAccountService customerAccountService;

  public CustomerAccountController(CustomerAccountService customerAccountService) {
    this.customerAccountService = customerAccountService;
  }

  @GetMapping("/api/customers/account")
  public CustomerAccount get(@RequestParam Long id) {
    return this.customerAccountService.getAccount(id);
  }

  @DeleteMapping("/api/customers/account")
  public ResponseEntity<Void> delete(@RequestParam Long id) {
    this.customerAccountService.deleteAccount(id);
    return ResponseEntity.noContent().build();
  }

  @PostMapping("/api/customers/create")
  public CustomerAccount create(@Valid @RequestBody CustomerAccountRequest request) {
    return this.customerAccountService.registerAccount(request);
  }

  @PutMapping("/api/customers/create")
  public CustomerAccount update(
      @Valid @RequestBody CustomerAccountRequest request, @RequestParam String id) {
    return this.customerAccountService.updateAccount(request, id);
  }
}
