# 项目介绍

## Bubble 是什么

Bubble 是面向下一代人机交互的 **智能体原生平台**，融合 LLM、RAG 检索增强与自主智能体技术，以"泡泡"为核心理念，构建轻量化、可组合、自进化的 AI 应用生态。

## 核心技术栈

| 层级 | 技术 | 版本         |
|------|------|------------|
| JDK | Java | 17+        |
| 后端框架 | Spring Boot | 4.1.1      |
| 微服务 | Spring Cloud | 2025.1.3   |
| 微服务(阿里) | Spring Cloud Alibaba | 2025.1.0.0 |
| 认证授权 | Spring Authorization Server | 随 Spring Boot 4.1 |
| ORM | MyBatis Plus | 3.5.16     |
| 注册/配置中心 | Nacos | -          |
| 网关 | Spring Cloud Gateway (WebFlux) | -          |
| 前端框架 | Vue 3 + TypeScript | 3.5.42 / 4.9 |
| UI 组件库 | Element Plus | 2.13.7     |
| CSS | Tailwind CSS + SCSS | 3.4        |
| 构建工具 | Vite | 8.1.5      |
| Node.js | Node.js | >= 20.19   |
| 数据库 | MySQL | 8.0        |
| 缓存 | Redis | -          |
| 对象存储 | S3 兼容 (AWS SDK) | -          |

## 当前开发状态

- **已完成**: backend（用户权限）、agi（智能体）、codegen（代码生成）、quartz（定时任务）、auth（认证）、gateway（网关）、monitor（监控）
- **已暂停**: bubble-biz-oa（陀螺匠 OA 迁移停止，模块只保留启动类；系统菜单中的 OA 树已删除。历史计划见 [迁移计划](/wiki/plans/oa-migration-plan)）
- **占位**: bubble-biz-flow（仅启动类）、agentic（Python 智能体，待实现）
- **当前分支**: `JDK17_master_4.1`（Spring Boot 4.1）。上游同步起点见 [上游同步基线](/wiki/architecture/upstream-sync)
