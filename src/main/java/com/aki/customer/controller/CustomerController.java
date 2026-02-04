package com.aki.customer.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
  @GetMapping("/api/users/me")
  public Map<String, String> getMe(@RequestHeader("X-Auth-User-ID") String userId) {
    return Map.of("message", "Success", "received_user_id", userId);
  }
}
