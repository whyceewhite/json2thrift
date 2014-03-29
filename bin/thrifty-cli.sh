#!/bin/sh
TARGET_DIR=$(cd "$(dirname "$0")" && pwd)/../target
java -jar $TARGET_DIR/json2thrift-*-jar-with-dependencies.jar $@
