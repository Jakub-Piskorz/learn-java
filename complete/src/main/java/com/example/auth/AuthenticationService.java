package com.example.auth;

import com.example.config.JwtService;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import com.example.model.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.firstname)
                .lastnast(request.lastnast)
                .email(request.email)
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepo.save(user);
        var JwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token()
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));
        var user = userRepo.findByEmail(request.getEmail())
                .orElseThrow();
        userRepo.save(user);
        var JwtToken = jwtService.generateToken((UserDetails) user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
