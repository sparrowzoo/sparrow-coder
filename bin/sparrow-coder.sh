#!/bin/sh

source /etc/profile

[ -z "$SPARROW_CODER_HOME" ] && echo "please config environment variable SPARROW_CODER_HOME" && exit 0

# Environment Variable Prerequisites
#
#
# SPARROW_CODER_HOME        home path can't null
# SPARROW_CODER             jar name default sparrow-coder-1.0.jar
# SPARROW_DEFAULT           db pool config file
# SPARROW_CONFIG_PATH       sparrow template config path default is "template"
# SPARROW_TABLE_CONFIG      sparrow table config file path e.g  resources/sparrow_table_config.properties
#

# convert to absolute for windows(git bath) compatibility
class_path=$(cd ../target/classes;pwd)
sparrow_coder_name=sparrow-coder-all.jar

[ -n "$SPARROW_CODER" ] && sparrow_coder_name=$SPARROW_CODER

java -classpath $SPARROW_CODER_HOME/$sparrow_coder_name:$class_path  com.sparrow.coding.Main $@



