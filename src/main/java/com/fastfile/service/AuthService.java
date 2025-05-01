package com.fastfile.service;

import com.fastfile.model.User;
import com.fastfile.repository.UserRepository;
import com.fastfile.auth.JwtService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public String authenticate(String username, String password) throws RuntimeException {
        // Find the user by username
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found:" + username);
        }

        // Check if the password matches
        User user = userOpt.get();
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        // Generate token if valid
        return jwtService.generateToken(user);
    }

    public User getCurrentUser(String token) {
        token = token.substring(7); // Remove "Bearer "
        Claims claims = jwtService.extractClaims(token);
        String userName = claims.getSubject();
        Optional<User> optUser = userRepository.findByUsername(userName);
        if (optUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User user = optUser.get();
        System.out.println("userId");
        System.out.println(user.getId());
        return user;
    }
}
