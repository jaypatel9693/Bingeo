package com.example.bingeo.history;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class WatchHistoryService {

    @Autowired
    private WatchHistoryRepository watchHistoryRepository;

    public List<WatchHistory> getHistory(String userId) {
        return watchHistoryRepository.findByUserId(userId);
    }

    public WatchHistory addHistory(String userId, String tconst) {
        WatchHistory watchHistory = new WatchHistory();
        watchHistory.setUserId(userId);
        watchHistory.setIconStr(tconst); // Use setIconStr since that's your actual field name
        watchHistory.setWatchedAt(LocalDateTime.now());

        return watchHistoryRepository.save(watchHistory);
    }
}