package com.example.bingeo.model;
import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Movie {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  private String title;
  @Column(length=2000) private String overview;
  private Integer year;
  private String posterUrl;
  private Double rating;
}
