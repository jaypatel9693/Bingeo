package com.example.bingeo.history;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "watch_history")
public class WatchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String iconStr;

    @Column(name = "watched_on") // Make sure this matches your database column name
    private LocalDateTime watchedAt; // Only declare this ONCE

    // Constructors, getters, and setters
    public WatchHistory() {}

    public WatchHistory(String userId, String tconst) {
        this.userId = userId;
        this.tconst = tconst;
        this.watchedAt = LocalDateTime.now();
    }

    // Add your getters and setters here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIconStr() {
        return iconStr;
    }

    public void setIconStr(String iconStr) {
        this.iconStr = iconStr;
    }

    public LocalDateTime getWatchedAt() {
        return watchedAt;
    }

    public void setWatchedAt(LocalDateTime watchedAt) {
        this.watchedAt = watchedAt;
    }
}