package com.sparta.ordersystem.order.management.User.service;

import com.sparta.ordersystem.order.management.User.dto.SignUpRequestDto;
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

    // OWNER_TOKEN
    private final String OWNER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // MANAGER_TOKEN
    private final String MANAGER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    //MASTER_TOKEN
    private final String MASTER_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    // 회원가입
    public void signUp(SignUpRequestDto requestDto) {
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

        boolean isOwner = false;
        boolean isManager = false;
        boolean isMaster = false;

        // 기본 고객
        UserRoleEnum role = UserRoleEnum.CUSTOMER;

        // 사업주
        if (requestDto.isOwner()) {
            if (!OWNER_TOKEN.equals(requestDto.getOwnerToken())) {
                throw new IllegalArgumentException("사업주 코드가 틀려 등록이 불가능합니다.");
            }
            isOwner = true;
            role = UserRoleEnum.OWNER;
        }

        // 관리자
        if (requestDto.isManager()) {
            if (!MANAGER_TOKEN.equals(requestDto.getManagerToken())) {
                throw new IllegalArgumentException("관리자 코드가 틀려 등록이 불가능합니다.");
            }
            isManager = true;
            role = UserRoleEnum.MANAGER;
        }

        // 마스터
        if (requestDto.isMaster()) {  // CEO 역할 추가
            if (!MASTER_TOKEN.equals(requestDto.getMasterToken())) {
                throw new IllegalArgumentException("마스터 코드가 틀려 등록이 불가능합니다.");
            }
            isMaster = true;
            role = UserRoleEnum.MASTER;
        }

        // 여러 권한이 동시에 설정된 경우 에러 처리
        if ((isOwner && isManager) || (isOwner && isMaster) || (isManager && isMaster)) {
            throw new IllegalArgumentException("권한은 동시에 부여될 수 없습니다.");
        }

        // 사용자 등록
        User user = new User(username, password, email, role);
        userRepository.save(user);
    }
}
