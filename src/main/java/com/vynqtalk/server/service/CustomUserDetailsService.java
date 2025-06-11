package com.vynqtalk.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.vynqtalk.server.model.User;
import com.vynqtalk.server.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private  UserRepository userRepository; 


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        // Convert your User entity to Spring Security's UserDetails:
        return org.springframework.security.core.userdetails.User

                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getIsAdmin() ? "ROLE_ADMIN" : "ROLE_USER") 
                .build();
    }
}
