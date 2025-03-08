#!/bin/bash

rsync -av __codebase__/build.gradle .;
rsync -avr __codebase__/gradle/ ./gradle;
rsync -avr __codebase__/src/ ./src;
