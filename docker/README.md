## docker（统一容器化模块）

本目录统一管理 Bubble 的容器化部署：基础设施、后端微服务、前端 Nginx。

### 文件说明

```text
docker/
├── docker-compose.yml       # 全栈编排（基础设施 + 后端 + 前端）
├── bubble-ui.Dockerfile     # 前端 Nginx 镜像构建
├── nginx/
│   └── default.conf         # Nginx 配置（/api/* 反代到 gateway）
├── .env.example             # compose 环境变量示例（可选）
└── README.md
```

### 一键启动

```bash
# 全部服务
docker compose -f docker/docker-compose.yml up -d --build

# 仅基础设施（开发时常用）
docker compose -f docker/docker-compose.yml up -d mysql redis register

# 仅前端 + 网关
docker compose -f docker/docker-compose.yml up -d gateway ui
```

### 环境变量（可选覆盖）

| 变量 | 默认值 | 说明 |
|------|--------|------|
| `MYSQL_PORT` | 3306 | MySQL 对外端口 |
| `MYSQL_ROOT_PASSWORD` | root | MySQL root 密码 |
| `REDIS_PORT` | 6379 | Redis 对外端口 |
| `NACOS_USERNAME` | 无，必填 | Nacos 管理员/客户端账号 |
| `NACOS_PASSWORD` | 无，必填 | 与 Nacos 用户表 bcrypt 密码匹配 |
| `NACOS_AUTH_IDENTITY_KEY` | 无，必填 | Nacos 服务间身份 Header 名 |
| `NACOS_AUTH_IDENTITY_VALUE` | 无，必填 | Nacos 服务间身份随机值 |
| `NACOS_AUTH_TOKEN` | 无，必填 | Base64 密钥，解码后至少 32 字节 |

在 `docker/` 目录下创建 `.env` 文件即可覆盖默认值。

首次启动先执行 `docker compose up -d mysql redis register`，用种子账号 `nacos` / `nacos` 登录控制台，创建 `.env` 中配置的管理员，并禁用或强改默认账号；随后执行 `docker compose up -d` 启动 Java 服务和前端。这样可避免 Java 服务在账号创建前反复连接失败。宿主机同名环境变量优先于 `.env`。对外端口监听宿主机所有网卡。

### 约定

- `docker/` 仅承载 Dockerfile / compose / 配置，业务代码统一在 `apps/`
- 端口与服务清单以 [`docs/wiki/architecture/index.md#ports`](../docs/wiki/architecture/index.md#ports) 为单一事实源
- 网络名统一为 `bubble-net`
