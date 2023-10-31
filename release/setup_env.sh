#!/bin/sh

echo "$PRIVATE_GPG" | base64 --decode > ./release/secring.gpg
mkdir -p ~/.gradle && touch ~/.gradle/gradle.properties

{ \
  echo "mavenCentralUsername=$MAVEN_CENTRAL_USERNAME";\
  echo "mavenCentralPassword=$(echo "$MAVEN_CENTRAL_PASSWORD" | base64 --decode)";\
  echo "signing.secretKeyRingFile=release/private.gpg";\
  echo "signing.keyId=$SIGNING_KEY_ID";\
  echo "signing.password=$(echo "$SIGNING_PASSWORD" | base64 --decode)";\
} >> ~/.gradle/gradle.properties

