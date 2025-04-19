package com.smartvocab.smart_vocab_backend.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartvocab.smart_vocab_backend.exception.JwtAuthenticationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomUserDetailsService uds) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = uds;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            String jwt = extractJwtFromRequest(request);

            if (jwt != null) {
                // Extract the email from JWT token
                String email = jwtUtil.extractEmail(jwt);
                // Extract the user ID from JWT token
                String userId = jwtUtil.extractUserId(jwt);

                if (email != null && userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    // Load UserDetails using userId (since our UserDetails.username holds the user ID)
                    UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                    if (jwtUtil.validateToken(jwt, userDetails)) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities());

                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }

            filterChain.doFilter(request, response);
        } catch (JwtAuthenticationException e) {
            log.error("JWT Authentication failed: {}", e.getMessage());
            sendErrorResponse(response, HttpStatus.UNAUTHORIZED, "JWT authentication failed: " + e.getMessage());
        } catch (Exception e) {
            log.error("Authentication error: {}", e.getMessage());
            filterChain.doFilter(request, response);
        }
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.trim().startsWith("Bearer ")) {
            // Make sure to trim any whitespace
            return authHeader.substring(7).trim();
        }
        return null;
    }

    private void sendErrorResponse(
            HttpServletResponse response,
            HttpStatus status,
            String message) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", new Date());
        errorDetails.put("status", status.value());
        errorDetails.put("error", status.getReasonPhrase());
        errorDetails.put("message", message);

        response.getWriter().write(new ObjectMapper().writeValueAsString(errorDetails));
    }
}