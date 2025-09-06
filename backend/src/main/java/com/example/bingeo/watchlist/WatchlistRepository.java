package com.example.bingeo.watchlist;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    // ============= BASIC CRUD OPERATIONS =============

    /**
     * Find all watchlist entries for a user
     */
    List<Watchlist> findByUserId(String userId);

    /**
     * Find all watchlist entries for a user with pagination
     */
    List<Watchlist> findByUserId(String userId, Pageable pageable);

    /**
     * Find all watchlist entries for a user ordered by most recently added
     */
    List<Watchlist> findByUserIdOrderByAddedOnDesc(String userId);

    /**
     * Find all watchlist entries for a user ordered by most recently added with pagination
     */
    List<Watchlist> findByUserIdOrderByAddedOnDesc(String userId, Pageable pageable);

    /**
     * Find specific watchlist entry by user and movie
     */
    Optional<Watchlist> findByUserIdAndTconst(String userId, String tconst);

    /**
     * Check if a movie exists in user's watchlist
     */
    boolean existsByUserIdAndTconst(String userId, String tconst);

    /**
     * Delete specific watchlist entry by user and movie
     */
    @Modifying
    @Transactional
    void deleteByUserIdAndTconst(String userId, String tconst);

    /**
     * Delete all watchlist entries for a user
     */
    @Modifying
    @Transactional
    void deleteByUserId(String userId);

    // ============= STATISTICS & ANALYTICS =============

    /**
     * Count total watchlist items for a user
     */
    long countByUserId(String userId);

    /**
     * Count how many users have added a specific movie to their watchlist
     */
    long countByTconst(String tconst);

    /**
     * Find all users who have a specific movie in their watchlist
     */
    List<Watchlist> findByTconst(String tconst);

    /**
     * Get recent watchlist additions for a user (last N days)
     */
    @Query("SELECT w FROM Watchlist w WHERE w.userId = :userId AND w.addedOn >= :since ORDER BY w.addedOn DESC")
    List<Watchlist> findRecentAdditions(@Param("userId") String userId, @Param("since") LocalDateTime since);

    /**
     * Get most popular movies in watchlists (top N movies by watchlist count)
     */
    @Query("SELECT w.tconst, COUNT(w) as count FROM Watchlist w GROUP BY w.tconst ORDER BY count DESC")
    List<Object[]> findMostWatchlistedMovies(Pageable pageable);

    // ============= BULK OPERATIONS =============

    /**
     * Find watchlist entries for multiple movies (batch operation)
     */
    @Query("SELECT w FROM Watchlist w WHERE w.userId = :userId AND w.tconst IN :tconsts")
    List<Watchlist> findByUserIdAndTconstIn(@Param("userId") String userId, @Param("tconsts") List<String> tconsts);

    /**
     * Get all movie IDs in user's watchlist (for efficient checking)
     */
    @Query("SELECT w.tconst FROM Watchlist w WHERE w.userId = :userId")
    List<String> findTconstsByUserId(@Param("userId") String userId);

    /**
     * Delete multiple watchlist entries by movie IDs
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Watchlist w WHERE w.userId = :userId AND w.tconst IN :tconsts")
    void deleteByUserIdAndTconstIn(@Param("userId") String userId, @Param("tconsts") List<String> tconsts);

    // ============= ADVANCED QUERIES =============

    /**
     * Find watchlist entries added within a date range
     */
    @Query("SELECT w FROM Watchlist w WHERE w.userId = :userId AND w.addedOn BETWEEN :startDate AND :endDate ORDER BY w.addedOn DESC")
    List<Watchlist> findByUserIdAndDateRange(@Param("userId") String userId,
                                             @Param("startDate") LocalDateTime startDate,
                                             @Param("endDate") LocalDateTime endDate);

    /**
     * Check if user's watchlist is empty
     */
    default boolean isWatchlistEmpty(String userId) {
        return countByUserId(userId) == 0;
    }

    /**
     * Get user's watchlist size efficiently
     */
    default int getWatchlistSize(String userId) {
        return (int) countByUserId(userId);
    }
}