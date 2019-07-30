#!/bin/bash

python3 scripts/build.py
python3 scripts/changelog.py
python3 scripts/readme.py
./gradlew build
