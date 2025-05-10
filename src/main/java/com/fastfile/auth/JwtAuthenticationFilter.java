package com.fastfile.auth;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip authentication for public paths
        if (path.startsWith("/auth/") || path.equals("/") || path.equals("/test")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the token from the Authorization header
        String token = request.getHeader("Authorization");


        if (token != null && token.startsWith("Bearer ")) {
            try {
                token = token.substring(7);  // Remove the "Bearer " prefix
                final Claims claims = jwtService.extractClaims(token);
                final String username = claims.getSubject();

                // If valid, set authentication
                if (jwtService.isTokenValid(token, username)) {
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            Collections.emptyList());
                    authentication.setDetails(claims);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    logger.warn("Invalid JWT token: " + token);
                }

            } catch (Exception e) {
                // Handle invalid token or other errors
                SecurityContextHolder.clearContext();
                logger.warn("JWT missing or malformed for request to " + request.getRequestURI());
                logger.warn(e.getMessage());
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            }
        }
        filterChain.doFilter(request, response);
    }
}
