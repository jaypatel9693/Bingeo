import axios from "axios";
import { Movie } from "../types";

const API_BASE = "http://localhost:8080/api/movies"; // adjust if your backend URL differs

export async function getPopularMovies(): Promise<Movie[]> {
  const response = await axios.get(`${API_BASE}/popular`);
  return response.data;
}

export async function searchMovies(query: string): Promise<Movie[]> {
  const response = await axios.get(`${API_BASE}/search`, {
    params: { q: query },
  });
  return response.data;
}

export async function getMyList(): Promise<Movie[]> {
  const token = localStorage.getItem("token");
  const response = await axios.get(`${API_BASE}/mylist`, {
    headers: { Authorization: `Bearer ${token}` },
  });
  return response.data;
}

export async function rateMovie(movieId: number, rating: number): Promise<void> {
  const token = localStorage.getItem("token");
  await axios.post(
    `${API_BASE}/${movieId}/rate`,
    { rating },
    {
      headers: { Authorization: `Bearer ${token}` },
    }
  );
}
