package com.example.model;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@RequiredArgsConstructor
public class UserLogin {
    @Autowired
    private final String username;
    @Autowired
    private final String password;
}
