package com.fastfile.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "var")
public record GlobalVariables(
        String secretKey,
        String dbUsername,
        String dbPassword,
        String ffUsername,
        String ffPassword
) {
}
