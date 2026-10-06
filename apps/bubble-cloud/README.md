## Bubble-Cloud

Java 17 + Spring Boot 4.1.1 + Spring Cloud 2025.1.3 的微服务聚合工程。当前开发分支是 `JDK17_master_4.1`，`${revision}` 为 `4.0.0`。

### 模块

```text
bubble-cloud/
├── bubble-gateway/          # Spring Cloud Gateway (:8666)
├── bubble-auth/             # OAuth2 认证中心 (:8766)
├── bubble-api/              # 实体、DTO、Feign，jar，不单独启动
│   ├── bubble-api-backend
│   └── bubble-api-agi
├── bubble-biz/
│   ├── bubble-biz-backend   # 用户权限与系统配置 (:8801)
│   └── bubble-biz-agi       # 模型、知识库、对话 (:8805)
├── bubble-common/           # 跨模块组件，见下方清单
└── bubble-visual/
    ├── bubble-codegen       # 代码生成 (:8901)
    ├── bubble-monitor       # Spring Boot Admin (:8902)
    └── bubble-quartz        # 定时任务 (:8903)
```

`bubble-common` 子模块都是 jar：

| 模块 | 作用 |
|------|------|
| bubble-common-bom | 依赖版本 |
| bubble-common-core | 工具、`R`、缓存键、错误码 |
| bubble-common-data | 数据源与多租户基础 |
| bubble-common-datasource | 动态数据源 |
| bubble-common-mybatis | MyBatis-Plus 封装 |
| bubble-common-security | 资源服务器、`@Inner`、`@HasPermission` |
| bubble-common-feign | Feign 与 Sentinel |
| bubble-common-log | `@SysLog` |
| bubble-common-oss | 文件与对象存储 |
| bubble-common-swagger | SpringDoc |
| bubble-common-xss | XSS 过滤 |
| bubble-common-excel | Excel 导入导出 |
| bubble-common-websocket | WebSocket |
| bubble-common-seata | Seata 封装 |
| bubble-common-sentinel | 限流熔断 |

### 本地启动

环境、数据库脚本和启动顺序见 [快速开始](../../docs/wiki/guide/index.md#quick-start)。在本目录执行 `mvn clean install -DskipTests` 后，按网关、认证、系统业务的顺序启动，其余服务按需启动。

`application.yml` 只声明端口、应用名和 Nacos 地址。业务配置在 Nacos，见 [注册与配置](../../docs/wiki/architecture/index.md#nacos)。

### 相关文档

- 端口：[docs/wiki/architecture/index.md#ports](../../docs/wiki/architecture/index.md#ports)
- 职责：[docs/wiki/architecture/index.md#modules](../../docs/wiki/architecture/index.md#modules)
- 开发约定：`.cursor/rules/backend-conventions.mdc`
- 网关与登录：[docs/wiki/architecture/index.md#gateway](../../docs/wiki/architecture/index.md#gateway)、[docs/wiki/architecture/index.md#auth](../../docs/wiki/architecture/index.md#auth)
