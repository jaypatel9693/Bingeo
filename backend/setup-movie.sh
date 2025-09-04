#!/bin/bash
set -e

echo "🎬 Setting up Movie entity in backend..."

# Base path
BASE_PATH=src/main/java/com/example/bingeo

# Ensure rec directory exists
mkdir -p $BASE_PATH/movie

################################
# Movie.java
################################
cat > $BASE_PATH/movie/Movie.java <<'EOL'
package com.example.bingeo.movie;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String genre;
    private int releaseYear;
    private double rating;
}
EOL

################################
# MovieRepository.java
################################
cat > $BASE_PATH/movie/MovieRepository.java <<'EOL'
package com.example.bingeo.movie;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
EOL

################################
# MovieService.java
################################
cat > $BASE_PATH/movie/MovieService.java <<'EOL'
package com.example.bingeo.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie addMovie(Movie movie) {
        return movieRepository.save(movie);
    }
}
EOL

################################
# MovieController.java
################################
cat > $BASE_PATH/movie/MovieController.java <<'EOL'
package com.example.bingeo.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping
    public List<Movie> getMovies() {
        return movieService.getAllMovies();
    }

    @PostMapping
    public Movie createMovie(@RequestBody Movie movie) {
        return movieService.addMovie(movie);
    }
}
EOL

echo "✅ Movie entity, repo, service, and controller created!"
