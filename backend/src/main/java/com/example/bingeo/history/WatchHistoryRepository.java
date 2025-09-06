package com.example.bingeo.history;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {

    // Find all watch history entries for a specific user
    List<WatchHistory> findByUserId(String userId);

    List<WatchHistory> findByUserIdOrderBywatchedAtDesc(String userId, PageRequest of);

    // You can add more custom query methods here as needed
    // For example:
    // List<WatchHistory> findByUserIdOrderByWatchedAtDesc(String userId);
}