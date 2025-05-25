#!/bin/bash

COMMAND=${1:-all}  # Default to 'all' if no argument is provided

case "$COMMAND" in
  build)
    ./mvnw clean install
    docker compose build
    ;;
    
  run)
    docker compose up
    ;;
  
  all)
    ./mvnw clean install
    docker compose up --build
    ;;
  
  *)
    echo "Unknown command: $COMMAND"
    echo "Usage: ./run.sh [build|run|all]"
    exit 1
    ;;
esac