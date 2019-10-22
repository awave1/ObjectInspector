#!/usr/bin/env bash

WATCH=$1
COMPILE="tectonic refactoring.tex"

if [ "$WATCH" == "--watch" ]; then
  watchman-make -p "refactoring.tex" --run "tectonic refactoring.tex"
else
  tectonic refactoring.tex
fi
