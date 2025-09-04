import React, { useEffect, useState } from "react";
import { getPopularMovies } from "../api";
import { Movie } from "../types";
import MovieCard from "../components/MovieCard";

export default function Home() {
  const [movies, setMovies] = useState<Movie[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    let mounted = true;
    const fetchMovies = async () => {
      try {
        const data = await getPopularMovies();
        if (mounted) setMovies(data as Movie[]);
      } catch (error) {
        console.error("Error fetching movies:", error);
      } finally {
        if (mounted) setLoading(false);
      }
    };
    fetchMovies();

    return () => {
      mounted = false;
    };
  }, []);

  if (loading) return <p className="text-white text-center">Loading...</p>;

  return (
    <div className="p-6">
      <h1 className="text-3xl font-bold text-white mb-6">🔥 Popular Movies</h1>
      <div className="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 gap-6">
        {movies.map((movie) => (
          <MovieCard key={movie.id} movie={movie} />
        ))}
      </div>
    </div>
  );
}
