import React from "react";
import { Movie } from "../types";

interface MovieCardProps {
  movie: Movie;
  isInList?: boolean;          // optional
  onAdd?: () => void;          // optional
  onRemove?: () => void;       // optional
  onRate?: (rating: number) => void; // optional
  rating?: number;             // optional
}

export default function MovieCard({
  movie,
  isInList,
  onAdd,
  onRemove,
  onRate,
  rating,
}: MovieCardProps) {
  return (
    <div className="bg-gray-900 text-white rounded-lg shadow-md p-4">
      <img
        src={movie.poster}
        alt={movie.title}
        className="w-full h-64 object-cover rounded mb-4"
      />
      <h2 className="text-lg font-bold">{movie.title}</h2>
      <p className="text-gray-400">IMDb: {movie.rating}</p>

      {rating !== undefined && (
        <p className="text-yellow-400">⭐ Your Rating: {rating}/10</p>
      )}

      <div className="flex gap-2 mt-3">
        {!isInList && onAdd && (
          <button
            onClick={onAdd}
            className="px-3 py-1 bg-blue-600 rounded hover:bg-blue-700"
          >
            + Add
          </button>
        )}
        {isInList && onRemove && (
          <button
            onClick={onRemove}
            className="px-3 py-1 bg-red-600 rounded hover:bg-red-700"
          >
            Remove
          </button>
        )}
      </div>

      {onRate && (
        <div className="mt-3">
          <label className="text-sm">Rate:</label>
          <select
            value={rating ?? ""}
            onChange={(e) => onRate(Number(e.target.value))}
            className="ml-2 px-2 py-1 bg-gray-800 rounded"
          >
            <option value="">--</option>
            {[...Array(10)].map((_, i) => (
              <option key={i + 1} value={i + 1}>
                {i + 1}
              </option>
            ))}
          </select>
        </div>
      )}
    </div>
  );
}
