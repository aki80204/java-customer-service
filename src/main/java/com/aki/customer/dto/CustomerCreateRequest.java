package com.aki.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class CustomerCreateRequest {
  @NotBlank(message = "名前は必須です")
  @Size(max = 255)
  private String name;

  @NotBlank(message = "メールアドレスは必須です")
  @Email(message = "メールアドレスの形式が正しくありません")
  @Size(max = 255)
  private String email;
}
