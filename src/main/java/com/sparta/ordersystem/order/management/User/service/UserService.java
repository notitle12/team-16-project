package com.sparta.ordersystem.order.management.User.service;

import com.sparta.ordersystem.order.management.User.dto.SignupRequestDto;
import com.sparta.ordersystem.order.management.User.entity.User;
import com.sparta.ordersystem.order.management.User.entity.UserRoleEnum;
import com.sparta.ordersystem.order.management.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // CEO+TOKEN
    private final String CEO_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 회원가입
    public void signup(SignupRequestDto requestDto) {
        String email = requestDto.getEmail();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        Optional<User> checkEmail = userRepository.findByEmail(email);
        if (checkEmail.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }

        // email 중복확인
        String username = requestDto.getUsername();
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            throw new IllegalArgumentException("중복된 닉네임이 존재합니다.");
        }

        // 사용자 ROLE 확인

        // Admin
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                throw new IllegalArgumentException("관리자 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.ADMIN;
        }

        // CEO
        if (requestDto.isCeo()) {  // CEO 역할 추가
            if (!CEO_TOKEN.equals(requestDto.getCeoToken())) {
                throw new IllegalArgumentException("CEO 암호가 틀려 등록이 불가능합니다.");
            }
            role = UserRoleEnum.CEO;
        }

        // ADMIN과 CEO 모두 체크된 경우 에러 처리
        if (requestDto.isAdmin() && requestDto.isCeo()) {
            throw new IllegalArgumentException("ADMIN과 CEO 권한은 동시에 부여될 수 없습니다.");
        }

        // 사용자 등록
        User user = new User(username, password, email, role);
        userRepository.save(user);
    }
}
