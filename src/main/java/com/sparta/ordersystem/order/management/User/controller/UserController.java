package com.sparta.ordersystem.order.management.User.controller;

import com.sparta.ordersystem.order.management.User.dto.SignUpRequestDto;
import com.sparta.ordersystem.order.management.User.dto.UserInfoRequestDto;
import com.sparta.ordersystem.order.management.User.dto.UserInfoResponseDto;
import com.sparta.ordersystem.order.management.User.security.UserDetailsImpl;
import com.sparta.ordersystem.order.management.User.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @PostMapping("/auth/sign-up")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpRequestDto requestDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                errorMessage.append(fieldError.getField()).append(" 필드: ")
                        .append(fieldError.getDefaultMessage())
                        .append("; ");
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorMessage.toString());
        }

        try {
            userService.signUp(requestDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok("회원가입 성공");
    }

    // 현재 로그인한 사용자의 정보 조회
    @GetMapping("/users/me")
    public ResponseEntity<UserInfoResponseDto> getCurrentUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        String email = userDetails.getUser().getEmail();

        UserInfoResponseDto userInfo = userService.getUserByEmail(email);
        return ResponseEntity.ok(userInfo);
    }

    // 현재 로그인한 사용자의 정보 수정
    // 현재 로그인한 사용자의 정보 수정
    @PutMapping("/users/me")
    public ResponseEntity<String> updateCurrentUserInfo(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody UserInfoRequestDto updateDto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            // 검증 오류 메시지를 수집합니다.
            List<String> errorMessages = bindingResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getField() + " : " + fieldError.getDefaultMessage())
                    .collect(Collectors.toList());

            // 검증 오류 메시지를 ResponseEntity에 담아 응답합니다.
            return ResponseEntity.badRequest().body(String.join(", ", errorMessages));
        }

        String email = userDetails.getUser().getEmail();

        try {
            userService.updateUserByEmail(email, updateDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok("정보 수정 성공");
    }

}
