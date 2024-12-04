package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    public User register(User user) {
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repo.save(user);
    }
    public User findByUsername(String username) {
        return repo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
