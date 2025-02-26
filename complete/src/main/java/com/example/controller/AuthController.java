package com.example.controller;

import com.example.dto.UserDTO;
import com.example.model.UserLogin;

import com.example.model.User;
import com.example.service.AuthService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserLogin user) {
        return authService.authenticate(user.getLogin(), user.getPassword());
    }
    @GetMapping("/user")
    public UserDTO getCurrentUser(@RequestHeader(value="Authorization") String authToken) {
        return authService.getCurrentUser(authToken);
    }
}
