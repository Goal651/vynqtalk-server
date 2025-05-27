package com.vynqtalk.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vynqtalk.server.model.AuthResult;
import com.vynqtalk.server.model.User;
import com.vynqtalk.server.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    public AuthResult authenticate(User user) {
        User dbUser = userRepo.findByEmail(user.getEmail());
        return (dbUser != null && user.getPassword()== dbUser.getPassword())
                ? new AuthResult(true, dbUser)
                : new AuthResult(false, null);
    }

    public void saveUser(User user) {
        userRepo.save(user);
    }
}
