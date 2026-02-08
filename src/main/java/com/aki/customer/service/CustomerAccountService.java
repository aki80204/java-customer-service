package com.aki.customer.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.aki.customer.dto.CustomerAccountRequest;
import com.aki.customer.entity.CustomerAccount;
import com.aki.customer.repository.CustomerAccountRepository;

@Service
public class CustomerAccountService {

  private final CustomerAccountRepository customerAccountRepository;

  public CustomerAccountService(CustomerAccountRepository customerAccountRepository) {
    this.customerAccountRepository = customerAccountRepository;
  }

  public CustomerAccount getAccount(String id) {
    return customerAccountRepository
        .findById(Long.valueOf(id))
        .orElseThrow(
            () ->
                new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "CustomerAccount not found. id=" + id));
  }

  public CustomerAccount registerAccount(CustomerAccountRequest request) {
    CustomerAccount customerAccount = new CustomerAccount();
    customerAccount.setName(request.getName());
    customerAccount.setEmail(request.getEmail());
    customerAccountRepository.save(customerAccount);

    return customerAccountRepository.save(customerAccount);
  }

  public CustomerAccount updateAccount(CustomerAccountRequest request, String id) {
    CustomerAccount customerAccount =
        customerAccountRepository
            .findById(Long.valueOf(id))
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "CustomerAccount not found. id=" + id));
    customerAccount.setName(request.getName());
    customerAccount.setEmail(request.getEmail());
    customerAccountRepository.save(customerAccount);

    return customerAccountRepository.save(customerAccount);
  }

  public void deleteAccount(String id) {
    customerAccountRepository.deleteById(Long.valueOf(id));
  }
}
