package com.aiexpensetracker.security.service;

import com.aiexpensetracker.security.model.CustomUserPrincipal;
import com.aiexpensetracker.user.entity.User;
import com.aiexpensetracker.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user =  userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Wrong email or password."));
        return new CustomUserPrincipal(user);
    }
}