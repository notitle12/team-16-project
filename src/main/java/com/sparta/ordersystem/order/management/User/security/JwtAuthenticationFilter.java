package com.sparta.ordersystem.order.management.User.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.ordersystem.order.management.User.config.JwtUtil;
import com.sparta.ordersystem.order.management.User.dto.LoginRequestDto;
import com.sparta.ordersystem.order.management.User.entity.UserRoleEnum;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/v1/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getEmail(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            // 요청 본문을 읽는 과정에서 예외가 발생하면 로그를 남기고 런타임 예외를 던짐
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        // 인증이 성공한 경우, 인증된 사용자의  id, 이메일과 역할을 가져옴
        Long userId = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getUser_id();
        String email = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getEmail();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();


        //JWT토큰 생성
        String token = jwtUtil.createToken(userId, email, role);

        // 생성된 JWT 토큰을 응답 헤더에 추가
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);

        // 로그인 성공 시 로그를 출력
        log.info("로그인 성공: 이메일 = {}, 역할 = {}", email, role);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        // 인증에 실패한 경우 HTTP 상태 코드를 401(Unauthorized)로 설정
        response.setStatus(401);

        // 실패 로그를 추가
        log.warn("로그인 실패");
    }
}
