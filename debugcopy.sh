#!/usr/bin/env bash

for buildJar in ./build/libs/*; do
  if [[ $buildJar == *original* ]]; then
    continue
  fi
  if [[ $buildJar == *-all.jar ]]; then
      cp -rv "$buildJar" "$1"
      break
  fi
done