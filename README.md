# sparrow-coder
代码生成器示例脚手架

git clone https://github.com/sparrowzoo/sparrow-shell.git
git clone https://github.com/sparrowzoo/sparrow-bom.git
git clone https://github.com/sparrow-os/sparrow-starter.git

cd sparrow-bom
mvn clean install
cd ../sparrow-shell
mvn clean install -Dmaven.test.skip
cd ../sparrow-starter
mvn clean install -Dmaven.test.skip

remove
