package com.example.bingeo.rec;

import com.example.bingeo.model.Recommendation;  // ✅ updated import
import com.example.bingeo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Long> {
    List<Recommendation> findByUserOrderByCreatedAtDesc(User user);
}
