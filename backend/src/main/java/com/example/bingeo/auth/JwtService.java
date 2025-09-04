package com.example.bingeo.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
  private final Key key;
  private final int expiryDays;

  public JwtService(@Value("${app.jwt.secret}") String secret,
                    @Value("${app.jwt.expiryDays}") int expiryDays) {
    this.key = Keys.hmacShaKeyFor(secret.getBytes());
    this.expiryDays = expiryDays;
  }

  public String generate(Long userId) {
    Instant now = Instant.now();
    Instant exp = now.plusSeconds(60L * 60 * 24 * expiryDays);
    return Jwts.builder()
      .setClaims(Map.of("uid", userId))
      .setIssuedAt(Date.from(now))
      .setExpiration(Date.from(exp))
      .signWith(key, SignatureAlgorithm.HS256)
      .compact();
  }

  public Jws<Claims> parse(String token) {
    return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
  }

  public Long userId(String token) {
    return parse(token).getBody().get("uid", Number.class).longValue();
  }
}
