package com.example.bingeo.dto;
import jakarta.validation.constraints.*;

public record SignupRequest(@Email @NotBlank String email,
                            @NotBlank @Size(min=6) String password) {}

public record LoginRequest(@Email @NotBlank String email,
                           @NotBlank String password) {}

public record AuthResponse(String token) {}
