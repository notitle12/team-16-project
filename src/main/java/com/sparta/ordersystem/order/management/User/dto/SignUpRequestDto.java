package com.sparta.ordersystem.order.management.User.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {

    @NotBlank
    @Pattern(regexp = "^[a-z0-9]{4,10}$", //최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 구성
    message = "아이디는 최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 구성되어야 합니다.")
    private String username;

    @NotBlank
    @Size(min = 8, max = 15, message = "Password must be between 8 and 15 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]*$", //최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9), 특수문자
    message = "비밀번호는 최소 8자 이상, 15자 이하이며, 대문자, 소문자, 숫자, 특수문자를 포함해야 합니다.")
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
