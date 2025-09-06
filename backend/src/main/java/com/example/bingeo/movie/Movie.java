package com.example.bingeo.movie;
import com.example.bingeo.ratings.Rating;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "titles")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Movie {
    @Id
    private String tconst; // IMDb ID, e.g. tt1375666

    private String primaryTitle; // Display title
    private Integer startYear; // Release year
    private String genres; // Comma-separated from TSV
    private String titleType; // movie, tvSeries, short, etc.

    @OneToOne(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private Rating rating; // Linked rating row (may be null)

    public Movie(String tconst) {
        this.tconst = tconst;
    }

}
