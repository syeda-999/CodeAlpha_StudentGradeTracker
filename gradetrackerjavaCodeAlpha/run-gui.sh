#!/usr/bin/env bash
# Compile + run the GUI front end (needs a desktop session)
set -e
cd "$(dirname "$0")"
mkdir -p out
javac -d out src/*.java
java -cp out GuiApp
