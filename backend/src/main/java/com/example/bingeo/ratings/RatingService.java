// RatingService.java - Fixed service
package com.example.bingeo.ratings;

import com.example.bingeo.movie.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RatingService {

    private final RatingRepository ratingRepository;

    // Get rating for a specific movie
    public Optional<Rating> getMovieRating(String tconst) {
        return ratingRepository.findByTconst(tconst);
    }

    // Get all ratings
    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    // Get top rated movies
    public List<Rating> getTopRatedMovies() {
        return ratingRepository.findTopRatedMovies();
    }

    // Get movies with minimum vote count (popular movies)
    public List<Rating> getPopularMovies(Integer minVotes) {
        return ratingRepository.findMoviesWithMinVotes(minVotes != null ? minVotes : 1000);
    }

    // Get highly rated movies (above certain rating)
    public List<Rating> getHighlyRatedMovies(Double minRating) {
        return ratingRepository.findMoviesAboveRating(minRating != null ? minRating : 7.0);
    }

    // Update or create rating for a movie
    public Rating updateMovieRating(String tconst, Double averageRating, Integer numVotes) {
        Rating rating = ratingRepository.findByTconst(tconst)
                .orElse(Rating.builder()
                        .movie(Movie.builder().tconst(tconst).build())
                        .build());


        rating.setAverageRating(averageRating);
        rating.setNumVotes(numVotes);

        return ratingRepository.save(rating);
    }
}