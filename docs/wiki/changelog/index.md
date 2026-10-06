# 变更日志

版本号是 Maven `${revision}`，当前 **4.0.0**。它表示本仓库的发布号，与 Spring Boot 4.1 的框架版本不是同一个数。

## [4.0.0] - 2026-10-06

### 移除

- 删除 `bubble-biz-oa`、`bubble-biz-flow`，以及 `bubble-api-oa`、`bubble-api-flow`。父 POM 和 `bubble-common-bom` 不再登记这四个模块。
- 删除 `docs/wiki/plans/` 下的 OA 迁移计划。后续如果做新业务，另写计划，不恢复这套文档。

### 升级

- 后端对齐上游 `768a2a8a56ea25ecd32364137dbee2fa2a03eb46`：Spring Boot 4.1.1、Spring Cloud 2025.1.3、Spring Cloud Alibaba 2025.1.0.0。
- 前端对齐上游 `ff6ec3f6ff27f2b537e73d21405e601d9fa28a24`：Vite 8.1、Vue 3.5.42、Element Plus 2.13.7，Node.js >= 20.19。
- 开发分支为 `JDK17_master_4.1`。下次同步起点见 [上游同步基线](/wiki/architecture/#upstream)。

### 变更

- 项目版本号由 3.9.2 调整为 4.0.0。
- 指南、架构、运维、参考各自合并为一篇，小节用页内锚点区分。
- 代码生成的数据表列表按当前数据源查询。打开生成页时没有数据源名或表名则不再请求。
- 网站配置一次提交。`LOGIN_ERROR_TIMES`、`PASSWORD_EXPIRE_DAYS` 写入 `sys_public_param`。`public_value` 为 `varchar(2000)`。上传上限 10MB。
- 用户列表在已有数据时不再盖白色加载遮罩。
- 开发平台父菜单跳到第一个未隐藏的子菜单。
- 知识库改为按入门、架构、网关、认证、开发、部署和排错组织。端口与客户端以仓库内配置为准。

### 菜单

系统菜单中的 OA 目录此前已从库里逻辑删除，`AI大模型`（`/agi`）保留。这次没有再改菜单 SQL。

---

## [3.9.2] - 2026-04-14

- 仓库改为 `apps/`、`docker/`、`docs/`、`script/`。
- 前后端 compose 合并为一个文件。
- 搭好 VitePress 知识库和 `.cursor/rules/`。

当时还在迁移一套办公业务。该方向已经取消，代码和计划文档在 2026-10-06 删除。

---

## [3.9.1] - 2026-03-30

- 增加 `bubble-biz-agi`。
- Spring Cloud 升到 2025.0.1，Spring Cloud Alibaba 升到 2025.0.0.0。

---

## 后续事项

| 项 | 状态 |
|----|------|
| `apps/agentic` Python 智能体 | 只有说明，没有进程 |
| 监控容器端口与 8902 对齐 | compose 仍映射 5001 |
| 提交时自动跑测试 | 未接入 |
