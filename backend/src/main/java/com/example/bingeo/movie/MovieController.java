// src/main/java/com/example/bingeo/movie/MovieController.java
package com.example.bingeo.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    // GET /api/movies/search?title=Inception&limit=10&userId=user123
    @GetMapping("/search")
    public List<MovieDtos.MovieCard> search(
            @RequestParam String title,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(required = false) String userId) {
        return movieService.search(title, limit, userId);
    }

    // GET /api/movies/top-rated?type=movie&limit=50&userId=user123
    @GetMapping("/top-rated")
    public List<MovieDtos.MovieCard> topRated(
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "50") int limit,
            @RequestParam(required = false) String userId) {
        return movieService.topRated(type, limit, userId);
    }

    // GET /api/movies/top-250?type=movie&limit=250&userId=user123
    @GetMapping("/top-250")
    public List<MovieDtos.MovieCard> getTop250(
            @RequestParam(required = false) String type,
            @RequestParam(defaultValue = "250") int limit,
            @RequestParam(required = false) String userId) {
        return movieService.getTop250(type, limit, userId);
    }

    // GET /api/movies/{tconst}?userId=user123
    @GetMapping("/{tconst}")
    public ResponseEntity<MovieDtos.MovieDetails> getMovieById(
            @PathVariable String tconst,
            @RequestParam(required = false) String userId) {
        Optional<MovieDtos.MovieDetails> movie = movieService.findById(tconst, userId);
        return movie.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/movies?page=0&size=20&userId=user123
    @GetMapping
    public List<MovieDtos.MovieCard> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String userId) {
        return movieService.findAll(page, size, userId);
    }

    // ============= USER-SPECIFIC ENDPOINTS =============

    // GET /api/movies/user/{userId}/watchlist?page=0&size=20
    @GetMapping("/user/{userId}/watchlist")
    public List<MovieDtos.MovieCard> getUserWatchlist(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return movieService.getUserWatchlist(userId, page, size);
    }

    // GET /api/movies/user/{userId}/history?page=0&size=20
    @GetMapping("/user/{userId}/history")
    public List<MovieDtos.MovieCard> getUserWatchHistory(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return movieService.getUserWatchHistory(userId, page, size);
    }

    // GET /api/movies/user/{userId}/rated?page=0&size=20
    @GetMapping("/user/{userId}/rated")
    public List<MovieDtos.MovieCard> getUserRatedMovies(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return movieService.getUserRatedMovies(userId, page, size);
    }

    // ============= ANALYTICS ENDPOINTS (unchanged) =============

    // GET /api/movies/charts/genres
    @GetMapping("/charts/genres")
    public List<MovieDtos.ChartData> getGenreStats() {
        return movieService.getGenreStats();
    }

    // GET /api/movies/charts/years
    @GetMapping("/charts/years")
    public List<MovieDtos.ChartData> getYearStats() {
        return movieService.getYearStats();
    }

    // GET /api/movies/charts/ratings-by-genre
    @GetMapping("/charts/ratings-by-genre")
    public List<MovieDtos.ChartData> getRatingByGenreStats() {
        return movieService.getRatingByGenreStats();
    }

    // ============= UTILITY ENDPOINTS =============

    // GET /api/movies/exists/{tconst}
    @GetMapping("/exists/{tconst}")
    public boolean movieExists(@PathVariable String tconst) {
        return movieService.exists(tconst);
    }

    // GET /api/movies/count
    @GetMapping("/count")
    public long getTotalMovieCount() {
        return movieService.getTotalCount();
    }
}