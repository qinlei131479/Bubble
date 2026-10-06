# Bubble 知识库

正文在 `wiki/`，用 [VitePress](https://vitepress.dev/) 发布。`.vitepress/config.ts` 把 `wiki/index.md` 重写成站点根路径 `/`，其余文章仍以 `/wiki/...` 访问。

阅读顺序是：版本与启动、架构与网关、认证、开发、部署与排错。同一事实只在一页维护，其它页面用链接指向它。

## 目录

```text
docs/
├── .vitepress/config.ts
├── package.json
└── wiki/
    ├── index.md                 # 首页
    ├── guide/index.md           # 技术栈、结构、启动、账号
    ├── architecture/index.md    # 端口、模块、库表、网关、认证、同步
    ├── development/             # 后端、前端、平台能力、智能体
    ├── ops/index.md             # 容器、脚本、监控、常见问题
    ├── changelog/
    └── reference/index.md       # 环境变量、错误码、API 约定
```

端口与模块边界维护在 `wiki/architecture/index.md`。`.cursor/rules/` 只放通用开发约束，不承载某个业务的实施计划。

## 本地预览

```bash
cd docs
npm install
npm run dev
```

默认端口见 `package.json`（`vitepress dev --port 5176`）。

## 写作

- 一篇文章讲清一件事：背景、本仓库里的真实入口、操作步骤、失败时去哪一页。
- 命令、端口、客户端、分支以代码和脚本为准，改代码时同步改对应文章。
- 链接使用 `/wiki/...`，不要写成去掉 `wiki` 的短路径。
