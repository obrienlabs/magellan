#!/bin/bash
# http://wiki.obrienlabs.cloud/display/DEV/Experiment+1901%3A+Sending+Packets+around+the+Earth+-+magellan
# source from http://jira.obrienlabs.cloud/browse/MAGELLAN-1
# https://github.com/obrienlabs/magellan
# Michael O'Brien
# docker login with token before running - to push

TAG=0.0.3-ia64
#TAG=0.0.3-arm

BUILD_ID=10001
BUILD_DIR=builds
mkdir ../../$BUILD_DIR
TARGET_DIR=../../$BUILD_DIR/$BUILD_ID
mkdir $TARGET_DIR
CONTAINER_IMAGE=magellan-nbi

cd ../../
mvn clean install -U -DskipTests=true
cd src/docker

cp ../../target/*.jar $TARGET_DIR
cp DockerFile $TARGET_DIR
cp startService.sh $TARGET_DIR
cd $TARGET_DIR
docker build --no-cache --build-arg build-id=$BUILD_ID -t obrienlabs/$CONTAINER_IMAGE -f DockerFile .
docker tag $CONTAINER_IMAGE:latest $CONTAINER_IMAGE:latest
docker tag obrienlabs/$CONTAINER_IMAGE obrienlabs/$CONTAINER_IMAGE:$TAG
# dockerhub
docker push obrienlabs/$CONTAINER_IMAGE:$TAG
# locally
CONTAINER_IMAGE2=magellan-nbi2
docker stop $CONTAINER_IMAGE
docker rm $CONTAINER_IMAGE
docker stop $CONTAINER_IMAGE2
docker rm $CONTAINER_IMAGE2

echo "starting: $CONTAINER_IMAGE"
docker run --name $CONTAINER_IMAGE \
    -d -p 8888:8080 \
    -e os.environment.configuration.dir=/ \
    -e os.environment.ecosystem=sbx \
    obrienlabs/$CONTAINER_IMAGE:$TAG
docker run --name $CONTAINER_IMAGE2 \
    -d -p 8889:8080 \
    -e os.environment.configuration.dir=/ \
    -e os.environment.ecosystem=sbx \
    obrienlabs/$CONTAINER_IMAGE:$TAG

cd ../../src/docker

echo "http://127.0.0.1:8888/nbi/forward/packet?dnsFrom=host.docker.internal&dnsTo=host.docker.internal&from=8889&to=8888&delay=1"

