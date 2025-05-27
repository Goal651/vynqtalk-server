package com.vynqtalk.server.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vynqtalk.server.model.User;
import com.vynqtalk.server.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository; // your user repo

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Convert your User entity to Spring Security's UserDetails:
        return org.springframework.security.core.userdetails.User

                .withUsername(user.getEmail())
                .password(user.getPassword()) // hashed password
                .authorities(user.getIsAdmin() ? "ROLE_ADMIN" : "ROLE_USER") // or map real roles here
                .build();
    }
}
