package com.sparta.ordersystem.order.management.User.dto;

import com.sparta.ordersystem.order.management.User.entity.UserRoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponseDto {

    private String email;

    private String username;

    private UserRoleEnum role;

}
