# 前端开发

管理端在 `apps/bubble-ui`。规范细节在 `.cursor/rules/frontend-conventions.mdc`。这一页说明目录、请求、菜单和本地怎么对上后端。

## 技术栈

- Vue 3.5，单文件组件使用 `<script setup>`
- TypeScript，路径别名 `/@` 指向 `src`
- Element Plus 2.13，图标按组件引入
- Tailwind CSS 3.4 和 `src/theme` 里的 SCSS
- Vite 8.1，Node.js >= 20.19
- Pinia 保存用户、路由和主题
- vue-router 使用 hash
- 开发端口 8888

`npm run dev` 会读 `.env` 和 `.env.development`。代理目标只看 `VITE_ADMIN_PROXY_PATH`，当前是 `http://127.0.0.1:8666`。

## 请求封装

`src/utils/request` 把 `baseURL`（`VITE_API_URL`，值为 `/api`）加到相对路径前面，并带上 `Authorization`。Token 过期时后端给 424，拦截器清掉会话并打开登录页。

页面里写成：

```ts
request({ url: '/admin/user/page', method: 'get', params })
```

不要写成 `http://127.0.0.1:8801/...`，也不要在 url 里再加一次 `/api`。

密码登录会先加密。密钥来自 `VITE_PWD_ENC_KEY`。OAuth 的 Basic 来自 `VITE_OAUTH2_PASSWORD_CLIENT`。两边的默认值见 [账号与客户端](/wiki/guide/#accounts)。

## 目录和页面

| 路径 | 放什么 |
|------|--------|
| `src/api/admin` | 系统管理 |
| `src/api/gen` | 代码生成 |
| `src/api/agi` | 智能体业务 |
| `src/api/daemon` | 定时任务 |
| `src/views` 同名目录 | 对应页面 |
| `src/views/*/i18n` | 该页的中英文案 |
| `src/components` | 上传、表格、权限等跨页面组件 |
| `src/router/backEnd.ts` | 把后端菜单变成路由 |

新增一页：

1. 在 `src/views/{模块}/` 写页面，列表和表单分开。
2. 在 `src/api/{模块}/` 写函数，一个接口一个函数。
3. 在 `sys_menu` 增加菜单。`component` 用后端已经约定的 views 路径，让 `import.meta.glob` 能找到文件。
4. 按钮权限用 `v-auth`，标识和 `@HasPermission` 相同。

后端控制路由是默认行为。父级菜单重定向到第一个 `meta.isHide` 不为真的子节点。隐藏页如果排在第一位，会被当成落地页，生成页就会在没有数据源和表名时发请求。

## 列表页

列表使用项目里的表格封装。查询会把 `loading` 设为 true，但不会先清空 `dataList`。表格的 `v-loading` 如果在已有数据时仍打开，Element Plus 会盖一层近乎纯白的遮罩，看起来像闪一下。已有数据时不要整表盖遮罩；第一次进入、列表还是空的时候可以盖。

保存弹窗成功后先结束按钮 loading，再关弹窗，然后刷新列表。刷新用 `getDataList(false)` 保持当前页。

单选框使用 Element Plus 的 `value` 绑定选项值。`label` 只负责显示文字。继续用 `label` 当值会在控制台打出弃用警告。

## 上传

图片上传组件要限制类型和大小。网站 Logo 接受 png、jpg、jpeg、svg，大小按组件上的限制。服务端 multipart 上限是 10MB，改的是 backend 的 yml，重启后才生效。扩展名比较要忽略大小写。上传失败时关掉全屏 loading，并把计数减回去，否则按钮会停在转圈。

## 国际化和主题

框架文案在 `src/i18n`。切换语言后，页面级文案读的是该页自己的语言包。只改了框架包、没改页面包时，开关会像没有效果。

全局覆盖 Element Plus 时放在 `src/theme`。对话框的定位如果改成相对，要确认没有把表格遮罩或下拉层裁掉。

## 构建

```bash
cd apps/bubble-ui
npm install
npm run dev      # 开发
npm run build    # 产出 dist，供 docker/bubble-ui.Dockerfile 使用
```

生产构建的公共路径是 `VITE_PUBLIC_PATH`，默认 `/`。Nginx 配置在 `docker/nginx`，把 `/api` 转到网关。
