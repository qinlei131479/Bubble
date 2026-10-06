## 模块职责与边界

端口以 [端口与服务](/wiki/architecture/ports) 为准，这里只写职责。

### `apps/bubble-cloud`（后端）

Java 17 微服务聚合工程，当前版本线是 Spring Boot 4.1.1、Spring Cloud 2025.1.3。

| 模块 | 职责 |
|------|------|
| `bubble-gateway` | 统一入口、路由 |
| `bubble-auth` | OAuth2 认证授权 |
| `bubble-api-*` | 实体、DTO、Feign 接口，打成 jar，不单独启动 |
| `bubble-biz-backend` | 用户、权限、网站配置等系统业务 |
| `bubble-biz-agi` | 智能体、模型、知识库、对话等 AGI 业务 |
| `bubble-biz-oa` | 陀螺匠 OA。迁移已暂停，目录里只保留启动类，`bubble-api-oa` 没有业务代码 |
| `bubble-biz-flow` | 工作流占位，仅启动类 |
| `bubble-visual-*` | 代码生成、监控、定时任务 |
| `bubble-common-*` | 跨模块组件 |

文档入口：`apps/bubble-cloud/README.md`

### `apps/bubble-ui`（前端）

Vue 3.5 + Element Plus 2.13 + Vite 8 的管理端。页面、路由、权限和主题都在这里。Node.js 需要 >= 20.19。

文档入口：`apps/bubble-ui/README.md`

### `apps/agentic`（规划中）

Python 智能体服务，尚未实现。不和 Java 模块共用目录，只通过 API 交互。接入时需要独立 README、依赖文件和最小启动方式。
