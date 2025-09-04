import React, { useEffect, useState } from "react";
import { getMyList } from "../api/api";
import { Movie } from "../types";
import MovieCard from "../components/MovieCard";

const MyList: React.FC = () => {
  const [movies, setMovies] = useState<Movie[]>([]);

  useEffect(() => {
    const fetchList = async () => {
      try {
        const data = await getMyList();
        setMovies(data);
      } catch (err) {
        console.error("Failed to fetch my list:", err);
      }
    };

    fetchList();
  }, []);

  return (
    <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
      {movies.map((movie) => (
        <MovieCard key={movie.id} movie={movie} />
      ))}
    </div>
  );
};

export default MyList;
