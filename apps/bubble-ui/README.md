<p align="center" style="font-family: '楷体'; font-size: 30px;">智能如泡，聚变未来</p>

## Bubble-UI

Vue 3.5 + Element Plus 2.13 + Vite 8 + TypeScript 的管理端。Node.js 需要 >= 20.19。

```bash
cd apps/bubble-ui
npm install
npm run dev
```

开发地址是 `http://localhost:8888`。`.env.development` 里的 `VITE_PORT` 可以改端口。改任何 `VITE_` 变量之后都要重启 `npm run dev`。

## 接口代理

页面里的 URL 不带主机名，也不重复写 `/api`。前缀、代理目标和 Nginx 反代见 [网关](../../docs/wiki/architecture/index.md#gateway)。网关未启动时，登录和菜单请求失败。

## 认证配置

客户端、加密密钥和种子账号见 [账号与客户端](../../docs/wiki/guide/index.md#accounts)。修改 `VITE_` 变量后需重启开发服务器。

## 目录

- `src/api`：接口函数，按 `/admin`、`/gen`、`/job`、`/agi` 分目录
- `src/views`：页面。菜单的组件路径要能匹配到这里
- `src/router/backEnd.ts`：后端菜单转成路由
- `src/components`：上传、表格、权限
- `src/theme`：全局样式

更完整的说明在知识库 [前端开发](../../docs/wiki/development/frontend.md)。编码约束是 `.cursor/rules/frontend-conventions.mdc`。
