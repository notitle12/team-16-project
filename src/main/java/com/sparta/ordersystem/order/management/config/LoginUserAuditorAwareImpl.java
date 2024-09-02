package com.sparta.ordersystem.order.management.config;

import com.sparta.ordersystem.order.management.User.config.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Component
public class LoginUserAuditorAwareImpl implements AuditorAware<Long> {

    private final JwtUtil jwtUtil;

    public LoginUserAuditorAwareImpl(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Optional<Long> getCurrentAuditor() {
        HttpServletRequest request =((ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes()).getRequest();

        String token = jwtUtil.getJwtFromHeader(request);

        Long userId = getUserIdFromToken(token);

        return Optional.of(userId); // 예시로 1L 반환
    }

    private Long getUserIdFromToken(String token) {
        Claims claims = jwtUtil.getUserInfoFromToken(token);
        return claims.get("user_id", Long.class);
    }
}