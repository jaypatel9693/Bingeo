package com.example.bingeo.watchlist;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchlistService {

    private final WatchlistRepository watchlistRepository;

    public List<Watchlist> getUserWatchlist(String userId) {
        return watchlistRepository.findByUserId(userId);
    }

    public Watchlist addMovie(String userId, String tconst) {
        Watchlist w = Watchlist.builder()
                .userId(userId)
                .tconst(tconst)
                .addedOn(LocalDateTime.now())
                .build();
        return watchlistRepository.save(w);
    }

    public void removeMovie(String userId, String tconst) {
        watchlistRepository.deleteByUserIdAndTconst(userId, tconst);
    }

    public List<Watchlist> getwatchlist(String userId) {
        return List.of();
    }

    public Watchlist add(String userId, String tconst) {
        return null;
    }

    public void remove(String userId, String tconst) {
    }
}