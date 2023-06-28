#!/bin/bash

# Prepare Jar
./mvnw clean
./mvnw package

# Ensure, that docker-compose stopped
docker-compose stop

# Start new deployment
docker-compose up --build -d
