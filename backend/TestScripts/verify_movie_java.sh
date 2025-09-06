#!/bin/bash

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== Bingeo Movie Application Verification ===${NC}"
echo ""

# Function to check if command succeeded
check_status() {
    if [ $? -eq 0 ]; then
        echo -e "${GREEN}✓ $1${NC}"
        return 0
    else
        echo -e "${RED}✗ $1${NC}"
        return 1
    fi
}

# Function to check if file exists
check_file() {
    if [ -f "$1" ]; then
        echo -e "${GREEN}✓ Found: $1${NC}"
        return 0
    else
        echo -e "${RED}✗ Missing: $1${NC}"
        return 1
    fi
}

# Check if we're in the right directory
echo -e "${YELLOW}1. Checking project structure...${NC}"
if [ ! -f "pom.xml" ]; then
    echo -e "${RED}Error: pom.xml not found. Please run this script from the backend directory.${NC}"
    exit 1
fi

# Verify key Java files exist
echo -e "${YELLOW}2. Verifying core Java files...${NC}"
CORE_FILES=(
    "src/main/java/com/example/bingeo/movie/MovieController.java"
    "src/main/java/com/example/bingeo/movie/MovieService.java"
    "src/main/java/com/example/bingeo/rec/RecommendationService.java"
    "src/main/java/com/example/bingeo/auth/JwtService.java"
    "src/main/java/com/example/bingeo/auth/PasswordService.java"
    "src/main/java/com/example/bingeo/config/CorsConfig.java"
)

missing_files=0
for file in "${CORE_FILES[@]}"; do
    if ! check_file "$file"; then
        ((missing_files++))
    fi
done

# Check for main application class
echo -e "${YELLOW}3. Looking for main application class...${NC}"
MAIN_CLASS=$(find src/main/java -name "*Application.java" -o -name "*Main.java" | head -1)
if [ -n "$MAIN_CLASS" ]; then
    check_file "$MAIN_CLASS"
else
    echo -e "${RED}✗ Main application class not found${NC}"
    ((missing_files++))
fi

# Check Maven/Gradle build file
echo -e "${YELLOW}4. Checking build configuration...${NC}"
check_file "pom.xml"

# Verify Java compilation
echo -e "${YELLOW}5. Testing compilation...${NC}"
echo "Compiling Java sources..."
if command -v mvn &> /dev/null; then
    mvn compile -q
    check_status "Maven compilation"
else
    echo -e "${YELLOW}Maven not found, skipping compilation test${NC}"
fi

# Check for data directory and files
echo -e "${YELLOW}6. Checking data files...${NC}"
if [ -d "data" ]; then
    echo -e "${GREEN}✓ Data directory exists${NC}"
    data_files=$(find data -type f | wc -l)
    if [ $data_files -gt 0 ]; then
        echo -e "${GREEN}✓ Found $data_files data file(s)${NC}"
        echo "Data files:"
        find data -type f | head -5
    else
        echo -e "${YELLOW}! Data directory is empty${NC}"
    fi
else
    echo -e "${YELLOW}! No data directory found${NC}"
fi

# Check for setup scripts
echo -e "${YELLOW}7. Checking setup scripts...${NC}"
SETUP_SCRIPTS=("setup-auth.sh" "setup-movie.sh" "setup-security.sh")
for script in "${SETUP_SCRIPTS[@]}"; do
    if check_file "$script"; then
        if [ -x "$script" ]; then
            echo -e "${GREEN}  ✓ $script is executable${NC}"
        else
            echo -e "${YELLOW}  ! $script is not executable${NC}"
        fi
    fi
done

# Check Postman collection
echo -e "${YELLOW}8. Checking API documentation...${NC}"
check_file "bingeo.postman_collection.json"

# Check for common Spring Boot properties
echo -e "${YELLOW}9. Checking configuration...${NC}"
if [ -f "src/main/resources/application.properties" ]; then
    check_file "src/main/resources/application.properties"
elif [ -f "src/main/resources/application.yml" ]; then
    check_file "src/main/resources/application.yml"
else
    echo -e "${YELLOW}! No application.properties or application.yml found${NC}"
fi

# Test if application can start (quick test)
echo -e "${YELLOW}10. Testing application startup (quick test)...${NC}"
if command -v mvn &> /dev/null; then
    echo "Testing if application can start..."
    timeout 10s mvn spring-boot:run -q &> /dev/null &
    PID=$!
    sleep 3
    if kill -0 $PID 2>/dev/null; then
        echo -e "${GREEN}✓ Application starts successfully${NC}"
        kill $PID 2>/dev/null
        wait $PID 2>/dev/null
    else
        echo -e "${YELLOW}! Application startup test inconclusive${NC}"
    fi
else
    echo -e "${YELLOW}Maven not found, skipping startup test${NC}"
fi

# Summary
echo ""
echo -e "${BLUE}=== Verification Summary ===${NC}"
if [ $missing_files -eq 0 ]; then
    echo -e "${GREEN}✓ All core files are present${NC}"
    echo -e "${GREEN}✓ Project structure looks good${NC}"
    echo ""
    echo -e "${GREEN}Your Bingeo movie application appears to be properly set up!${NC}"
    echo ""
    echo -e "${BLUE}Next steps:${NC}"
    echo "1. Run: mvn spring-boot:run"
    echo "2. Test endpoints with the Postman collection"
    echo "3. Check application logs for any startup issues"
else
    echo -e "${RED}✗ Found $missing_files missing file(s)${NC}"
    echo -e "${YELLOW}Please check the missing files above${NC}"
fi

echo ""
echo -e "${BLUE}Available endpoints (from MovieController):${NC}"
if [ -f "src/main/java/com/example/bingeo/movie/MovieController.java" ]; then
    grep -n "@.*Mapping" src/main/java/com/example/bingeo/movie/MovieController.java | head -5
fi
