// Central place for shared types
export interface Movie {
  id: number;
  title: string;
  poster: string;   // full image URL
  rating: number;   // IMDb rating 0–10 (float)
}
