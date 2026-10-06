---
layout: home
hero:
  name: Bubble
  text: 智能体原生平台
  tagline: Java 微服务管理端，加上规划中的智能体运行时
  actions:
    - theme: brand
      text: 快速开始
      link: /wiki/guide/#quick-start
    - theme: alt
      text: 架构总览
      link: /wiki/architecture/#overview
features:
  - title: 微服务后端
    details: Spring Boot 4.1.1、Spring Cloud 2025.1.3。网关 8666，认证、系统业务、AGI、代码生成、监控、定时任务分进程部署。
    link: /wiki/development/backend
  - title: Vue 3 管理端
    details: Vue 3.5、Element Plus 2.13、Vite 8。开发端口 8888，请求经 /api 转到网关。
    link: /wiki/development/frontend
  - title: 认证与网关
    details: Spring Authorization Server 发 Token，Gateway 统一入口。客户端 bubble:bubble，缓存前缀 bubble-cloud::。
    link: /wiki/architecture/#auth
  - title: 部署
    details: Docker Compose 拉起基础设施和默认业务进程。已有库走增量脚本，不重导全量 SQL。
    link: /wiki/ops/#docker
---

## 阅读顺序

当前分支 `JDK17_master_4.1`，版本号 `4.0.0`。技术栈见 [项目介绍](/wiki/guide/#stack)，启动见 [快速开始](/wiki/guide/#quick-start)，登录信息见 [账号与客户端](/wiki/guide/#accounts)。请求路径见 [网关](/wiki/architecture/#gateway) 与 [认证与鉴权](/wiki/architecture/#auth)。改代码前阅读对应的开发页；无法启动时看 [常见问题](/wiki/ops/#troubleshooting)。
