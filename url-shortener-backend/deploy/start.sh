#!/bin/sh
cd ..
./mvnw clean package
docker-compose down --remove-orphans
docker-compose up --build
