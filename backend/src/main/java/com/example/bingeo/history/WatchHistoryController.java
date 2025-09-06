package com.example.bingeo.history;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/watch-history")
public class WatchHistoryController {

    @Autowired
    private WatchHistoryService watchHistoryService;

    @GetMapping
    public List<WatchHistory> getHistory(@RequestParam String userId) {
        return watchHistoryService.getHistory(userId);
    }

    @PostMapping
    public WatchHistory add(@RequestBody Map<String, String> body) {
        return watchHistoryService.addHistory(body.get("userId"), body.get("tconst"));
    }
}
