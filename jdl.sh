#!/bin/bash

./clean.sh
jhipster jdl edster.jdl --force;
echo ".goose" >> .gitignore;
./sync.sh;
