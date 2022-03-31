#!/usr/bin/env bash
export ARGS=`echo "$@"`
mvn package
clear
java -jar ./target/photoGallery*.jar "$ARGS"