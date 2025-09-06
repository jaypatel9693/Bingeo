#!/bin/bash

echo "🧹 Cleaning and compiling..."
mvn clean compile

if [ $? -eq 0 ]; then
    echo "✅ Compilation successful! Starting application..."
    mvn spring-boot:run
else
    echo "❌ Compilation failed. Check the errors above."
fi
