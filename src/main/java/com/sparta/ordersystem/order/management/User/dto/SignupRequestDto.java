package com.sparta.ordersystem.order.management.User.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email
    @NotBlank
    private String email;

    private boolean admin = false;

    private boolean Ceo = false;

    private String adminToken = "";

    private String ceoToken = "";
}
