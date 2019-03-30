# Stage 1: Build theme files
FROM python:3 AS builder
WORKDIR /app

# Copy files
COPY requirements.txt .
COPY scripts scripts

# Install packages
RUN pip install --no-cache-dir -r requirements.txt

# Build the theme xml files
RUN python scripts/build.py

# Stage 2: Publish theme
FROM openjdk:11
WORKDIR /app

# Copy files
COPY . .
COPY --from=builder app/src/main/resources/colorSchemes src/main/resources/colorSchemes

# Install Gradle dependencies
RUN ./gradlew assemble

# Run task as specified by the build arg
ARG task
RUN ./gradlew $task
