# sparrow-coder 麻雀代码生成器 【灵活半自动化代码生成器】

# 核心功能
## 基于整洁架构生成核心代码
![整洁架构](整洁架构.png)
下载sparrow-example 【默认保存至${user.home}】目录

可以在config.properties 文件中修改该目录[config.properties](./example-po/bin/config.properties)
] 见下文说明
[sparrow-example](https://github.com/sparrowzoo/sparrow-example)

## 生成前后端代码
- 支持前后端分离和不分离版本
- 本示例为不分离版本，分离版本由网友自行配置

# 使用方式
## 添加sparrow 依赖
个性maven settings 配置文件
将下文代码复制到 {user.home}/.m2/setting.xml 文件中.
```

<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">

    <localRepository>${user.home}/.m2/repository</localRepository>
    <interactiveMode>true</interactiveMode>
    <profiles>
        <profile>
            <id>center-repository</id>
            <repositories>
                <repository>
                    <id>center</id>
                    <name>center Repository</name>
                    <url>https://s01.oss.sonatype.org/content/repositories/snapshots</url>
                    <snapshots>
                        <enabled>true</enabled>
                        <updatePolicy>always</updatePolicy>
                        <checksumPolicy>warn</checksumPolicy>
                    </snapshots>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                </repository>
            </repositories>
        </profile>
    </profiles>

    <activeProfiles>
        <activeProfile>center-repository</activeProfile>
    </activeProfiles>
</settings>
```

## 编译
配置成功后执行命令
```
mvn clean install -Dmaven.test.skip
```


## 环境变量配置说明
SPARROW_CODER_HOME={sparrow-coder/coder/target} 目录下

{sparrow-coder/coder/target} 为变量 具体为sparrow-coder-all.jar 所在目录，即代码生成器所在目录
## 配置文件
```
vi example-po/bin/config.properties
workspace=修改成自己的工作目录(sparrow-example 工程所在目录)
resource_workspace=修改成自己的工作目录
```

## java 类生成

```
cd /sparrow-coder/example-po/bin //切到bin 目录
sh sparrow-java-coder.sh --help //帮助菜单
sh sparrow-java-coder.sh --example //生成示例代码

sh sparrow-java-coder.sh -ct com.sparrow.example.po.SparrowExample
sh sparrow-java-coder-all.sh //生成所有java 代码
```

## 前端代码生成


```
sh sparrow-front-coder.sh --help //帮助菜单
sh sparrow-front-coder.sh --example //生成示例代码

生成对应页面脚本示例
sh sparrow-front-coder.sh -mp com.sparrow.coding.config.ExampleFront
sh sparrow-front-coder.sh -cp com.sparrow.coding.config.ExampleFront
sh sparrow-front-coder.sh -cj com.sparrow.coding.config.ExampleFront
sh sparrow-front-coder.sh -lj com.sparrow.coding.config.ExampleFront
sh sparrow-front-coder.sh -mj com.sparrow.coding.config.ExampleFront
sh sparrow-front-code-all.sh //生成所有前端代码

```


