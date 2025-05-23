#!/bin/bash

# Check if an argument is passed
if [ $# -eq 0 ]; then
  echo "No command provided. Usage: ./run.sh [clean|build|deploy]"
  exit 1
fi

case "$1" in
  clean)
    ./mvnw clean install
    # ./mvnw clean package -pl calculator
    # ./mvnw clean package -pl rest
    ;;

  build)
    ./mvnw clean install

    echo "Starting REST module..."
    ./mvnw -pl rest spring-boot:run > rest.log 2>&1 &

    echo "Starting Calculator module..."
    ./mvnw -pl calculator spring-boot:run > calculator.log 2>&1 &
    ;;

  *)
    echo "Unknown command: $1"
    echo "Usage: ./run.sh [clean|build|deploy]"
    exit 1
    ;;
esac
