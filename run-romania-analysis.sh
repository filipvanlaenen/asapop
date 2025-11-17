#!/bin/bash
# Script to build and run ASAPOP analysis for Romania

set -e  # Exit on error

echo "========================================"
echo "ASAPOP - Romania Opinion Poll Analysis"
echo "========================================"
echo ""

# Check Java version
echo "Checking Java version..."
java -version 2>&1 | head -n 1
echo ""

# Build the project
echo "Building project with Maven..."
mvn clean compile assembly:single

if [ ! -f "target/asapop-1.0-SNAPSHOT-jar-with-dependencies.jar" ]; then
    echo "ERROR: Build failed - JAR file not found"
    exit 1
fi

echo ""
echo "Build successful!"
echo ""

# Run the analysis
echo "Running analysis on romania-example.ropf..."
java -jar target/asapop-1.0-SNAPSHOT-jar-with-dependencies.jar \
  analyze \
  romania-example.ropf \
  romania-election-data.yaml \
  romania-results.yaml

echo ""
echo "========================================"
echo "Analysis complete!"
echo "========================================"
echo ""
echo "Results saved to: romania-results.yaml"
echo ""
echo "To view results:"
echo "  cat romania-results.yaml"
echo ""
echo "To convert to CSV format:"
echo "  java -jar target/asapop-1.0-SNAPSHOT-jar-with-dependencies.jar \\"
echo "    convert romania-example.ropf romania.csv PSD PNL AUR USR UDMR"
echo ""
