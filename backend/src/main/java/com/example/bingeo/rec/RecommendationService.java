package com.example.bingeo.rec;

import com.example.bingeo.model.Recommendation;
import com.example.bingeo.model.User;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RecommendationService {

    private final RecommendationRepository repo;

    public RecommendationService(RecommendationRepository repo) {
        this.repo = repo;
    }

    public List<Recommendation> list(User user) {
        return repo.findByUserOrderByCreatedAtDesc(user);
    }

    public Recommendation add(User user, Recommendation rec) {
        rec.setUser(user);
        return repo.save(rec);
    }

    public Recommendation update(User user, Long id, Recommendation updated) {
        Recommendation existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Recommendation not found"));
        if (!existing.getUser().equals(user)) {
            throw new RuntimeException("Not authorized");
        }
        existing.setMovieTitle(updated.getMovieTitle());
        existing.setDescription(updated.getDescription());
        existing.setStatus(updated.getStatus());
        existing.setTmdbId(updated.getTmdbId());
        return repo.save(existing);
    }

    public void delete(User user, Long id) {
        Recommendation existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Recommendation not found"));
        if (!existing.getUser().equals(user)) {
            throw new RuntimeException("Not authorized");
        }
        repo.delete(existing);
    }
}
