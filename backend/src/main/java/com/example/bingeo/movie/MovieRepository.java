package com.example.bingeo.repo;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bingeo.model.Movie;
public interface MovieRepository extends JpaRepository<Movie, Long> {}
