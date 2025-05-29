package com.fastfile.service;

import com.fastfile.model.User;
import com.fastfile.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.regex.Pattern;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthService authService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authService = authService;
    }

    @Value("${storage.limits.free}")
    private long freeLimit;
    @Value("${storage.limits.premium}")
    private long premiumLimit;

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User getCurrentUser() {
        var userId = authService.getUserId();
        return userRepository.findById(Long.parseLong(userId)).orElse(null);
    }

    public long getUserStorageLimit() {
        User user = getCurrentUser();
        boolean isUserPremium = Objects.equals(user.getUserType(), "premium");

        return isUserPremium ? premiumLimit : freeLimit;
    }

    public boolean updateUserType(String newUserType) {
        if (!Pattern.matches("^(free|premium)$", newUserType)) {
            return false;
        }
        User user = getCurrentUser();
        user.setUserType(newUserType);
        userRepository.save(user);

        return true;
    }

    public long getUserStorage() {
        User user = getCurrentUser();

        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user.getUsedStorage();
    }

}
