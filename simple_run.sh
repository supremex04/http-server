#!/bin/bash

# Exit on any error
set -e

# Change to the directory containing this script
cd "$(dirname "$0")"

# Create a classes directory if it doesn't exist
mkdir -p classes

# Compile all Java files
echo "Compiling Java files..."
javac -d classes src/main/java/*.java

# Run the Main class
echo "Starting server..."
java -cp classes Main "$@"