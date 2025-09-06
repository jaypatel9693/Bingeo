package com.example.bingeo.watchlist;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "watchlists",
        uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "tconst"}),
        indexes = {
                @Index(name = "idx_watchlist_user", columnList = "userId"),
                @Index(name = "idx_watchlist_tconst", columnList = "tconst"),
                @Index(name = "idx_watchlist_added_on", columnList = "addedOn")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Watchlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String userId;  // will be linked to User later

    @Column(nullable = false, length = 20)
    private String tconst;  // IMDb movie ID (e.g., "tt0111161")

    @Column(nullable = false)
    @Builder.Default
    private LocalDateTime addedOn = LocalDateTime.now();

    // Custom constructor for backward compatibility
    public Watchlist(String userId, String tconst) {
        this.userId = userId;
        this.tconst = tconst;
        this.addedOn = LocalDateTime.now();
    }

    // Utility method for setting creation time
    @PrePersist
    protected void onCreate() {
        if (addedOn == null) {
            addedOn = LocalDateTime.now();
        }
    }

    // Utility methods for business logic
    public boolean belongsToUser(String userId) {
        return this.userId != null && this.userId.equals(userId);
    }

    public boolean isForMovie(String tconst) {
        return this.tconst != null && this.tconst.equals(tconst);
    }

    // Override toString for better debugging
    @Override
    public String toString() {
        return "Watchlist{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", tconst='" + tconst + '\'' +
                ", addedOn=" + addedOn +
                '}';
    }
}