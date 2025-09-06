package com.example.bingeo.movie;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MovieRepository extends JpaRepository<Movie, String> {

    // Basic search by title
    List<Movie> findByPrimaryTitleContainingIgnoreCase(String title);

    // Search with rating data (fetch join to avoid N+1 problem)
    @Query("SELECT m FROM Movie m LEFT JOIN FETCH m.rating r WHERE LOWER(m.primaryTitle) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Movie> searchWithRating(@Param("title") String title, Pageable pageable);

    // Top rated movies/shows
    @Query("SELECT m FROM Movie m JOIN m.rating r WHERE (:type IS NULL OR m.titleType = :type) ORDER BY r.averageRating DESC, r.numVotes DESC")
    List<Movie> topRated(@Param("type") String titleType, Pageable pageable);

    // Chart data: Count by genre
    @Query("SELECT m.genres, COUNT(m) FROM Movie m WHERE m.genres IS NOT NULL GROUP BY m.genres ORDER BY COUNT(m) DESC")
    List<Object[]> countByGenre();

    // Chart data: Count by year
    @Query("SELECT m.startYear, COUNT(m) FROM Movie m WHERE m.startYear IS NOT NULL GROUP BY m.startYear ORDER BY m.startYear DESC")
    List<Object[]> countByYear();

    // Chart data: Average rating by genre
    @Query("SELECT m.genres, AVG(r.averageRating) FROM Movie m JOIN m.rating r WHERE m.genres IS NOT NULL AND r.averageRating IS NOT NULL GROUP BY m.genres ORDER BY AVG(r.averageRating) DESC")
    List<Object[]> avgRatingByGenre();

    // Add this to MovieRepository.java
    //@Query("SELECT new com.example.bingeo.movie.MovieDtos.MovieCard(t.tconst, t.primaryTitle, t.startYear, t.genres, t.rating, t.numVotes) FROM TopMovie t WHERE (:type IS NULL OR t.titleType = :type) ORDER BY t.weightedRating DESC")
    //List<MovieDtos.MovieCard> getTop250(@Param("type") String titleType, Pageable pageable);

    @Query(value = "SELECT * FROM titles_mv " +
            "WHERE (:type IS NULL OR title_type = :type) " +
            "ORDER BY wn DESC, num_votes DESC",
            nativeQuery = true)
    List<Map<String, Object>> getTop250(@Param("type") String type, Pageable pageable);

    List<Movie> findAllById(List<Object> tconsts);
}
