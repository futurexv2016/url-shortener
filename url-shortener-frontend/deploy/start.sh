#!/bin/sh
docker-compose down --remove-orphans
docker-compose -f ../docker-compose.yml up --build
