package com.fastfile.controller;

import com.fastfile.model.UserLogin;

import com.fastfile.model.User;
import com.fastfile.service.AuthService;
import com.fastfile.service.UserService;
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
    public User getCurrentUser(@RequestHeader(value="Authorization") String authToken) {
        return authService.getCurrentUser(authToken);
    }
}
