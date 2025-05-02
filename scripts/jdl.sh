#!/bin/bash

# Define the expected directory name
EXPECTED_DIR_NAME="edster"

# Get the current working directory name
CURRENT_DIR=$(pwd)
CURRENT_DIR_NAME=$(basename "$CURRENT_DIR")

# Assertion: Check if the current directory name is the expected name
if [ "$CURRENT_DIR_NAME" == "$EXPECTED_DIR_NAME" ]; then
  echo "Assertion passed: Current directory name is '$EXPECTED_DIR_NAME'."
else
  echo "Assertion failed: Current directory name is '$CURRENT_DIR_NAME', but expected '$EXPECTED_DIR_NAME'." >&2
  exit 1
fi

./scripts/clean.sh

nvm use lts/jod;

jhipster jdl edster.jdl --force;

./scripts/sync.sh;

#jhipster ci-cd;

# shellcheck disable=SC2129
echo ".goose" >> .gitignore;
echo "README.pdf" >> .gitignore;
echo "README.html" >> .gitignore;
echo "README.docx" >> .gitignore;
echo "README.epub" >> .gitignore;
echo "README.fr.pdf" >> .gitignore;
echo "README.fr.html" >> .gitignore;
echo "README.fr.docx" >> .gitignore;
echo "README.fr.epub" >> .gitignore;
