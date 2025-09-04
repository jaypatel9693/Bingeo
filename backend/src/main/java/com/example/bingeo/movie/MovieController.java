package com.example.bingeo.controller;
import com.example.bingeo.model.Movie;
import com.example.bingeo.repo.MovieRepository;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/movies")
public class MovieController {
  private final MovieRepository repo;
  public MovieController(MovieRepository repo){ this.repo = repo; }

  @GetMapping public List<Movie> all(){ return repo.findAll(); }

  @PostMapping public Movie create(@RequestBody Movie m){ return repo.save(m); } // (protect later)
}
