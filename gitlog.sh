#!/usr/bin/env bash

# Find remote URL for hashes (designed for GitHub-hosted projects)
BASE="https://github.com/awave1/assessment-loan-system"

# Output LaTeX table in chronological order
echo "\\begin{tabularx}{\\textwidth}{l l X}\\textbf{Commit} & \\textbf{Author} & \\textbf{Description}\\\\ \\hline"
git log --pretty=format:"\\href{$BASE/commit/%H}{%h} & %an & %s\\\\ \\hline" --reverse | sed "s/#/\\\\#/g"
echo "\\end{tabularx}"
