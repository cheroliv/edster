#!/bin/bash

./clean.sh
jhipster jdl edster.jdl --force;
echo ".goose" >> .gitignore;
rsync -avr __webapp__/ src/main/webapp;
