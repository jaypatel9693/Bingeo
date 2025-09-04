import React, { useEffect, useState, useMemo } from "react";
import { getPopularMovies, searchMovies } from "../api/api";
import { Movie } from "../types";
import MovieCard from "../components/MovieCard";

export default function Movies() {
  const [sortBy, setSortBy] = useState<"imdb_desc" | "imdb_asc">("imdb_desc");
  const [movies, setMovies] = useState<Movie[]>([]);
  const [loading, setLoading] = useState(false);
  const [query, setQuery] = useState("");

  useEffect(() => {
    let mounted = true;
    (async () => {
      setLoading(true);
      try {
        const data =
          query.trim() === ""
            ? await getPopularMovies()
            : await searchMovies(query.trim());
        if (mounted) setMovies(data as Movie[]);
      } catch (err) {
        console.error("Failed to fetch movies:", err);
      } finally {
        if (mounted) setLoading(false);
      }
    })();
    return () => {
      mounted = false;
    };
  }, [query]);

  const sorted = useMemo(() => {
    const copy = [...movies];
    switch (sortBy) {
      case "imdb_asc":
        return copy.sort((a, b) => a.rating - b.rating);
      case "imdb_desc":
      default:
        return copy.sort((a, b) => b.rating - a.rating);
    }
  }, [movies, sortBy]);

  return (
    <div>
      <h1 className="text-2xl font-bold mb-4">Trending Movies</h1>

      {/* Search bar */}
      <input
        type="text"
        value={query}
        onChange={(e) => setQuery(e.target.value)}
        placeholder="Search movies..."
        className="border rounded px-3 py-2 mb-4 w-full text-black"
      />

      {/* Sort dropdown */}
      <select
        value={sortBy}
        onChange={(e) => setSortBy(e.target.value as "imdb_desc" | "imdb_asc")}
        className="border rounded px-2 py-1 mb-4 text-black"
      >
        <option value="imdb_desc">IMDb Rating (High → Low)</option>
        <option value="imdb_asc">IMDb Rating (Low → High)</option>
      </select>

      {/* Loading */}
      {loading && <p>Loading movies...</p>}

      {/* Movies grid */}
      <div className="grid grid-cols-2 sm:grid-cols-3 lg:grid-cols-5 gap-4">
        {sorted.map((movie) => (
          <MovieCard key={movie.id} movie={movie} />
        ))}
      </div>
    </div>
  );
}
