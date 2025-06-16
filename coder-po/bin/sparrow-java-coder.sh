#!/bin/sh
source /etc/profile

[ -z "$SPARROW_CODER_HOME" ] && echo "please config environment variable SPARROW_CODER_HOME" && exit 0
sparrow_coder_name=sparrow-coder-all.jar
class_path=$(cd ../target/classes;pwd)
if [ $1 == '--help' ]; then
    java  -classpath $SPARROW_CODER_HOME/$sparrow_coder_name:$class_path  com.sparrow.coding.JavaMain --help
fi

if [ $1 == '--example' ]; then
    java  -classpath $SPARROW_CODER_HOME/$sparrow_coder_name:$class_path  com.sparrow.coding.JavaMain --example
fi

options=$1
if [ $1 == '-all' ]; then
     options='-b,-p,-q,-v,-cv,-pq,-cq,-bop,-d,-di,-mi,-r,-ri,-s,-c,-a,-ct'
fi
#将,替换为空格
options_array=(${options//,/ })
for option in ${options_array[@]}
do
  java  -classpath $SPARROW_CODER_HOME/$sparrow_coder_name:$class_path  com.sparrow.coding.JavaMain $option $2 $3
done




