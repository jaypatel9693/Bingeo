// src/api.ts
import axios from "axios";

// Base URLs
const AUTH_API = "http://localhost:8080/api/auth";
const MOVIES_API = "http://localhost:8080/api/movies";

// ===================== AUTH =====================

// User login
export async function login(email: string, password: string) {
  const res = await axios.post(`${AUTH_API}/login`, { email, password });
  return res.data; // { token: "..." }
}

// User registration
export async function register(email: string, password: string) {
  const res = await axios.post(`${AUTH_API}/register`, { email, password });
  return res.data; // { token: "..." }
}

// ===================== MOVIES =====================

// Get popular movies
export async function getPopularMovies() {
  const res = await axios.get(`${MOVIES_API}/popular`);
  return res.data;
}

// (optional) Get all movies
export async function getAllMovies() {
  const res = await axios.get(`${MOVIES_API}`);
  return res.data;
}
