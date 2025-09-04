// src/api.ts
import axios from "axios";

const API_URL = "http://localhost:8080/api/auth"; // ✅ backend auth endpoint

// User login
export async function login(email: string, password: string) {
  const res = await axios.post(`${API_URL}/login`, { email, password });
  return res.data; // { token: "..." }
}

// User registration
export async function register(email: string, password: string) {
  const res = await axios.post(`${API_URL}/register`, { email, password });
  return res.data; // { token: "..." }
}
