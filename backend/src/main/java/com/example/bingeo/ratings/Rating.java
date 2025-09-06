package com.example.bingeo.ratings;

import com.example.bingeo.movie.Movie;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ratings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rating {

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tconst")
    private Movie movie;

    @Column(name = "average_rating")
    private Double averageRating;

    @Column(name = "num_votes")
    private Integer numVotes;

    // Helper method to get the tconst from the associated movie
    public String getTconst() {
        return movie != null ? movie.getTconst() : null;
    }
}