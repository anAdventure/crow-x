#!/bin/bash

cd ../crowx-admin/target/

docker build -t crowx:1.0.0 -f ./bin/dockerfile .

docker build -t crowx:1.0.0 -f dockerfile .
docker run -d -p 7999:8080 --platform linux/amd64  --name crowx crowx:1.0.0
docker run -d -p 7999:8080   --name crowx crowx:1.0.0


docker build --no-cache -t crowx:1.0.0 --platform linux/arm64  -f ./bin/dockerfile .
