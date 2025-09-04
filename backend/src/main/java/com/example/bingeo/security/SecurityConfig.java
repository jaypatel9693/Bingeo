package com.example.bingeo.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

@Configuration
public class SecurityConfig {

  private final JwtUtil jwtUtil;
  public SecurityConfig(JwtUtil jwtUtil){ this.jwtUtil = jwtUtil; }

  @Bean PasswordEncoder passwordEncoder(){ return new BCryptPasswordEncoder(); }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf->csrf.disable())
      .cors(c->c.configurationSource(req->{
        var cfg = new CorsConfiguration();
        cfg.setAllowedOrigins(List.of(System.getProperty("bingeo.cors.allowed-origin",
            System.getenv().getOrDefault("BINGEO_CORS","http://localhost:3000"))));
        cfg.setAllowedMethods(List.of("GET","POST","PUT","DELETE","OPTIONS"));
        cfg.setAllowedHeaders(List.of("*"));
        cfg.setAllowCredentials(true);
        return cfg;
      }))
      .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(auth->auth
        .requestMatchers("/api/auth/**").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/movies/**").permitAll()
        .anyRequest().authenticated()
      )
      .addFilterBefore(new JwtFilter(jwtUtil), BasicAuthenticationFilter.class);
    return http.build();
  }

  static class JwtFilter implements Filter {
    private final JwtUtil jwt;
    JwtFilter(JwtUtil jwt){ this.jwt = jwt; }
    @Override public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
      HttpServletRequest req = (HttpServletRequest) request;
      String header = req.getHeader("Authorization");
      if(header != null && header.startsWith("Bearer ")) {
        try {
          String email = jwt.validateAndGetSubject(header.substring(7));
          var auth = new UsernamePasswordAuthenticationToken(email, null, List.of());
          SecurityContextHolder.getContext().setAuthentication(auth);
        } catch (Exception ignored) {}
      }
      chain.doFilter(request,response);
    }
  }
}
