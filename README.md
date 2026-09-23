# sparrow-coder

基于**整洁架构（Clean Architecture）**的后台管理系统代码生成器。

根据用户定义的 **PO 类**，一键生成符合企业级包规范的**后端代码**与**前端代码**（前后端分离），生成过程无需任何 AI 参与，纯模板 + 元数据驱动。

> 生成的后台管理系统以 `sparrow-example` 为**后端脚手架**，以 `react-next-admin`（位于 `sparrow-js` 前端工程内）为**前端脚手架**。

---

## 1. 项目背景

日常后台管理系统的开发存在大量重复劳动：Controller / Service / DAO / Mapper / 前端列表页、表单页等结构高度相似，仅字段与业务含义不同。

`sparrow-coder` 解决了这个问题：

1. 在界面上**定义项目**（项目名、包名、模块前缀、代码架构、脚手架等）；
2. **下载脚手架**到本地，得到一个可独立编译启动的前后端分离业务工程；
3. 在生成的项目中手动编写 **PO 类**（表结构元数据来源）；
4. 点击界面上的**生成代码**按钮，自动生成基于整洁架构的后端代码与前端页面；
5. 重新编译启动业务项目，代码即生效；
6. 根据 **column 配置**自定义前端界面展示。

### 核心特性

- **一键生成**：根据 PO 类自动生成 DAO / Domain / Adapter / Infrastructure 各层代码与前端页面，无需 AI；
- **整洁架构**：后端严格遵循 `adapter → domain → infrastructure → dao` 的分层与依赖倒置；
- **前后端分离**：后端 Spring Boot + 前端 React/Next.js，一次生成同时输出两端；
- **父子关系建模**：通过 `@ListDatasource(TABLE)` 关联父子表，自动生成下拉/关联控件；
- **界面可定制**：通过 `column_configs` 配置列的展示、过滤、行菜单等。

### 官方示例 PO 类

| 类 | 说明 |
| --- | --- |
| `com.sparrow.coder.po.Department` | 父表（部门） |
| `com.sparrow.coder.po.UserExample` | 子表（用户），通过 `@ListDatasource(type = TABLE, params = "t_department")` 关联部门 |

> **注意**：以上两个类（`Department`、`UserExample`）是**官方提供的示例**，仅作参考。生成业务代码时**不要直接使用**它们——请将其复制到你自己的 PO 包中，并把包名改为你的业务包名（例如 `com.yourcompany.po.Department`、`com.yourcompany.po.UserExample`）。

```java
@Table(name = "t_user_example")
@Data
public class UserExample extends PO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "int COMMENT 'ID'")
    private Long id;

    @Column(name = "user_name", nullable = false, columnDefinition = "varchar(32) COMMENT '用户名'")
    private String userName;

    // 父表关联：自动生成下拉选择控件
    @Column(name = "department_id", columnDefinition = "int comment '部门ID'")
    @ListDatasource(type = ListDatasourceType.TABLE, params = "t_department")
    private Long departmentId;
}
```

---

## 2. 相关依赖仓库

`sparrow-coder` 依赖以下仓库，需一并 clone 并按顺序编译安装：

| 仓库 | 作用 |
| --- | --- |
| [sparrow-shell](https://github.com/sparrowzoo/sparrow-shell) | 基础框架（`infra-bom`、`sparrow-bom` 及 `sparrow` 核心模块） |
| [sparrow-starter](https://github.com/sparrowzoo/sparrow-starter) | Spring Boot 与 Sparrow 框架的整合 Starter |
| [sparrow-file](https://github.com/sparrowzoo/sparrow-file) | 文件服务（`file-starter`） |
| [sparrow-passport-ddd](https://github.com/sparrowzoo/sparrow-passport-ddd) | 认证框架（`passport-starter`） |
| [sparrow-example](https://github.com/sparrowzoo/sparrow-example) | 后端脚手架（后台代码模板） |
| [sparrow-js](https://github.com/sparrowzoo/sparrow-js) | 前端工程，`react-next-admin` 为前端脚手架 |

---

## 3. 环境要求

| 依赖 | 版本要求 |
| --- | --- |
| JDK | 17+ |
| Maven | 3.6+ |
| MySQL | 5.7 / 8.x |
| Redis | 5+ |
| Node.js | 18+（仅前端项目运行需要） |

---

## 4. 快速开始

### 4.1 克隆代码

```bash
mkdir -p ~/workspace/sparrow && cd ~/workspace/sparrow

# 基础依赖
git clone https://github.com/sparrowzoo/sparrow-shell.git
git clone https://github.com/sparrowzoo/sparrow-starter.git
git clone https://github.com/sparrowzoo/sparrow-file.git
git clone https://github.com/sparrowzoo/sparrow-passport-ddd.git

# 前端工程（含 react-next-admin 前端脚手架）
git clone https://github.com/sparrowzoo/sparrow-js.git

# 本项目
git clone https://github.com/sparrowzoo/sparrow-coder.git

# 后端脚手架 sparrow-example（位于工作区根目录，与 sparrow 平级）
cd ~/workspace
git clone https://github.com/sparrowzoo/sparrow-example.git
```

### 4.2 按顺序编译安装依赖

```bash
cd ~/workspace/sparrow

cd sparrow-shell          && mvn clean install -Dmaven.test.skip=true
cd ../sparrow-starter     && mvn clean install -Dmaven.test.skip=true
cd ../sparrow-file        && mvn clean install -Dmaven.test.skip=true
cd ../sparrow-passport-ddd && mvn clean install -Dmaven.test.skip=true

# 编译本项目
cd ../sparrow-coder       && mvn clean install -Dmaven.test.skip=true
```

> `sparrow-shell` 内含 `sparrow-bom`（及 `infra-bom`），需最先安装，其余模块依赖其生成的 POM。

### 4.3 初始化数据库

在 MySQL 中创建数据库 `sparrow`，并执行 `ddl` 目录下的建表脚本：

```bash
mysql -uroot -p sparrow < ddl/t_project_config.sql
mysql -uroot -p sparrow < ddl/t_table_config.sql
mysql -uroot -p sparrow < ddl/t_user_example.sql
```

### 4.4 配置

编辑 `coder-main-spring-boot/src/main/resources/application.properties`：

```properties
# 本地工作区路径（Mac 示例）
workspace=/Users/<your-name>/workspace
project_root=sparrow
# 前端工程相对路径
frontend_project_root=sparrow/sparrow-js

# 数据库
spring.datasource.druid.url=jdbc:mysql://127.0.0.1/sparrow?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC
spring.datasource.druid.username=root
spring.datasource.druid.password=<your-password>

# Redis
spring.redis.host=127.0.0.1
spring.redis.port=6379
```

编辑 `coder-main-spring-boot/src/main/configs/dev.properties`：

```properties
# 前端静态资源物理路径
physical_resource={your_home_path}/workspace/sparrow/sparrow-js/source
```

### 4.5 启动 sparrow-coder

```bash
cd coder-main-spring-boot
mvn spring-boot:run
```

默认端口为 `8888`（`dev` 环境），启动后访问：

- 界面首页：<http://localhost:8888>
- 接口文档（Knife4j / Swagger）：<http://localhost:8888/doc.html>
- Druid 监控：<http://localhost:8888/druid>（账号密码均为 `druid`）

---

## 5. 操作指南

### 步骤 1：启动 sparrow-coder

按上文启动后，登录进入系统。

### 步骤 2：定义项目

在「项目配置」界面新增项目，填写：

- 项目名称 / 中文名称 / 前端项目名称
- 模块前缀、代码架构、脚手架
- 是否支持国际化、是否使用父 module 等

### 步骤 3：下载脚手架到本地

定义完成后，将脚手架下载到本地（`zip-download`）。**下载得到的工程即为你将要生成的业务项目**，包含符合你自己包规范的后端代码与前端代码。

### 步骤 4：编译启动生成的项目

```bash
cd <生成的项目根目录>
# 后端
mvn clean package -Dmaven.test.skip=true
java -jar <module>/target/*.jar

# 前端（前后端分离，单独启动）
cd <前端目录>
npm install
npm run dev
```

> 生成的项目是前后端分离结构，后端与前端需分别编译启动。

### 步骤 5：手动创建 PO 类

在刚生成的项目中，参考官方示例手动创建 PO 类：

- `com.sparrow.coder.po.Department` —— 父表
- `com.sparrow.coder.po.UserExample` —— 子表（父子关系示例）

> **重要**：示例类**不能直接用于生成**。请将它们复制到你自己的 PO 包中，并将包名修改为你的业务包名（例如 `com.yourcompany.po.Department`、`com.yourcompany.po.UserExample`），再进行代码生成。

PO 类通过 JPA 注解（`@Table` / `@Column` / `@ListDatasource`）描述表结构与字段展示元数据。

### 步骤 6：点击生成代码

回到 sparrow-coder 界面，选择表后点击「生成代码」按钮（`/coder/generate.json`）。

### 步骤 7：重新编译启动项目

回到业务项目，重新编译并启动，生成的代码即生效。

### 步骤 8：自定义前端界面展示

根据 `t_table_config` 中的 `column_configs`（列配置），自定义前端界面的列展示、过滤、行菜单、状态命令等。

---

## 6. 模块结构

```
sparrow-coder
├── bom                        # 依赖版本管理（coder-bom，parent 为 infra-bom）
├── coder-protocol             # 协议层（Query / Param / 枚举）
├── coder-po                   # 持久化对象（PO 类，含官方示例）
├── coder-dao-api              # DAO 接口定义
├── coder-dao-sparrow          # DAO 的 Sparrow ORM 实现
├── coder-dao-mybatis          # DAO 的 MyBatis 实现
├── coder-domain               # 领域层（业务逻辑 / 代码生成核心）
├── coder-infrastructure       # 基础设施层
├── coder-adapter              # 适配层（Controller / Assemble）
├── coder-spring-starter       # Spring Boot 整合 Starter
├── coder-main-spring-boot     # 启动模块（Application 入口）
└── ddl                        # 建表脚本
```

## 7. 常见问题

- **编译失败：找不到 `infra-bom` / `sparrow-*` 依赖** —— 依赖仓库未安装，请按 4.2 顺序重新 `mvn clean install`。
- **启动时数据库连接失败** —— 检查 MySQL 是否启动、`sparrow` 库是否创建、账号密码是否正确。
- **前端静态资源 404** —— 检查 `application.properties` 中 `workspace` / `frontend_project_root` 与 `dev.properties` 中 `physical_resource` 路径是否正确。
