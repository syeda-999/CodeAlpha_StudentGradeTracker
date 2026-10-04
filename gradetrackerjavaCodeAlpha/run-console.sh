#!/usr/bin/env bash
# Compile + run the CONSOLE front end
set -e
cd "$(dirname "$0")"
mkdir -p out
javac -d out src/*.java
java -cp out ConsoleApp
