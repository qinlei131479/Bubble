# 变更日志

> 遵循 [Keep a Changelog](https://keepachangelog.com/zh-CN/) 格式，版本号遵循 [语义化版本](https://semver.org/lang/zh-CN/)。

## [3.9.2] - 2026-10-06

项目版本号仍是 `3.9.2`。这一轮把后端和前端对齐到上游 4.1，并停掉陀螺匠 OA。

### 升级

- 后端对齐 [pig](https://github.com/pig-mesh/pig) `768a2a8a56ea25ecd32364137dbee2fa2a03eb46`：Spring Boot 4.1.1、Spring Cloud 2025.1.3、Spring Cloud Alibaba 2025.1.0.0
- 前端对齐 [pig-ui](https://github.com/pig-mesh/pig-ui) `ff6ec3f6ff27f2b537e73d21405e601d9fa28a24`：Vite 8.1、Vue 3.5.42、Element Plus 2.13.7，Node.js >= 20.19
- 当前开发分支改为 `JDK17_master_4.1`。下次同步起点见 [上游同步基线](/wiki/architecture/upstream-sync)

### 变更

- 代码生成的数据表列表按当前所选数据源查询
- 网站配置一次提交。登录失败锁定次数、密码过期天数写入 `sys_public_param`；`public_value` 放宽到 `varchar(2000)`；上传大小上限 10MB
- 用户管理编辑保存时，列表已有数据不再盖白色加载遮罩
- 开发平台父菜单跳到第一个可见子菜单，避免打开隐藏的生成页

### 暂停

- 陀螺匠 OA 迁移停止。`bubble-biz-oa` 只保留启动类，`bubble-api-oa` 没有业务代码
- 系统菜单中的 OA 树（根路径 `/oa` 及其下级，共 113 条）已逻辑删除，角色关联已去掉。`AI大模型`（`/agi`）保留
- `docs/wiki/plans/` 里的 10 个阶段文档改为历史记录，不再作为当前开发任务

---

## [3.9.2] - 2026-04-14

### Monorepo 改造

- 仓库目录重构为 `apps/ + docker/ + docs/ + script/` Monorepo 结构
- Docker 前后端 compose 合并为统一 `docker-compose.yml`
- `.cursor/rules/` 重构：精简 alwaysApply 规则，新增 `project-context.mdc`
- VitePress 知识库骨架搭建

### 后端

- OA 迁移持续推进中（PHP Laravel 9 → Java Spring Boot 3）
- 当前进度：80 Controller / ~300 Endpoint，约 45% 真实逻辑

### 前端

- Vue 3.5 + Element Plus 2.13 稳定运行

---

## [3.9.1] - 2026-03-30

### 新增

- OA 迁移主计划 v2.0 发布（10 阶段拆分）
- `bubble-biz-agi` AI 智能体业务模块

### 变更

- Spring Cloud 升级至 2025.0.1
- Spring Cloud Alibaba 升级至 2025.0.0.0

---

## 版本规划

| 版本 | 计划内容 | 状态 |
|------|----------|------|
| 未排期 | `apps/agentic` Python 智能体服务 | 规划中 |
| 未排期 | `bubble-biz-flow` 工作流 | 仅启动类 |
| 已暂停 | 陀螺匠 OA 迁移（原 3.10 / 3.11 计划） | 2026-10-06 停止，历史计划仍在 `wiki/plans/` |

---

_更早的变更记录待整理补充。_
