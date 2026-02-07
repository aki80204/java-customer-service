package com.aki.customer.service;

import com.aki.customer.dto.CustomerCreateRequest;
import com.aki.customer.entity.CustomerAccount;
import com.aki.customer.repository.CustomerAccountRepository;

public class CustomerAccountService {

  private final CustomerAccountRepository customerAccountRepository;

  public CustomerAccountService(CustomerAccountRepository customerAccountRepository) {
    this.customerAccountRepository = customerAccountRepository;
  }

  public CustomerAccount register(CustomerCreateRequest request) {
    CustomerAccount customerAccount = new CustomerAccount();
    customerAccount.setName(request.getName());
    customerAccount.setEmail(request.getEmail());
    customerAccountRepository.save(customerAccount);

    return customerAccountRepository.save(customerAccount);
  }
}
