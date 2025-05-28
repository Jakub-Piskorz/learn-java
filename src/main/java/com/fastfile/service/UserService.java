package com.fastfile.service;

import com.fastfile.model.User;
import com.fastfile.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    @Autowired
    private UserRepository repo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthService authService;
    @Value("${storage.limits.free}")
    private long freeLimit;
    @Value("${storage.limits.premium}")
    private long premiumLimit;

    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return repo.save(user);
    }
    public User findByUsername(String username) {
        return repo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }
    public long getUserStorageLimit() {
        String userId = authService.getUserId();
//        TODO check if user is free or premium (probably add this as table in DB)
        boolean isUserPremium = false;
        return freeLimit;
    }
}
