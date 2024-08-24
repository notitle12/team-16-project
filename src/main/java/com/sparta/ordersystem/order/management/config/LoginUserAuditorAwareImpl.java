package com.sparta.ordersystem.order.management.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

/***
 * @Created_by ,LastModifiedBy 의 값을 자동으로 설정해주기 위함
 ***/
@Component
public class LoginUserAuditorAwareImpl implements AuditorAware<Long> {

    //TODO: 나중에 로그인한 회원의 ID를 세션 혹은 header로 가져와야된다.

    @Override
    public Optional<Long> getCurrentAuditor() {
        // 현재 사용자 ID를 반환합니다. 실제로는 세션, 보안 컨텍스트 등에서 가져와야 합니다.
        return Optional.of(1L); // 예시로 1L 반환
    }
}