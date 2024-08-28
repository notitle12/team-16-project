package com.sparta.ordersystem.order.management.User.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email
    @NotBlank
    private String email;

    private boolean isOwner = false;

    private boolean isManager = false;

    private boolean isMaster = false;

    private String ownerToken = "";

    private String managerToken = "";

    private String masterToken = "";
}
