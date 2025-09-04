package com.example.bingeo.web;

import com.example.bingeo.auth.JwtService;
import com.example.bingeo.auth.PasswordService;
import com.example.bingeo.model.User;
import com.example.bingeo.user.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordService passwordService;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepo, PasswordService passwordService, JwtService jwtService) {
        this.userRepo = userRepo;
        this.passwordService = passwordService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        if (userRepo.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already in use"));
        }

        User user = new User();
        user.setEmail(email);
        user.setPasswordHash(passwordService.hash(password));
        userRepo.save(user);

        // ✅ fixed: user.getId() instead of user.getUser().getId()
        String token = jwtService.generate(user.getId());

        return ResponseEntity.ok(Map.of("token", token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
        String email = body.get("email");
        String password = body.get("password");

        return userRepo.findByEmail(email)
                .filter(u -> passwordService.matches(password, u.getPasswordHash()))
                .map(u -> {
                    // ✅ same fix here
                    String token = jwtService.generate(u.getId());
                    return ResponseEntity.ok(Map.of("token", token));
                })
                .orElse(ResponseEntity.status(401).body(Map.of("error", "Invalid credentials")));
    }
}
