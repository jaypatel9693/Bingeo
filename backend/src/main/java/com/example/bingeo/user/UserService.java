package com.example.bingeo.user;

import com.example.bingeo.model.User;
import com.example.bingeo.auth.PasswordService;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository repo;
    private final PasswordService passwords;

    public UserService(UserRepository repo, PasswordService passwords) {
        this.repo = repo;
        this.passwords = passwords;
    }

    public User register(String email, String password) {
        if (repo.existsByEmail(email)) {
            throw new RuntimeException("Email already taken");
        }

        User u = new User();
        u.setEmail(email);
        u.setPasswordHash(passwords.hash(password));
        u.setRole("USER");
        return repo.save(u);
    }

    public User validate(String email, String password) {
        User u = repo.findByEmail(email)
                     .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!passwords.matches(password, u.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }
        return u;
    }
}
