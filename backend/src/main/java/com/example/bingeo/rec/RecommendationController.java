package com.example.bingeo.rec;

import com.example.bingeo.model.Recommendation;
import com.example.bingeo.model.User;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recs;

    public RecommendationController(RecommendationService recs) {
        this.recs = recs;
    }

    @GetMapping
    public List<Recommendation> list( User user) {
        return recs.list(user);
    }

    @PostMapping
    public Recommendation add( User user, @RequestBody Recommendation r) {
        return recs.add(user, r);
    }

    @PutMapping("/{id}")
    public Recommendation update(
            User user,
            @PathVariable Long id,
            @RequestBody Recommendation r
    ) {
        return recs.update(user, id, r);
    }

    @DeleteMapping("/{id}")
    public void delete( User user, @PathVariable Long id) {
        recs.delete(user, id);
    }
}
