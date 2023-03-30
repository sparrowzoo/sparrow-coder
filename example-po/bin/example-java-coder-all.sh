config_path=$(pwd)/config.properties
echo $config_path
sh ./sparrow-java-coder.sh -all com.sparrow.example.po.SparrowExample -config=$config_path
