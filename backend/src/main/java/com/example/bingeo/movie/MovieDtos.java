package com.example.bingeo.movie;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class MovieDtos {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MovieCard {
        private String tconst;
        private String title;
        private Integer year;
        private String genres;
        private Double averageRating;  // IMDb average rating (decimal, e.g., 8.6)
        private Integer votes;
        private String titleType;

        // NEW FIELDS for user integration
        private Boolean inWatchlist;   // true if user has added to watchlist
        private Boolean watched;       // true if user has watched it
        private Integer userRating;    // personal user rating 1-10 (optional)

        // Constructor for JPQL queries (backward compatibility)
        public MovieCard(String tconst, String title, Integer year, String genres, Double averageRating, Integer votes) {
            this.tconst = tconst;
            this.title = title;
            this.year = year;
            this.genres = genres;
            this.averageRating = averageRating;
            this.votes = votes;
            this.inWatchlist = false;
            this.watched = false;
            this.userRating = null;
        }

        // Constructor with watchlist integration
        public MovieCard(String tconst, String title, Integer year, String genres,
                         Double averageRating, Integer votes, Boolean inWatchlist) {
            this.tconst = tconst;
            this.title = title;
            this.year = year;
            this.genres = genres;
            this.averageRating = averageRating;
            this.votes = votes;
            this.inWatchlist = inWatchlist;
            this.watched = false;
            this.userRating = null;
        }

        // Full constructor with all user integration fields
        public MovieCard(String tconst, String title, Integer year, String genres,
                         Double averageRating, Integer votes, Boolean inWatchlist,
                         Boolean watched, Integer userRating) {
            this.tconst = tconst;
            this.title = title;
            this.year = year;
            this.genres = genres;
            this.averageRating = averageRating;
            this.votes = votes;
            this.inWatchlist = inWatchlist;
            this.watched = watched;
            this.userRating = userRating;
        }

        // Convenience constructor for Movie entity
        public MovieCard(Movie movie) {
            this.tconst = movie.getTconst();
            this.title = movie.getPrimaryTitle();
            this.year = movie.getStartYear();
            this.genres = movie.getGenres();
            this.titleType = movie.getTitleType();

            if (movie.getRating() != null) {
                this.averageRating = movie.getRating().getAverageRating();
                this.votes = movie.getRating().getNumVotes();
            }

            this.inWatchlist = false;
            this.watched = false;
            this.userRating = null;
        }

        // Convenience constructor for Movie entity with user context
        public MovieCard(Movie movie, Boolean inWatchlist, Boolean watched, Integer userRating) {
            this(movie);
            this.inWatchlist = inWatchlist != null ? inWatchlist : false;
            this.watched = watched != null ? watched : false;
            this.userRating = userRating;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MovieDetails {
        private String tconst;
        private String title;
        private Integer year;
        private String genres;
        private String titleType;
        private Double averageRating;  // IMDb average rating
        private Integer votes;
        private String description;

        // NEW FIELDS for user integration
        private Boolean inWatchlist;
        private Boolean watched;
        private Integer userRating;    // personal user rating 1-10

        // Constructor from Movie entity
        public MovieDetails(Movie movie) {
            this.tconst = movie.getTconst();
            this.title = movie.getPrimaryTitle();
            this.year = movie.getStartYear();
            this.genres = movie.getGenres();
            this.titleType = movie.getTitleType();

            if (movie.getRating() != null) {
                this.averageRating = movie.getRating().getAverageRating();
                this.votes = movie.getRating().getNumVotes();
            }

            this.inWatchlist = false;
            this.watched = false;
            this.userRating = null;
        }

        // Constructor from Movie entity with user context
        public MovieDetails(Movie movie, Boolean inWatchlist, Boolean watched, Integer userRating) {
            this(movie);
            this.inWatchlist = inWatchlist != null ? inWatchlist : false;
            this.watched = watched != null ? watched : false;
            this.userRating = userRating;
        }

        // Convenience setters
        public void setInWatchlist(Boolean inWatchlist) {
            this.inWatchlist = inWatchlist != null ? inWatchlist : false;
        }

        public void setWatched(Boolean watched) {
            this.watched = watched != null ? watched : false;
        }

        public void setUserRating(Integer userRating) {
            this.userRating = userRating;
        }

        public void setUserRating(Double averageRating) {
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChartData {
        private String label;
        private Long count;
        private Double average;

        public ChartData(String label, Long count) {
            this.label = label;
            this.count = count;
            this.average = null;
        }

        public ChartData(String label, Double average) {
            this.label = label;
            this.count = null;
            this.average = average;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserStats {
        private Long watchlistCount;
        private Long watchedCount;
        private Long ratedCount;
        private Double averageUserRating;
        private String favoriteGenre;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MovieSummary {
        private String tconst;
        private String title;
        private Integer year;
        private Double averageRating;

        public MovieSummary(Movie movie) {
            this.tconst = movie.getTconst();
            this.title = movie.getPrimaryTitle();
            this.year = movie.getStartYear();
            this.averageRating = movie.getRating() != null ? movie.getRating().getAverageRating() : null;
        }
    }
}
