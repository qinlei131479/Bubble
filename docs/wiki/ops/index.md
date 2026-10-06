# 运维

端口以[架构文档](/wiki/architecture/#ports)为准。登录信息见[入门](/wiki/guide/#accounts)。

## 容器部署 {#docker}

编排文件是 `docker/docker-compose.yml`。业务代码在 `apps/`，这里只有镜像和 Nginx。

| 服务名 | 容器 | 宿主机端口 | 说明 |
|--------|------|------------|------|
| mysql | bubble-mysql | 33306 | 初始化脚本来自 `script/db` |
| redis | bubble-redis | 36379 | 无密码 |
| register | bubble-register | 8848、9848、8080 | Nacos |
| gateway | bubble-gateway | 8666 | 依赖 register |
| auth | bubble-auth | 不映射 | 仅 `bubble-net` 内访问 |
| biz-backend | bubble-biz-backend | 不映射 | 系统业务 |
| codegen | bubble-codegen | 不映射 | 代码生成 |
| quartz | bubble-quartz | 不映射 | 定时任务 |
| monitor | bubble-monitor | 5001 | 与进程端口 8902 不一致 |
| ui | bubble-ui | 80 | Nginx，`/api` 转到网关 |

不包含 `bubble-biz-agi`。Python 智能体的服务块是注释。容器网络为 `bubble-net`，`NACOS_HOST=bubble-register`，数据库与缓存主机名为 `bubble-mysql`、`bubble-redis`。

```bash
docker compose -f docker/docker-compose.yml up -d --build
docker compose -f docker/docker-compose.yml up -d mysql redis register
docker compose -f docker/docker-compose.yml logs -f gateway auth biz-backend
```

镜像要求模块已执行 `mvn package`，Dockerfile 复制 `target/*.jar`。前端使用 `docker/bubble-ui.Dockerfile`，Nginx 配置在 `docker/nginx`。

在 `docker/` 下复制 `.env.example` 为 `.env` 可覆盖 `MYSQL_PORT`（33306）、`MYSQL_ROOT_PASSWORD`（root）、`REDIS_PORT`（36379）。不要提交生产密码。

`depends_on` 只等待容器创建。网关和业务镜像会先 `sleep` 再启动 Java。若仍连不上配置中心，等 register 就绪后执行 `docker compose restart gateway auth biz-backend`。

IDE 默认访问 `127.0.0.1:3306`，compose 的 MySQL 在宿主机是 33306。同一份 Nacos 配置不要同时指向容器主机名和 localhost。空库与增量脚本的选择见[快速开始](/wiki/guide/#quick-start)。

## 脚本部署 {#script}

不用 compose、直接跑 jar 时使用 `script/deploy/deploy.sh`。它读取 `{应用名}.properties` 与 `version.properties`。

```bash
bash script/deploy/deploy.sh <应用名> start
bash script/deploy/deploy.sh <应用名> status
bash script/deploy/deploy.sh <应用名> stop
```

properties 包含应用名、端口、健康检查地址、jar 路径和 profile。第三个参数可覆盖堆内存，例如 `512m`。标准输出写入 `script/deploy/logs/start_<应用名>.log`。健康检查期望 HTTP 200。

`deploy_flow.sh` 按环境变量重启一组历史进程名，并不部署名为 flow 的业务。properties 中的服务名与端口表中的 artifact 对齐后再使用。

`script/db/Dockerfile` 把初始化 SQL 放进 `bubble-mysql` 镜像。改脚本后需重新构建镜像；已有数据卷不会自动重放 SQL。

发布顺序：备份 `bubble`；执行增量 SQL；先发布业务 jar，再发布依赖它的服务，最后发布网关；用管理端打开改过的页面。令牌格式不兼容时安排重新登录，不要在脚本中删除 `bubble-cloud::token::`。

## 监控与日志 {#monitoring}

`bubble-monitor` 从 Nacos 发现实例，可查看健康、内存、线程和日志级别。级别改动只存在进程内存中，重启后回到 `logback-spring.xml`。本地地址是 `http://localhost:8902`。控制台账号在 Nacos 的 monitor 配置里，不是管理端的 `admin`。容器端口映射见上一节。

每个服务的 `logback-spring.xml` 把日志写到工作目录 `logs/`。`deploy.sh` 另写标准输出。业务异常看下游日志，网关日志只反映转发。

`@SysLog` 写入 `sys_log`，在系统管理的日志页面查询。Nacos 控制台用于确认实例和配置是否下发。

仓库中没有 Prometheus、Grafana 或集中日志。需要时作为独立组件加入 `docker/`，不要嵌进业务模块。

## 常见问题 {#troubleshooting}

### 登录提示客户端不合法

`VITE_OAUTH2_PASSWORD_CLIENT` 与 `sys_oauth_client_details` 必须一致，且授权类型包含 `password` 和 `refresh_token`。修改 `.env` 后重启 Vite。

### 登录提示密码错误

种子密码是 `123456`，库存 bcrypt。前端使用 `thanks,bubbleyes` 做 AES，密钥必须为 16 位且与认证服务一致。验证码为空时到不了密码校验；Redis 中应有 `bubble-cloud::DEFAULT_CODE_KEY:`。连续失败达到 `LOGIN_ERROR_TIMES` 后账户锁定。

### 接口返回 424 或 403

424 表示访问令牌失效，前端会回到登录页。若只有单个请求出现 424，检查是否漏了 `Authorization`。403 表示已登录但没有 `sys_menu` 中的权限。改完菜单删除 `bubble-cloud::menu_details`，不要删除 token。

### 验证码不显示

查看验证码请求的状态码。503 表示网关找不到 `bubble-auth`。连接被拒绝表示 8666 没有进程。

### 服务列表为空

确认命名空间是 `bubble`，`NACOS_HOST` 指向当前这台 Nacos。容器内不要把该变量设为 `127.0.0.1`，除非 Nacos 就在同一容器。8848 通而注册失败时检查 9848。

### 启动报 Failed to configure a DataSource

到命名空间 `bubble` 查看 `{服务名}-dev.yml`。JDBC 端口与实际实例一致：本机 3306、compose 映射 33306，容器内为 `bubble-mysql:3306`。

### 网关文档空指针

下游都未注册时，文档聚合端点可能报错。先启动 auth 和 backend。业务接口不依赖该端点。

### jar 没有主清单属性

`mvn package` 需要 Spring Boot 插件重打包。确认 `java -jar` 能打印启动横幅。Dockerfile 复制的是这个文件。

### 主类启动报 yml 语法错误

IDE 直接运行未经 Maven 过滤的资源时，可能留下 `@artifactId@`。先对聚合工程执行 `mvn compile`，再从 IDE 启动。

### 开发平台打开即报错

父菜单应进入第一个未隐藏的子页面。隐藏的生成页若成为默认子节点，会请求没有表名的 `/gen/table/` 并得到 405。模板更新检查也要同时具备数据源名和表名。

### 网站配置保存失败

`LOGIN_ERROR_TIMES` 与 `PASSWORD_EXPIRE_DAYS` 按 key 写入 `sys_public_param`。`public_value` 长度至少 2000，旧库执行 `upgrade-4.2.sql`。上传失败时确认 multipart 上限已随进程重启生效。
