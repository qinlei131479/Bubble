# 架构

请求从浏览器进入 Vite 或 Nginx，去掉 `/api` 后到达网关，再按路径转到认证、系统业务、AGI 或可视化服务。服务之间通过 Feign 调用，契约在 `bubble-api-*`。

```text
Browser
   │  开发: http://localhost:8888/api/...
   │  容器: http://<host>/api/...
   ▼
Vite 或 Nginx
   ▼
bubble-gateway :8666
   ├─ /auth/**   → bubble-auth        :8766
   ├─ /admin/**  → bubble-biz-backend :8801
   ├─ /gen/**    → bubble-codegen     :8901
   ├─ /job/**    → bubble-quartz      :8903
   └─ /agi/**    → bubble-biz-agi     :8805
```

每个业务进程校验 Bearer Token，向 Nacos 注册，使用 MySQL 与 Redis，并通过 Feign 访问其它服务的内部接口。`bubble-monitor` 不承接业务流量。

| 层 | 目录 | 能否单独启动 |
|----|------|----------------|
| 网关 | `bubble-gateway` | 能 |
| 认证 | `bubble-auth` | 能 |
| 契约 | `bubble-api-{模块}` | 不能 |
| 业务 | `bubble-biz-{模块}` | 能 |
| 公共能力 | `bubble-common-*` | 不能 |
| 管理面 | `bubble-visual-*` | 能 |

Controller 不注入 Mapper。简单条件使用 `Wrappers`，复杂查询放在 Service。返回 `R.ok` 或 `R.failed`。登录、配置和容器细节分别在下文的认证、注册与配置，以及[运维](/wiki/ops/#docker)。运行时版本见[入门](/wiki/guide/#stack)。

## 端口与服务 {#ports}

本表是端口的单一事实源。

| 模块 | 端口 | 职责 | 关键依赖 |
|------|-----:|------|----------|
| bubble-gateway | 8666 | 统一入口 | Redis、Nacos |
| bubble-auth | 8766 | 签发并校验客户端 | Redis、Nacos、Feign |
| bubble-biz-backend | 8801 | 用户、权限、文件、网站配置 | MySQL、Redis、Nacos、对象存储 |
| bubble-biz-agi | 8805 | 模型、知识库、对话、评测 | MySQL、Redis、Nacos |
| bubble-codegen | 8901 | 按数据源生成代码 | MySQL、Redis、Nacos |
| bubble-monitor | 8902 | Spring Boot Admin | Nacos |
| bubble-quartz | 8903 | 定时任务 | MySQL、Redis、Nacos |
| bubble-ui 开发服务器 | 8888 | Vite | 网关 |
| bubble-ui 容器 | 80 | Nginx，反代 `/api` | 网关 |

Nacos 控制台 8848，客户端 gRPC 9848。compose 把这两个端口映射到宿主机所有网卡。

compose 把数据库、缓存、网关和监控映射到宿主机所有网卡：MySQL `3306`（`MYSQL_PORT`），Redis `6379`（`REDIS_PORT`），Gateway `8666`，Monitor `5001`，前端 Nginx `80`。容器内使用主机名 `bubble-mysql`、`bubble-redis`、`bubble-register`。

`bubble-monitor` 的进程端口是 **8902**。Dockerfile 的 `EXPOSE` 与 compose 映射仍是 **5001:5001**。jar 未改 `server.port` 时，宿主机 5001 到不了进程。本地使用 `http://localhost:8902`。

约定：浏览器只访问网关，不直连 8801 或 8766。新增服务先在本表占用端口，再写 `application.yml` 和 compose。文档中的网关端口写 8666。

## 模块职责 {#modules}

| 模块 | 职责 | 边界 |
|------|------|------|
| `bubble-gateway` | 转发、跨域、收口浏览器流量 | 不写业务 SQL，不发 Token |
| `bubble-auth` | 密码、刷新、短信、社交登录 | 用户数据通过 Feign 向 backend 查询 |
| `bubble-api-backend` / `bubble-api-agi` | 实体与远程接口 | jar，无启动类 |
| `bubble-biz-backend` | 系统管理，前缀 `/admin` | 不承载模型对话 |
| `bubble-biz-agi` | 模型、知识库、对话、MCP、评测，前缀 `/agi` | 不替代认证中心 |
| `bubble-codegen` | 生成代码，前缀 `/gen` | 生成结果经审查后提交 |
| `bubble-monitor` | 健康、JVM、动态日志级别 | 不是业务网关 |
| `bubble-quartz` | 任务与执行日志，前缀 `/job` | 任务逻辑放在可调度 Bean 中 |
| `bubble-common-*` | 安全、Feign、日志、OSS、XSS、Excel、文档 | 不放业务表 |

公共组件清单见 `apps/bubble-cloud/README.md`。

管理端页面目录：`views/admin`、`views/gen`、`views/tools`、`views/agi`、`views/login`。菜单来自 `sys_menu`。隐藏菜单不能作为父级的默认落地页。

`apps/agentic` 规划为独立 Python 进程，只通过网关调用 Java，当前没有可执行入口。

新增服务时：在 `bubble-api` 与 `bubble-common-bom` 登记契约；在 `bubble-biz` 增加启动类、`application.yml` 和 `logback-spring.xml`；打开资源服务器、Feign 与接口文档；在 Nacos 命名空间 `bubble` 增加配置和路由；更新本节与端口表；前端增加 `src/api`、`src/views` 和菜单。业务排期不写入 `.cursor/rules/`。

## 数据库 {#database}

| 库 | 用途 | 脚本 |
|----|------|------|
| `bubble` | 用户、权限、任务、代码生成、AGI | `script/db/bubble.sql`；已有库用 `upgrade-4.2.sql` |
| `bubble_config` | Nacos 持久化 | `script/db/bubble_config.sql` |

全量脚本会重建表，只用于空库。连接信息以 Nacos 数据源为准。

系统表包括 `sys_user`、`sys_role`、`sys_user_role`、`sys_menu`、`sys_role_menu`、`sys_dept`、`sys_post`、`sys_user_post`、`sys_dict`、`sys_dict_item`、`sys_public_param`（`public_value` 为 `varchar(2000)`）、`sys_oauth_client_details`、`sys_log`、`sys_file`、`sys_i18n`、`sys_area`、`sys_message`、`sys_message_relation`、`sys_sensitive_word`、`sys_system_config`、`sys_schedule`。种子管理员见[账号](/wiki/guide/#accounts)。

`LOGIN_ERROR_TIMES` 与 `PASSWORD_EXPIRE_DAYS` 由网站配置按 key 写入。缺行时不走「系统内置参数禁止修改」的更新路径。

任务与生成：`sys_job`、`sys_job_log`、`QRTZ_*`、`gen_datasource_conf`、`gen_table`、`gen_table_column`、`gen_template`、`gen_group`。查询待生成表时必须带当前数据源。

约定：审计字段由 MyBatis-Plus 填充；`del_flag` 为 `0` 正常、`1` 删除；保留 `tenant_id`；主键使用包装类型；字符集 `utf8mb4`。改菜单后删除 `bubble-cloud::menu_details`，不要删除 `bubble-cloud::token::`。

AGI 表与系统表同在 `bubble` 库，实体在 `bubble-api-agi`。

## 注册与配置 {#nacos}

所有 Java 进程连接同一台 Nacos，控制台 `http://localhost:8848/nacos`。账号和密码来自部署环境的 `NACOS_USERNAME` / `NACOS_PASSWORD`，不要写入代码或仓库文档。命名空间固定为 **`bubble`**，由聚合 POM 的 `nacos.namespace` 在构建时写入 `application.yml`。`public` 中出现服务不表示配置已生效。`bubble_config.sql` 负责放入该命名空间和初始配置。

```yaml
spring:
  cloud:
    nacos:
      username: ${NACOS_USERNAME}
      password: ${NACOS_PASSWORD}
      discovery:
        server-addr: ${NACOS_HOST:127.0.0.1}:${NACOS_PORT:8848}
        namespace: bubble
      config:
        server-addr: ${spring.cloud.nacos.discovery.server-addr}
        namespace: bubble
  config:
    import:
      - optional:nacos:application-dev.yml
      - optional:nacos:${spring.application.name}-dev.yml
```

开发 profile 一般为 `dev`。IDE 中 `NACOS_HOST` 默认 `127.0.0.1`，compose 中为 `bubble-register`。`optional:` 允许暂时缺少 dataId 仍能启动；没有数据源时先核对控制台，而不是先改代码。

| dataId | 内容 |
|--------|------|
| `application-{profile}.yml` | Redis、日志、文档开关 |
| `{服务名}-{profile}.yml` | 该服务的数据源、线程池、白名单 |

服务名等于构建后的 `artifactId`。`@RefreshScope` 的 Bean 可热更新。`server.port` 和连接池在启动时绑定，修改后需重启。

服务未出现时：确认命名空间、`server-addr`、容器内没有把 `NACOS_HOST` 指到自身的 `127.0.0.1`，以及 9848 是否可达。本机与容器各启一份时会看到重复服务名，停掉不用的那份。

路由写在 Nacos 的网关配置中，不写在仓库的 `application.yml`。路径见下一节。

## 网关 {#gateway}

`bubble-gateway` 基于 Spring Cloud Gateway，端口 **8666**。同步上游时保持该端口，见下文同步基线。

| 浏览器 | 网关收到 |
|--------|----------|
| `/api/auth/oauth2/token` | `/auth/oauth2/token` |
| `/api/admin/user/page` | `/admin/user/page` |
| `/api/gen/table/page` | `/gen/table/page` |
| `/api/job/sys-job/page` | `/job/sys-job/page` |
| `/api/agi/...` | `/agi/...` |

开发代理目标为 `VITE_ADMIN_PROXY_PATH`，当前 `http://127.0.0.1:8666`，并去掉 `/api`。容器中的 Nginx 做同样的反代。前端 URL 不写主机名。

| 前缀 | 下游 |
|------|------|
| `/auth/**` | `bubble-auth` |
| `/admin/**` | `bubble-biz-backend` |
| `/gen/**` | `bubble-codegen` |
| `/job/**` | `bubble-quartz` |
| `/agi/**` | `bubble-biz-agi` |

下游使用 `lb://服务名`。没有实例时返回 503。新增服务要同时增加路由和前端前缀。

开发请求发到 8888，由 Vite 转发，通常不触发跨域。生产环境页面与网关不同源时，在网关配置中允许管理端来源以及 `Authorization`、`Enc-Flag`。不要在每个 Controller 上再写 CORS。

网关拒绝非法或重复的 Host，去除外部 `from` 头，并拒绝 absolute-form 请求目标。Nginx 同时做 Host 规范化和安全响应头收口。不要把 8666 直接暴露到公网。

网关放行登录、验证码和文档等匿名路径，名单在 Nacos。业务进程仍要作为资源服务器校验 Token。令牌失效时返回 **424**，前端据此重新登录。

Sentinel 可按路由限流。下游超时表现为 504，先确认实例已注册且接口本身没有长时间锁等待。OpenAPI 由各服务的 `@EnableDoc` 提供，网关做分组。某个分组缺失只说明该服务没启动。调试仍需 Bearer Token，见[平台能力](/wiki/development/platform)。

## 认证与鉴权 {#auth}

`bubble-auth` 签发令牌。业务服务只校验令牌和权限。客户端与密钥的取值见[账号](/wiki/guide/#accounts)。

密码模式必须带图形验证码：

1. HTTP Basic 为 `clientId:clientSecret` 的 Base64。
2. 请求体是表单，`Content-Type` 为 `application/x-www-form-urlencoded`。
3. 密码先用 16 位密钥做 AES。
4. 查询参数包含 `username`、`grant_type=password`、`scope`、`randomStr`、`code`。
5. 认证中心核对 Redis 验证码、客户端表和 bcrypt 密码。

成功响应包含 `access_token` 与 `refresh_token`。刷新仍走 `/auth/oauth2/token`，`grant_type=refresh_token`。短信和社交登录使用 `grant_type=mobile`，`mobile` 参数带渠道前缀。只改前端或只改库表时，表现为客户端不合法或密码错误。令牌续期校验使用 `POST /auth/token/check_token`，令牌放在表单体中，不再放入 URL。

| 键 | 作用 |
|----|------|
| `bubble-cloud::token::access_token` | 访问令牌 |
| `bubble-cloud::DEFAULT_CODE_KEY:` | 验证码 |
| `bubble-cloud::user_details` | 用户信息 |
| `bubble-cloud::menu_details` | 按角色缓存的菜单 |

有效期以签发时的客户端配置为准。可以删除 `menu_details`。不要整段删除 `bubble-cloud::token::`。

业务请求携带 `Authorization: Bearer <access_token>`。权限注解 `@HasPermission` 与 `sys_menu.permission`、前端 `v-auth` 一致。服务间调用使用 `@Inner`，由 Feign 带上内部标识。当前用户从安全工具类读取，不在 Controller 里解析 JWT。

浏览器不再接受 URL 中的 `access_token` / `refresh_token`。WebSocket 无法自定义请求头，因此只在 Upgrade 握手阶段允许 `token` Cookie；普通 HTTP 请求仍只接受 Bearer Header，避免 Cookie 被跨站请求滥用。

验证码、登录、部分文档和健康检查保持匿名，名单在 Nacos。不要留下永久放开的调试接口。HTTP 状态的完整对照见[参考](/wiki/reference/#errors)。

## 上游同步 {#upstream}

下一次合并从下面两条提交之后开始，不要把这两条再合一次。

| 目录 | 提交 | 日期 | 说明 |
|------|------|------|------|
| `apps/bubble-cloud` | `768a2a8a56ea25ecd32364137dbee2fa2a03eb46` | 2026-09-20 | `merge: sync dev into master` |
| `apps/bubble-ui` | `ff6ec3f6ff27f2b537e73d21405e601d9fa28a24` | 2026-09-20 | `merge: sync dev into master` |

该基线对应 Spring Boot 4.1.1、Spring Cloud 2025.1.3、Spring Cloud Alibaba 2025.1.0.0、Vue 3.5.42、Vite 8.1.5、Element Plus 2.13.7。

合并时保留：网关 `8666`、密码客户端 `bubble:bubble`、密钥 `thanks,bubbleyes`、Nacos 命名空间 `bubble`、Redis 前缀 `bubble-cloud::`、`bubble-biz-agi` 与菜单「AI大模型」（`/agi`）、库名 `bubble` 与 `bubble_config`。已删除的 OA 菜单不要加回。

步骤：以上表为起点取出上游 `master` 的后续差异；先合后端并执行 `mvn clean install -DskipTests`；再合前端并执行 `npm run build`；检索端口、客户端和缓存前缀是否被覆盖；把新提交号写回本节，并记入[变更日志](/wiki/changelog/)。规范冲突时先改 `.cursor/rules/`。

## 测试 {#testing}

| 改动 | 最低验证 |
|------|----------|
| Java 编译与依赖 | `mvn -pl 模块 -am compile`；格式使用 `mvn spring-javaformat:apply` |
| 登录、菜单、权限 | 管理端走通目标页，查看状态码 |
| 网关路由 | 经 `localhost:8888/api` 确认落到预期服务 |
| 前端页面 | 打开、保存、刷新后数据仍在 |
| SQL 增量 | 在已有库执行 `upgrade-4.2.sql` |

单元测试使用 JUnit 5。尚未统一接入 Testcontainers、CI 和 Playwright。改列表、弹窗、上传或路由时，在开发服务器上完成一次真实操作。

比对接口时看 `code`、`msg` 和 `data`。403 是没有菜单权限，424 是登录态失效。待补充的自动测试与性能基线写入本节后再记入变更日志。
