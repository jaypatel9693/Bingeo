#!/bin/bash

echo "🔧 Fixing SecurityConfig..."
./fix_security_config.sh

echo "🔧 Creating missing classes..."
./create_missing_classes.sh

echo "🚀 Compiling and running..."
./run_app.sh
