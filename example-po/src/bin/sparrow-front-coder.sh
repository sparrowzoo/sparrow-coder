#!/bin/sh

source /etc/profile

[ -z "$SPARROW_CODER_HOME" ] && echo "please config environment variable SPARROW_CODER_HOME" && exit 0

sparrow_coder_name=sparrow-coder-all.jar
class_path=$(cd ../target/classes;pwd)
java  -classpath $SPARROW_CODER_HOME/$sparrow_coder_name:$class_path  com.sparrow.coding.FrontMain $@



