package com.sparta.ordersystem.order.management.User.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequestDto {

    private String email;

    private String password;
}
