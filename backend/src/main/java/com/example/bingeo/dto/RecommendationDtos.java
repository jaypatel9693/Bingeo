package com.example.bingeo.dto;

import jakarta.validation.constraints.NotBlank;

public class RecommendationDtos {
  public record CreateRecReq(@NotBlank String movieTitle, @NotBlank String description, String status, Integer tmdbId) {}
}
