package com.sparta.ordersystem.order.management.User.security;

import com.sparta.ordersystem.order.management.User.entity.User;
import com.sparta.ordersystem.order.management.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Not Found " + email));

        // UserDetailsImpl 객체를 생성할 때 User 객체를 전달합니다.
        return new UserDetailsImpl(user);
    }
}
