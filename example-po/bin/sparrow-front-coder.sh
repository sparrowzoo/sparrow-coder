#!/bin/sh
source /etc/profile

[ -z "$SPARROW_CODER_HOME" ] && echo "please config environment variable SPARROW_CODER_HOME" && exit 0
sparrow_coder_name=sparrow-coder-all.jar
class_path=$(cd ../target/classes;pwd)
if [ $1 == '--help' ]; then
    java  -classpath $SPARROW_CODER_HOME/$sparrow_coder_name:$class_path  com.sparrow.coding.FrontMain --help
    exit
fi

if [ $1 == '--example' ]; then
    java  -classpath $SPARROW_CODER_HOME/$sparrow_coder_name:$class_path  com.sparrow.coding.FrontMain --example
    exit
fi

options=$1
if [ $1 == '-all' ]; then
     options='-mp,-cp,-cj,-lj,-mj'
fi
#将,替换为空格
options_array=(${options//,/ })
for option in ${options_array[@]}
do
  echo   java  -classpath $SPARROW_CODER_HOME/$sparrow_coder_name:$class_path  com.sparrow.coding.FrontMain $option $2

  java  -classpath $SPARROW_CODER_HOME/$sparrow_coder_name:$class_path  com.sparrow.coding.FrontMain $option $2
done




