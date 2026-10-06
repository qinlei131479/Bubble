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

## 文档

知识库本地预览：

```bash
cd docs && npm install && npm run dev
```

建议顺序：项目介绍、快速开始、架构总览、网关与认证、后端或前端开发、部署与常见问题。站点导航与此一致。
