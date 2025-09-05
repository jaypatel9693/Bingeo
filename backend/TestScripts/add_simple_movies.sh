#!/bin/bash

# Simple script to add movies with basic fields only
echo "=== Adding Top Movies (Simple Version) ==="
echo ""

BASE_URL="http://localhost:8080/api/movies"

# Function to add movie with minimal fields
add_simple_movie() {
    local title="$1"
    local year="$2"
    
    echo "Adding: $title ($year)"
    
    curl -X POST "$BASE_URL" \
        -H "Content-Type: application/json" \
        -d "{\"title\": \"$title\", \"year\": $year}"
    
    echo ""
}

# Check server
echo "Checking server..."
curl -s "$BASE_URL" > /dev/null
if [ $? -ne 0 ]; then
    echo "Error: Server not running. Start with: mvn spring-boot:run"
    exit 1
fi

echo "Server is running. Adding movies..."
echo ""

# Add top movies with just title and year
add_simple_movie "The Shawshank Redemption" 1994
add_simple_movie "The Godfather" 1972
add_simple_movie "The Dark Knight" 2008
add_simple_movie "Pulp Fiction" 1994
add_simple_movie "Schindler's List" 1993
add_simple_movie "Fight Club" 1999
add_simple_movie "Forrest Gump" 1994
add_simple_movie "Inception" 2010
add_simple_movie "The Matrix" 1999
add_simple_movie "Goodfellas" 1990

echo ""
echo "=== Done! Check your movies: ==="
echo "curl http://localhost:8080/api/movies"
