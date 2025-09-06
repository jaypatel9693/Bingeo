// src/main/java/com/example/bingeo/movie/MovieService.java
package com.example.bingeo.movie;

import com.example.bingeo.ratings.Rating;
import com.example.bingeo.ratings.RatingRepository;
import com.example.bingeo.watchlist.Watchlist;
import com.example.bingeo.watchlist.WatchlistRepository;
import com.example.bingeo.history.WatchHistory;
import com.example.bingeo.history.WatchHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepo;

    // Optional repositories for integration - will be null if beans don't exist
    private final WatchlistRepository watchlistRepo;
    private final RatingRepository ratingRepo;
    private final WatchHistoryRepository historyRepo;

    /**
     * Search movies by title with rating data
     */
    public List<MovieDtos.MovieCard> search(String title, int limit) {
        return search(title, limit, null);
    }

    /**
     * Search movies by title with rating data and optional user context
     */
    public List<MovieDtos.MovieCard> search(String title, int limit, String userId) {
        if (title == null || title.trim().isEmpty()) {
            return List.of();
        }

        List<Movie> movies = movieRepo.searchWithRating(title.trim(), PageRequest.of(0, Math.max(1, limit)));

        // Get user's watchlist IDs if user context provided
        Set<String> watchlistIds = getUserWatchlistIds(userId);

        return movies.stream()
                .map(movie -> toMovieCard(movie, watchlistIds))
                .toList();
    }

    /**
     * Get top rated movies/shows by type
     */
    public List<MovieDtos.MovieCard> topRated(String type, int limit) {
        return topRated(type, limit, null);
    }

    /**
     * Get top rated movies/shows by type with optional user context
     */
    public List<MovieDtos.MovieCard> topRated(String type, int limit, String userId) {
        List<Movie> movies = movieRepo.topRated(type, PageRequest.of(0, Math.max(1, limit)));
        Set<String> watchlistIds = getUserWatchlistIds(userId);

        return movies.stream()
                .map(movie -> toMovieCard(movie, watchlistIds))
                .toList();
    }

    /**
     * Get Top 250 movies using weighted rating formula
     */
    public List<MovieDtos.MovieCard> getTop250(String type, int limit) {
        return getTop250(type, limit, null);
    }

    /**
     * Get Top 250 movies using weighted rating formula with optional user context
     */
    public List<MovieDtos.MovieCard> getTop250(String type, int limit, String userId) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Map<String, Object>> results = movieRepo.getTop250(type, pageable);

        // Fetch user's watchlist IDs for marking
        Set<String> watchlistIds = getUserWatchlistIds(userId);

        return results.stream()
                .map(result -> new MovieDtos.MovieCard(
                        (String) result.get("tconst"),
                        (String) result.get("primary_title"),
                        (Integer) result.get("start_year"),
                        (String) result.get("genres"),
                        (Double) result.get("average_rating"),
                        (Integer) result.get("num_votes"),
                        watchlistIds.contains(result.get("tconst"))
                ))
                .toList();
    }

    /**
     * Get single movie by IMDb ID
     */
    public Optional<MovieDtos.MovieDetails> findById(String tconst) {
        return findById(tconst, null);
    }

    /**
     * Get single movie by IMDb ID with optional user integration
     */
    public Optional<MovieDtos.MovieDetails> findById(String tconst, String userId) {
        if (tconst == null || tconst.trim().isEmpty()) {
            return Optional.empty();
        }

        return movieRepo.findById(tconst.trim())
                .map(movie -> {
                    MovieDtos.MovieDetails details = new MovieDtos.MovieDetails(movie);

                    // Add user-specific data if user context provided
                    if (userId != null) {
                        // Check if in watchlist
                        if (watchlistRepo != null) {
                            boolean inWatchlist = watchlistRepo.findByUserIdAndTconst(userId, tconst).isPresent();
                            details.setInWatchlist(inWatchlist);
                        }

                        // Get user rating
                        if (ratingRepo != null) {
                            ratingRepo.findByUserIdAndTconst(userId, tconst)
                                    .ifPresent(r -> details.setUserRating(r.getAverageRating()));
                        }

                        // Check if watched
                        if (historyRepo != null) {
                            boolean watched = historyRepo.findByUserIdAndTconst(userId, tconst).isPresent();
                            details.setWatched(watched);
                        }
                    }

                    return details;
                });
    }

    /**
     * Get all movies (paginated) - useful for admin/bulk operations
     */
    public List<MovieDtos.MovieCard> findAll(int page, int size) {
        return findAll(page, size, null);
    }

    /**
     * Get all movies (paginated) with optional user context
     */
    public List<MovieDtos.MovieCard> findAll(int page, int size, String userId) {
        List<Movie> movies = movieRepo.findAll(PageRequest.of(page, Math.max(1, size))).getContent();
        Set<String> watchlistIds = getUserWatchlistIds(userId);

        return movies.stream()
                .map(movie -> toMovieCard(movie, watchlistIds))
                .toList();
    }

    /**
     * Get chart data for analytics
     */
    public List<MovieDtos.ChartData> getGenreStats() {
        return movieRepo.countByGenre()
                .stream()
                .map(row -> new MovieDtos.ChartData(
                        (String) row[0],    // genre
                        (Long) row[1],      // count
                        null                // no average for count data
                ))
                .toList();
    }

    public List<MovieDtos.ChartData> getYearStats() {
        return movieRepo.countByYear()
                .stream()
                .map(row -> new MovieDtos.ChartData(
                        String.valueOf(row[0]), // year as string
                        (Long) row[1],          // count
                        null
                ))
                .toList();
    }

    public List<MovieDtos.ChartData> getRatingByGenreStats() {
        return movieRepo.avgRatingByGenre()
                .stream()
                .map(row -> new MovieDtos.ChartData(
                        (String) row[0],    // genre
                        null,               // no count for average data
                        (Double) row[1]     // average rating
                ))
                .toList();
    }

    /**
     * Check if movie exists
     */
    public boolean exists(String tconst) {
        return tconst != null && !tconst.trim().isEmpty() && movieRepo.existsById(tconst.trim());
    }

    /**
     * Get total count for pagination
     */
    public long getTotalCount() {
        return movieRepo.count();
    }

    // ============= HELPER METHODS =============

    /**
     * Get user's watchlist IDs safely (returns empty set if not available)
     */
    private Set<String> getUserWatchlistIds(String userId) {
        if (userId == null || watchlistRepo == null) {
            return Set.of();
        }

        try {
            return watchlistRepo.findByUserId(userId).stream()
                    .map(Watchlist::getTconst)
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            // Log error if needed, return empty set
            return Set.of();
        }
    }

    /**
     * Map Movie entity to MovieCard DTO with optional watchlist integration
     */
    private MovieDtos.MovieCard toMovieCard(Movie movie, Set<String> watchlistIds) {
        boolean inWatchlist = watchlistIds != null && watchlistIds.contains(movie.getTconst());

        return new MovieDtos.MovieCard(
                movie.getTconst(),
                movie.getPrimaryTitle(),
                movie.getStartYear(),
                movie.getGenres(),
                movie.getRating() != null ? movie.getRating().getAverageRating() : null,
                movie.getRating() != null ? movie.getRating().getNumVotes() : null,
                inWatchlist
        );
    }

    // ============= USER-SPECIFIC METHODS =============

    /**
     * Get user's watchlist movies
     */
    public List<MovieDtos.MovieCard> getUserWatchlist(String userId, int page, int size) {
        if (userId == null || watchlistRepo == null) {
            return List.of();
        }

        List<Watchlist> watchlistItems = watchlistRepo.findByUserId(userId, PageRequest.of(page, size));
        List<String> tconsts = watchlistItems.stream().map(Watchlist::getTconst).toList();

        if (tconsts.isEmpty()) {
            return List.of();
        }

        List<Movie> movies = movieRepo.findAllById(tconsts);
        Set<String> watchlistIds = Set.copyOf(tconsts); // All are in watchlist

        return movies.stream()
                .map(movie -> toMovieCard(movie, watchlistIds))
                .toList();
    }

    /**
     * Get user's watch history
     */
    public List<MovieDtos.MovieCard> getUserWatchHistory(String userId, int page, int size) {
        if (userId == null || historyRepo == null) {
            return List.of();
        }

        List<WatchHistory> historyItems = historyRepo.findByUserIdOrderBywatchedAtDesc(userId, PageRequest.of(page, size));
        List<String> tconsts = historyItems.stream().map(WatchHistory::getTconst).toList();

        if (tconsts.isEmpty()) {
            return List.of();
        }

        List<Movie> movies = movieRepo.findAllById(tconsts);
        Set<String> watchlistIds = getUserWatchlistIds(userId);

        return movies.stream()
                .map(movie -> toMovieCard(movie, watchlistIds))
                .toList();
    }

    /**
     * Get user's rated movies
     */
    public List<MovieDtos.MovieCard> getUserRatedMovies(String userId, int page, int size) {
        if (userId == null || ratingRepo == null) {
            return List.of();
        }

        List<Rating> ratings = ratingRepo.findByUserIdOrderByRatedAtDesc(userId, PageRequest.of(page, size));
        List<Object> tconsts = Collections.singletonList(ratings.stream().map(Rating::getTconst).toList());

        if (tconsts.isEmpty()) {
            return List.of();
        }

        List<Movie> movies = movieRepo.findAllById(tconsts);
        Set<String> watchlistIds = getUserWatchlistIds(userId);

        return movies.stream()
                .map(movie -> toMovieCard(movie, watchlistIds))
                .toList();
    }
}