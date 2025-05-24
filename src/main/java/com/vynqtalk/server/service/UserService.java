package com.vynqtalk.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.vynqtalk.server.model.User;
import com.vynqtalk.server.repository.UserRepo;

@Service
public class UserService {
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private UserRepo userRepo;

    public boolean authenticate(User user) {
        User dbUser = userRepo.findByEmail(user.getEmail());
        return passwordEncoder.matches(user.getPassword(), dbUser.getPassword());
    }

    public void saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }
}
