# 入门

Bubble 是智能体原生平台的 Monorepo。管理端负责账号、权限、配置和 AI 业务入口，Java 微服务提供接口。Python 智能体目录 `apps/agentic` 尚未实现。

Maven 坐标 `com.bubblecloud:Bubble-Cloud`，`${revision}` 为 **4.0.0**。当前开发分支 `JDK17_master_4.1`。

## 技术栈 {#stack}

| 层级 | 技术 | 版本 |
|------|------|------|
| JDK | Java | 17+ |
| 后端 | Spring Boot | 4.1.1 |
| 微服务 | Spring Cloud | 2025.1.3 |
| 阿里组件 | Spring Cloud Alibaba | 2025.1.0.0 |
| 认证 | Spring Authorization Server | 随 Spring Boot 4.1 |
| ORM | MyBatis-Plus | 3.5.16 |
| 注册与配置 | Nacos | 2.x |
| 网关 | Spring Cloud Gateway | WebFlux |
| 前端 | Vue 3 + TypeScript | 3.5.42 / 4.9 |
| 组件库 | Element Plus | 2.13.7 |
| 样式 | Tailwind CSS + SCSS | Tailwind 3.4 |
| 构建 | Vite | 8.1.5 |
| Node.js | Node.js | >= 20.19 |
| 数据库 | MySQL | 8.0 |
| 缓存 | Redis | 6+ |
| 对象存储 | S3 兼容 | AWS SDK |

构建还需要 Maven 3.9+。容器镜像的基础运行时是 Java 21，与本地 JDK 17 不冲突。

## 分支 {#branches}

| 分支 | 运行时 | 状态 |
|------|--------|------|
| `JDK17_master_4.1` | Java 17，Spring Boot 4.1.1，Spring Cloud 2025.1，Vue 3 | 当前开发 |
| `JDK17_master` | Java 17，Spring Boot 3.5 | 维护 |
| `JDK8_master` | Java 8，Spring Boot 2.7 | 存量兼容 |

新功能只进 `JDK17_master_4.1`。与上游合并时从[架构文档的同步基线](/wiki/architecture/#upstream)之后开始，并保留该节列出的端口、客户端和缓存前缀。

## 仓库结构 {#layout}

业务代码只放 `apps/`。编排和 Nginx 只放 `docker/`。说明只放 `docs/`。

```text
Bubble/
├── apps/
│   ├── bubble-cloud/            # Java 聚合工程
│   │   ├── bubble-gateway/      # 网关
│   │   ├── bubble-auth/         # 认证中心
│   │   ├── bubble-register/     # 可选的 Nacos 服务端
│   │   ├── bubble-api/          # 实体、DTO、Feign，无启动类
│   │   ├── bubble-biz/          # 可部署业务
│   │   ├── bubble-common/       # 公共 jar 与 BOM
│   │   └── bubble-visual/       # 代码生成、监控、定时任务
│   ├── bubble-ui/               # Vue 3 管理端
│   └── agentic/                 # Python 智能体，尚未实现
├── docker/                      # compose、Dockerfile、Nginx
├── docs/
├── script/
│   ├── db/                      # 初始化与增量 SQL
│   └── deploy/                  # JAR 启停脚本
└── .cursor/rules/               # 通用开发约束
```

可部署模块包含 `pom.xml`、`Dockerfile`、启动类、`application.yml` 和 `logback-spring.xml`。`application.yml` 只放端口、应用名和 Nacos 地址；数据源与路由在 Nacos。其它服务要调用的类型放在对应 `bubble-api-*`。

前端 `src/api` 按后端前缀分目录，`src/views` 与 `sys_menu` 的组件路径对应。页面文案在 `views/<页面>/i18n/`，框架文案在 `src/i18n`。菜单由登录后的接口下发。

`target/`、`node_modules/`、`dist/` 是构建产物。运行日志在进程工作目录的 `logs/`。`.env` 不要提交到公开仓库。

新增 `apps/<name>` 时要带 README、依赖文件和最小启动方式，并补到[架构文档的模块职责](/wiki/architecture/#modules)。`.cursor/rules/` 只写跨模块写法，不写某个业务的排期。

## 快速开始 {#quick-start}

启动顺序：基础设施、数据库、网关、认证、系统业务、前端。代码生成、监控、定时任务和 AGI 按需启动。进程端口见[架构文档](/wiki/architecture/#ports)。

本地调试时先拉起基础设施：

```bash
docker compose -f docker/docker-compose.yml up -d mysql redis register
```

| 服务 | 宿主机 | 容器内 |
|------|--------|--------|
| MySQL | 33306（`MYSQL_PORT`） | 3306 |
| Redis | 36379（`REDIS_PORT`） | 6379 |
| Nacos | 8848，gRPC 9848 | 同左 |

MySQL root 密码默认 `root`。IDE 里的服务默认连 `127.0.0.1:3306` 与 `6379`。若数据库只暴露在 33306，数据源要改到该端口，或本机另起监听 3306 的实例。

空库：

```bash
mysql -h 127.0.0.1 -P 3306 -u root -p < script/db/bubble.sql
mysql -h 127.0.0.1 -P 3306 -u root -p < script/db/bubble_config.sql
```

已有 `bubble` 库只执行 `script/db/upgrade-4.2.sql`，不要重导全量脚本。

```bash
cd apps/bubble-cloud
mvn clean install -DskipTests
```

IDE 中的顺序：Nacos（`register` 容器已包含）→ `bubble-gateway` → `bubble-auth` → `bubble-biz-backend`。改过 Java 或 `application.yml` 后重启对应进程。

```bash
cd apps/bubble-ui
npm install
npm run dev
```

开发服务器默认 `http://localhost:8888`。登录账号见下一节。`/api` 的转发见[网关](/wiki/architecture/#gateway)。

全量容器的服务清单与命令见[运维](/wiki/ops/#docker)。镜像复制 `target/*.jar`，构建前需要先完成打包。

| 检查 | 期望 |
|------|------|
| Nacos 服务列表 | 能看到 gateway、auth、backend |
| `http://localhost:8666` | 网关在听；无 Token 的业务接口返回 401 |
| 登录 | 返回 `access_token` |
| 用户管理 | 列表可打开 |

失败时看[常见问题](/wiki/ops/#troubleshooting)。

## 账号与客户端 {#accounts}

生产环境要更换密码、客户端密钥和加密密钥。

| 项 | 值 |
|----|----|
| 管理端 | `http://localhost:8888`，容器部署为 Nginx 80 |
| 用户 | `admin` / `123456`，`sys_user.user_id = 1`，密码为 bcrypt |
| 密码客户端 | `VITE_OAUTH2_PASSWORD_CLIENT=bubble:bubble` |
| 短信客户端 | `VITE_OAUTH2_MOBILE_CLIENT=app:app` |
| 社交客户端 | `VITE_OAUTH2_SOCIAL_CLIENT=social:social` |
| 加密密钥 | `VITE_PWD_ENC_KEY=thanks,bubbleyes`，必须 16 位 |

登录必须填写验证码，答案在 Redis，前缀 `bubble-cloud::DEFAULT_CODE_KEY:`。`sys_oauth_client_details` 必须与 `.env` 使用同一组 `client_id` 与 `client_secret`。请求格式与令牌键见[认证](/wiki/architecture/#auth)。

网站配置中的锁定次数和密码有效期写入 `sys_public_param` 的 `LOGIN_ERROR_TIMES`、`PASSWORD_EXPIRE_DAYS`。Nacos 为 `nacos` / `nacos`，命名空间 `bubble`。监控台不使用 `admin`。
