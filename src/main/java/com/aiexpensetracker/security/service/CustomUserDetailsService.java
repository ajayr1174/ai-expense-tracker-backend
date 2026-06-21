package com.aiexpensetracker.security.service;

import com.aiexpensetracker.security.model.CustomUserPrincipal;
import com.aiexpensetracker.user.entity.Users;
import com.aiexpensetracker.user.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users users =  usersRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Wrong email or password."));
        return new CustomUserPrincipal(users);
    }
}