#!/bin/bash

# Create JwtUtil if it doesn't exist
if [ ! -f "src/main/java/com/example/bingeo/security/JwtUtil.java" ]; then
    echo "📝 Creating JwtUtil.java..."
    mkdir -p src/main/java/com/example/bingeo/security/
    cat > src/main/java/com/example/bingeo/security/JwtUtil.java << 'JWT_CONTENT'
package com.example.bingeo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final long expirationMs = 86400000;

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(key)
                .compact();
    }

    public String validateAndGetSubject(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token, String username) {
        final String tokenUsername = validateAndGetSubject(token);
        return (tokenUsername.equals(username));
    }
}
JWT_CONTENT
    echo "✅ Created JwtUtil.java"
fi

# Create CustomUserDetailsService if it doesn't exist
if [ ! -f "src/main/java/com/example/bingeo/security/CustomUserDetailsService.java" ]; then
    echo "📝 Creating CustomUserDetailsService.java..."
    cat > src/main/java/com/example/bingeo/security/CustomUserDetailsService.java << 'USER_CONTENT'
package com.example.bingeo.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
USER_CONTENT
    echo "✅ Created CustomUserDetailsService.java"
fi

# Create JwtAuthenticationFilter if it doesn't exist
if [ ! -f "src/main/java/com/example/bingeo/security/JwtAuthenticationFilter.java" ]; then
    echo "📝 Creating JwtAuthenticationFilter.java..."
    cat > src/main/java/com/example/bingeo/security/JwtAuthenticationFilter.java << 'FILTER_CONTENT'
package com.example.bingeo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(CustomUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String path = request.getServletPath();
        if (path.startsWith("/api/auth/") || path.equals("/actuator/health")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            username = jwtUtil.validateAndGetSubject(token);
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if (jwtUtil.validateToken(token, userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
FILTER_CONTENT
    echo "✅ Created JwtAuthenticationFilter.java"
fi
