// RatingController.java - Fixed controller
package com.example.bingeo.ratings;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;

    // Get rating for a specific movie
    @GetMapping("/movie/{tconst}")
    public Optional<Rating> getMovieRating(@PathVariable String tconst) {
        return ratingService.getMovieRating(tconst);
    }

    // Get all ratings
    @GetMapping
    public List<Rating> getAllRatings() {
        return ratingService.getAllRatings();
    }

    // Get top rated movies
    @GetMapping("/top")
    public List<Rating> getTopRatedMovies() {
        return ratingService.getTopRatedMovies();
    }

    // Get popular movies (with minimum votes)
    @GetMapping("/popular")
    public List<Rating> getPopularMovies(@RequestParam(required = false) Integer minVotes) {
        return ratingService.getPopularMovies(minVotes);
    }

    // Get highly rated movies
    @GetMapping("/highly-rated")
    public List<Rating> getHighlyRatedMovies(@RequestParam(required = false) Double minRating) {
        return ratingService.getHighlyRatedMovies(minRating);
    }

    // Update movie rating (admin endpoint)
    @PutMapping("/movie/{tconst}")
    public Rating updateMovieRating(
            @PathVariable String tconst,
            @RequestParam Double averageRating,
            @RequestParam Integer numVotes) {
        return ratingService.updateMovieRating(tconst, averageRating, numVotes);
    }
}