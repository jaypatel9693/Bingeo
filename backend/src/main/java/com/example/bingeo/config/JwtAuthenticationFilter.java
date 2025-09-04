package com.example.bingeo.config;

import com.example.bingeo.auth.JwtService;
import com.example.bingeo.model.User;
import com.example.bingeo.user.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends GenericFilter {

  private final JwtService jwt;
  private final UserRepository users;

  public JwtAuthenticationFilter(JwtService jwt, UserRepository users) {
    this.jwt = jwt; this.users = users;
  }

  @Override
  public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest r = (HttpServletRequest) req;
    String h = r.getHeader("Authorization");
    if (h != null && h.startsWith("Bearer ")) {
      String token = h.substring(7);
      try {
        Long uid = jwt.userId(token);
        Optional<User> u = users.findById(uid);
        if (u.isPresent()) {
          var auth = new UsernamePasswordAuthenticationToken(u.get(), null, null);
          SecurityContextHolder.getContext().setAuthentication(auth);
        }
      } catch (Exception ignored) { }
    }
    chain.doFilter(req, res);
  }
}
