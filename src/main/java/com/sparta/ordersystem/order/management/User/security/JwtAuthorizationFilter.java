package com.sparta.ordersystem.order.management.User.security;

import com.sparta.ordersystem.order.management.User.config.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        //요청의 헤더에서 JWT 토큰을 추출
        String tokenValue = jwtUtil.getJwtFromHeader(req);

        //토큰이 존재하고, 그 토큰이 유효한지 확인, 유효하지 않으면 로그를 남기고 필터 체인을 중단
        if (StringUtils.hasText(tokenValue)) {

            if (!jwtUtil.validateToken(tokenValue)) {
                log.error("Token Error");
                return;
            }

            //토큰에서 사용자 정보(이메일 등)를 추출
            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            //사용자 정보를 기반으로 인증을 설정, 문제가 발생하면 예외를 로그로 남기고 필터 체인을 중단
            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        //인증이 완료된 후, 다음 필터로 요청을 전달
        filterChain.doFilter(req, res);
    }

    // 인증 처리
    // 사용자 이메일을 사용하여 인증 객체를 생성하고, 이를 Spring Security의 SecurityContext에 설정
    public void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(email);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    // loadUserByUsername 는 Spring Security에서 UserDetailsService 구현체가 사용자를 찾지 못했을 때 던지는 표준 예외로
    // email을 사용자 식별자로 사용하더라도 해당 메서드를 사용한다.
    private Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
