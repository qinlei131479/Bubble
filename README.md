<p align="center" style="font-family: '楷体'; font-size: 30px;">智能如泡，聚变未来</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-17-orange" alt="Java 17">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.1.1-brightgreen" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Spring%20Cloud-2025.1.3-brightgreen" alt="Spring Cloud">
  <img src="https://img.shields.io/badge/Vue-3.5-blue" alt="Vue">
  <img src="https://img.shields.io/badge/revision-4.0.0-blue" alt="revision">
</p>

Bubble 是智能体原生平台。Java 微服务提供网关、认证、系统管理和 AGI 接口，Vue 3 管理端负责操作界面。Python 智能体运行时仍在规划，目录为 `apps/agentic`。当前开发分支是 `JDK17_master_4.1`，Maven 坐标 `com.bubblecloud:Bubble-Cloud`，`${revision}` 为 `4.0.0`。

## 仓库结构

```text
Bubble/
├── apps/
│   ├── bubble-cloud/     # Java 微服务
│   ├── bubble-ui/        # Vue 3 管理端
│   └── agentic/          # Python 智能体（未实现）
├── docker/               # 编排与镜像，不含业务代码
├── docs/                 # VitePress 知识库
├── script/               # 数据库脚本与 JAR 部署
└── .cursor/rules/        # 通用开发约束
```

端口、模块边界和启动步骤不在本文展开：

| 内容 | 位置 |
|------|------|
| 端口 | [端口与服务](docs/wiki/architecture/index.md#ports) |
| 模块边界 | [模块职责](docs/wiki/architecture/index.md#modules) |
| 技术栈与分支 | [项目介绍](docs/wiki/guide/index.md#stack) |
| 初始化与启动 | [快速开始](docs/wiki/guide/index.md#quick-start) |
| 登录与客户端 | [账号与客户端](docs/wiki/guide/index.md#accounts) |
| 容器 | [Docker 部署](docs/wiki/ops/index.md#docker) |

各应用的模块说明：

- [apps/bubble-cloud/README.md](apps/bubble-cloud/README.md)
- [apps/bubble-ui/README.md](apps/bubble-ui/README.md)
- [docker/README.md](docker/README.md)
- [apps/agentic/README.md](apps/agentic/README.md)

## 版本

| 分支 | JDK | Spring Boot | 状态 |
|------|-----|-------------|------|
| `JDK17_master_4.1` | 17+ | 4.1.1 | 当前开发 |
| `JDK17_master` | 17+ | 3.5.x | 维护 |
| `JDK8_master` | 8 | 2.7.x | 存量兼容 |

与上游对齐时，从 [上游同步基线](docs/wiki/architecture/index.md#upstream) 记录的提交之后继续。网关端口、OAuth 客户端、缓存前缀和 AGI 模块保持仓库内现状。

## 构建与运行

依赖 JDK 17、Maven 3.9、Node.js >= 20.19、MySQL 8、Redis 与 Nacos。完整顺序、脚本选择和启动检查见 [快速开始](docs/wiki/guide/index.md#quick-start)。

本地只拉起基础设施：

```bash
docker compose -f docker/docker-compose.yml up -d mysql redis register
```

管理端：

```bash
cd apps/bubble-ui
npm install
npm run dev
```

开发服务器默认 `http://localhost:8888`，接口经 `/api` 转发到网关 `8666`。

## 安全配置

容器部署必须先在 `docker/` 下执行 `cp .env.example .env`，再替换所有 `change-me` 值。`NACOS_USERNAME` 和 `NACOS_PASSWORD` 是 Java 服务访问 Nacos 的账号；首次初始化使用 `nacos` / `nacos` 登录控制台，创建 `.env` 中配置的新管理员并禁用默认账号，然后启动 Java 服务。不要把生产 `.env` 提交到仓库。宿主机已导出的同名环境变量优先于 `docker/.env`，部署前应确认没有残留的 `NACOS_USERNAME=nacos` 或 `NACOS_PASSWORD=nacos`。

首次启动顺序：

```bash
cd docker
docker compose up -d mysql redis register
# 访问 http://127.0.0.1:8848/nacos，用 nacos/nacos 登录，
# 创建 .env 中的管理员，再退出并用新账号禁用/修改默认账号。
docker compose up -d
```

| 配置 | 必填 | 说明 |
|------|------|------|
| `NACOS_USERNAME` | 是 | Java 服务和 Nacos 控制台账号，生产环境必须更换默认账号 |
| `NACOS_PASSWORD` | 是 | 与 Nacos 用户表中的 bcrypt 密码匹配；首启后立即轮换 |
| `NACOS_AUTH_IDENTITY_KEY` | 是 | Nacos 服务间身份 Header 名，每个环境独立设置 |
| `NACOS_AUTH_IDENTITY_VALUE` | 是 | Nacos 服务间身份 Header 值，必须使用随机值 |
| `NACOS_AUTH_TOKEN` | 是 | Base64 编码且解码后不少于 32 字节的随机密钥 |
| `CODE_GEN_ALLOWED_OUTPUT_ROOTS` | 否 | 允许代码生成写入的目录根；未配置时默认限制在当前 Git 仓库 |

升级影响：浏览器不再接受 URL 中的 `access_token` / `refresh_token`；`check_token` 改为 `POST` 表单提交；WebSocket 使用同站 `token` Cookie；CORS 默认关闭；Actuator 只暴露健康检查。老客户端或外部脚本未同步调整时会出现登录、令牌校验或 WebSocket 连接失败。

### AGI 内网数据源

开关写在 Nacos 的 `bubble-biz-agi-dev.yml`：

```yaml
agi:
  datasource:
    allow-private-network: true
```

未配置时默认为 `true`。在 Nacos 中修改后会刷新到 `bubble-biz-agi`，不必重启。回环、私网和共享地址段允许访问；链路本地、组播和云厂商元数据地址始终拒绝。

### 凭证展示与编辑

OAuth 客户端密钥和 AI 供应商 API Key 不再通过管理端查询、详情或导出接口返回明文。页面显示 `******` 表示已配置，显示“未配置”表示没有可用凭证。编辑表单回填 `******`；提交空白或 `******` 时后端保留原凭证，提交新的真实值时才替换。AI 模型选择已有凭证的供应商创建时，可以直接提交掩码并复用供应商凭证；API 域名不是凭证，仍按普通字段展示和编辑。

`******` 是保留哨兵值，不要把真实凭证设置为这 6 个字符。

## 文档

知识库本地预览：

```bash
cd docs && npm install && npm run dev
```

建议顺序：项目介绍、快速开始、架构总览、网关与认证、后端或前端开发、部署与常见问题。站点导航与此一致。
