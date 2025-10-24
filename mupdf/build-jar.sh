#!/bin/zsh
set -e

rm -rf build/java/debug build/java/android

# --- Version without Android sources ---
mkdir -p build/java/debug/classes jar

# Find .java files excluding the android subdir
javac -d build/java/debug/classes \
  $(find mupdf/platform/java/src/com/artifex/mupdf/fitz -name '*.java' ! -path '*/android/*')
echo "Build the classes for desktop"

# Create non-Android jar
jar cf jar/mupdf.jar -C build/java/debug/classes .

echo "Built the jar for desktop"

# --- Version with Android sources ---
mkdir -p build/java/android/classes

javac -classpath ~/Android/Sdk/platforms/android-35/android.jar \
      -d build/java/android/classes \
      $(find mupdf/platform/java/src/com/artifex/mupdf/fitz -type f -name '*.java') 
echo "Build the classes for android"
# Create Android jar
jar cf jar/mupdf-android.jar -C build/java/android/classes .
