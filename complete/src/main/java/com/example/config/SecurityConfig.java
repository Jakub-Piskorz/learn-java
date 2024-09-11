package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private Environment env;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        if (env.acceptsProfiles(Profiles.of("dev"))) {
            return http.authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/games/**").permitAll()
                            .requestMatchers("/").permitAll()
                            .requestMatchers("/actuator/**").permitAll()
                            .anyRequest().authenticated()).csrf(csrf -> csrf
                            .ignoringRequestMatchers(/*"/h2-console/**", */"/api/games/**"))
                    .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                    .httpBasic(Customizer.withDefaults()).build();
        } else {
            return http.authorizeHttpRequests(auth -> auth
                            .requestMatchers("/api/games/**").permitAll()
                            .requestMatchers("/").permitAll()
                            .anyRequest().authenticated()).csrf(csrf -> csrf
                            .ignoringRequestMatchers(/*"/h2-console/**", */"/api/games/**"))
                    .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                    .httpBasic(Customizer.withDefaults()).build();
        }
    }
}
