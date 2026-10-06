# 智能体

Java 侧已经有 `bubble-biz-agi`：模型供应商、知识库、对话、MCP、评测。管理端页面在 `apps/bubble-ui/src/views/agi`，接口前缀 `/agi`，进程端口 8805。

`apps/agentic` 是另外一个进程，打算用 Python 做编排和工具调用。它还没有实现，compose 里的服务块也是注释。不要把 Python 代码放进 `bubble-biz-agi`。

## 已有实现

启动 `bubble-biz-agi`，并确认网关把 `/agi/**` 转到该服务。菜单「AI大模型」指向 `/agi`。默认 Docker 编排不包含这个进程，只启动 compose 时，这个菜单会 503。

数据在业务库 `bubble`。实体和 Feign 在 `bubble-api-agi`。其它 Java 服务要调用时依赖这个 jar，不复制实体。

## 接入约束

接入时按仓库对 `apps/<name>` 的要求补齐 README、依赖文件和启动命令，并更新 [模块职责](/wiki/architecture/#modules)。

约束：

- 只通过 `http://bubble-gateway:8666` 访问 Java 能力，本地则是 `http://127.0.0.1:8666`。
- 不连接 Java 进程的内部端口，不共享 Feign。
- 写业务数据走已有 HTTP 接口。只读场景再考虑直接读库。
- 调用带用户身份的接口时，使用认证中心签发的令牌，并遵守 `@Inner` 的边界。

编码约定在 `.cursor/rules/python-conventions.mdc`。在模块真正创建之前，不在这里写虚构的目录和命令。
