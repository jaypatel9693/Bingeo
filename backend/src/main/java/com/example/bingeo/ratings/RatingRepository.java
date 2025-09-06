// RatingRepository.java - Fixed repository
package com.example.bingeo.ratings;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, String> {

    // Find rating by movie tconst
    Optional<Rating> findByUserIdAndTconst(String userId, String tconst);
    // Find top rated movies
    @Query("SELECT r FROM Rating r WHERE r.averageRating IS NOT NULL ORDER BY r.averageRating DESC")
    List<Rating> findTopRatedMovies();

    // Find movies with minimum vote count
    @Query("SELECT r FROM Rating r WHERE r.numVotes >= :minVotes ORDER BY r.averageRating DESC")
    List<Rating> findMoviesWithMinVotes(@Param("minVotes") Integer minVotes);

    // Find movies with rating above threshold
    @Query("SELECT r FROM Rating r WHERE r.averageRating >= :minRating ORDER BY r.averageRating DESC")
    List<Rating> findMoviesAboveRating(@Param("minRating") Double minRating);

    Optional<Rating> findByTconst(String tconst);

    List<Rating> findByUserIdOrderByRatedAtDesc(String userId, PageRequest of);
}

// ===================================