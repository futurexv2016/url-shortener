#!/bin/sh
cd ../url-shortener-backend && ./mvnw clean package
cd .. && docker-compose down --remove-orphans
docker-compose -f ./docker-compose.yml up --build
