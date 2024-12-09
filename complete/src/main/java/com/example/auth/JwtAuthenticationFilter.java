package com.example.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        // Skip authentication for public paths
        if (path.startsWith("/auth/")) {
            System.out.println("AUTHS!");
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the token from the Authorization header
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);  // Remove the "Bearer " prefix

            try {
                // Validate the token
                String username = jwtService.validateToken(token);

                // If valid, set authentication (you can extend this to a proper Authentication object)
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, List.of());
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                // Handle invalid token or other errors
                SecurityContextHolder.clearContext();
            }
        }

        filterChain.doFilter(request, response);  // Proceed with the filter chain
    }
}
