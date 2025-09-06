package com.example.bingeo.watchlist;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/watchlist")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistService watchlistService;

    @GetMapping
    public List<Watchlist> getWatchlist(@RequestParam String userId) {
        return watchlistService.getwatchlist(userId);
    }

    @PostMapping
    public Watchlist add(@RequestBody WatchlistRequest req) {
        return watchlistService.add(req.getUserId(), req.getTconst());
    }

    @DeleteMapping
    public void remove(@RequestParam String userId, @RequestParam String tconst) {
        watchlistService.remove(userId, tconst);
    }

    @Data
    public static class WatchlistRequest {
        private String userId;
        private String tconst;
    }
}