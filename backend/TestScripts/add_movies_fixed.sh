#!/bin/bash

BASE_URL="http://localhost:8080/api/movies"

add_movie() {
    local title="$1"
    local year="$2"
    
    echo "Adding: $title ($year)"
    
    curl -X POST "$BASE_URL" \
        -H "Content-Type: application/json" \
        -d "{\"title\": \"$title\", \"releaseYear\": $year}"
    
    echo ""
}

# Test with one movie first
add_movie "Test Movie" 2023
add_movie "Test Movie" 2023
