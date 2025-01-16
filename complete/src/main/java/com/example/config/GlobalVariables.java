package com.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "var")
public record GlobalVariables(
        String firstName,
        String lastName,
        String secretKey,
        Long expirationMs,
        String dbUsername,
        String dbPassword,
        String ffUsername,
        String ffPassword
) {
}
