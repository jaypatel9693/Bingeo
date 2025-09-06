CREATE OR REPLACE VIEW title_wr AS
SELECT
  t.tconst,
  t.primary_title AS primarytitle,
  t.start_year AS startyear,
  t.title_type AS titletype,
  t.genres,
  r.average_rating AS rating,
  r.num_votes AS numvotes,
  ((r.num_votes::float / (r.num_votes + 25000)) * r.average_rating
   + (25000.0 / (r.num_votes + 25000)) * 6.8) AS wr
FROM titles t
JOIN ratings r ON t.tconst = r.tconst
WHERE r.num_votes IS NOT NULL
  AND r.average_rating IS NOT NULL
  AND r.num_votes >= 1000;
