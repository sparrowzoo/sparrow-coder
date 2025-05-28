config_path=$(pwd)/config.properties
echo $config_path

if pwd | grep -q -E 'bin$'; then
    echo "true"
else
    echo "请cd到bin目录下运行"
    exit 1
fi

echo $pwd
#sh ./sparrow-java-coder.sh -all com.sparrow.example.po.SparrowExample -config=$config_path
