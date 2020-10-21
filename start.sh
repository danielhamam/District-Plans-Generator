#! /bin/bash
echo 'Welcome, *nix script...'
echo 'Starting up application...'
npm start --prefix ./client/ && fg
echo '[REACT] npm start'
 
cd ./backend 
./mvnw spring-boot:run
echo '[SPRING FRAMEWORK] ./mvnw spring-boot:run'