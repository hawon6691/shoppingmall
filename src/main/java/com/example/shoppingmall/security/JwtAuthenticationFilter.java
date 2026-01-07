package com.example.shoppingmall.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                if (!jwtUtil.isTokenExpired(token)) {
                    Long userId = jwtUtil.extractUserId(token);
                    String username = jwtUtil.extractUsername(token);
                    String role = jwtUtil.extractRole(token);

                    // Add user info to request attributes
                    request.setAttribute("userId", userId);
                    request.setAttribute("username", username);
                    request.setAttribute("role", role);
                }
            } catch (Exception e) {
                // Invalid token, continue without authentication
                logger.warn("Invalid JWT token: " + e.getMessage());
            }
        }

        filterChain.doFilter(request, response);
    }
}
