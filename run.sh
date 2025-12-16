#!/bin/bash
# Load environment variables from .env file and start the Auth Service
export $(cat .env | xargs)
./mvnw spring-boot:run
