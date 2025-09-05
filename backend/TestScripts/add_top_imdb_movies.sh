#!/bin/bash

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== Adding Top IMDB Movies to Bingeo ===${NC}"
echo ""

# Base URL for your API
BASE_URL="http://localhost:8080/api/movies"

# Function to add a movie
add_movie() {
    local title="$1"
    local year="$2"
    local director="$3"
    local genre="$4"
    local rating="$5"
    
    echo -e "${YELLOW}Adding: $title ($year)${NC}"
    
    response=$(curl -s -X POST "$BASE_URL" \
        -H "Content-Type: application/json" \
        -d "{
            \"title\": \"$title\",
            \"year\": $year,
            \"director\": \"$director\",
            \"genre\": \"$genre\",
            \"rating\": $rating
        }")
    
    if [ $? -eq 0 ] && [[ "$response" != *"error"* ]]; then
        echo -e "${GREEN}✓ Successfully added: $title${NC}"
    else
        echo -e "${RED}✗ Failed to add: $title${NC}"
        echo "Response: $response"
    fi
    echo ""
}

# Check if the server is running
echo -e "${YELLOW}Checking if Bingeo server is running...${NC}"
if ! curl -s "$BASE_URL" > /dev/null; then
    echo -e "${RED}Error: Cannot connect to $BASE_URL${NC}"
    echo -e "${YELLOW}Please make sure your Bingeo application is running:${NC}"
    echo "mvn spring-boot:run"
    exit 1
fi
echo -e "${GREEN}✓ Server is running${NC}"
echo ""

# Add top IMDB movies
echo -e "${BLUE}Adding top-rated movies...${NC}"
echo ""

# Top 20 IMDB movies with details
add_movie "The Shawshank Redemption" 1994 "Frank Darabont" "Drama" 9.3
add_movie "The Godfather" 1972 "Francis Ford Coppola" "Crime/Drama" 9.2
add_movie "The Dark Knight" 2008 "Christopher Nolan" "Action/Crime" 9.0
add_movie "The Godfather Part II" 1974 "Francis Ford Coppola" "Crime/Drama" 9.0
add_movie "12 Angry Men" 1957 "Sidney Lumet" "Drama" 9.0
add_movie "Pulp Fiction" 1994 "Quentin Tarantino" "Crime/Drama" 8.9
add_movie "Schindler's List" 1993 "Steven Spielberg" "Biography/Drama" 8.9
add_movie "The Lord of the Rings: The Return of the King" 2003 "Peter Jackson" "Adventure/Fantasy" 8.9
add_movie "Fight Club" 1999 "David Fincher" "Drama" 8.8
add_movie "Forrest Gump" 1994 "Robert Zemeckis" "Drama/Romance" 8.8
add_movie "Inception" 2010 "Christopher Nolan" "Action/Sci-Fi" 8.8
add_movie "The Lord of the Rings: The Fellowship of the Ring" 2001 "Peter Jackson" "Adventure/Fantasy" 8.8
add_movie "Star Wars: Episode V - The Empire Strikes Back" 1980 "Irvin Kershner" "Adventure/Fantasy" 8.7
add_movie "The Lord of the Rings: The Two Towers" 2002 "Peter Jackson" "Adventure/Fantasy" 8.7
add_movie "The Matrix" 1999 "Lana Wachowski, Lilly Wachowski" "Action/Sci-Fi" 8.7
add_movie "Goodfellas" 1990 "Martin Scorsese" "Biography/Crime" 8.7
add_movie "One Flew Over the Cuckoo's Nest" 1975 "Milos Forman" "Drama" 8.7
add_movie "Parasite" 2019 "Bong Joon Ho" "Comedy/Drama" 8.6
add_movie "Se7en" 1995 "David Fincher" "Crime/Mystery" 8.6
add_movie "The Silence of the Lambs" 1991 "Jonathan Demme" "Crime/Horror" 8.6

echo ""
echo -e "${BLUE}=== Summary ===${NC}"
echo -e "${GREEN}Finished adding top IMDB movies!${NC}"
echo ""
echo -e "${YELLOW}To view all movies:${NC}"
echo "curl http://localhost:8080/api/movies"
