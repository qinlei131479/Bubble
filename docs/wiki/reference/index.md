# 参考

登录相关变量的取值见[入门 · 账号与客户端](/wiki/guide/#accounts)。Nacos 的用户、密码和命名空间见[架构 · 注册与配置](/wiki/architecture/#nacos)。路径转发见[网关](/wiki/architecture/#gateway)。

## 环境变量 {#env}

修改任何 `VITE_` 变量后重启 Vite。文件为 `apps/bubble-ui/.env` 与 `.env.development`。

| 变量 | 默认 | 作用 |
|------|------|------|
| `VITE_PORT` | `8888` | 开发服务器端口 |
| `VITE_OPEN` | `true` | 启动时打开浏览器 |
| `VITE_API_URL` | `/api` | 请求前缀，由 Vite 或 Nginx 去掉后再转发 |
| `VITE_ADMIN_PROXY_PATH` | `http://127.0.0.1:8666` | 开发代理目标 |
| `VITE_PUBLIC_PATH` | `/` | 生产构建的 base |
| `VITE_IS_MICRO` | `true` | 按微服务方式组装请求 |
| `VITE_WEBSOCKET_ENABLE` | `false` | 是否接收 WebSocket |
| `VITE_REQUEST_TIMEOUT` | `50000` | 超时，毫秒 |

`VITE_PWD_ENC_KEY` 与 `VITE_OAUTH2_*` 不在此重复列出。

后端进程读取 `NACOS_HOST`（默认 `127.0.0.1`，容器内为 `bubble-register`）和 `NACOS_PORT`（默认 `8848`）。数据源不在环境变量中。compose 另传入 `MYSQL_HOST`、`REDIS_HOST`，并可覆盖 `MYSQL_PORT`（3306）、`MYSQL_ROOT_PASSWORD`（root）、`REDIS_PORT`（6379）。本地连接 compose 中的 MySQL 时，端口是 3306。

## 错误码 {#errors}

业务成功与失败看响应体，认证和权限看 HTTP 状态。

成功时 `code` 为 0。失败时 `code` 为 1，`msg` 为原因。需要稳定判断的错误使用 `ErrorCodes`，文案走国际化资源。分页数据在 `data` 中，常见字段为 `records`、`total`、`current`、`size`。

| 状态 | 含义 | 前端 |
|------|------|------|
| 200 | 传输成功。业务是否成功看 `code` | 解析 `R` |
| 401 | 未认证，或 Basic 客户端错误 | 留在登录流程 |
| 403 | 已认证但没有权限 | 提示无权限 |
| 424 | 访问令牌不能用 | 清除会话并重新登录 |
| 429 | 被限流 | 稍后重试 |
| 500 | 未处理异常 | 查看该服务日志 |
| 503 | 网关没有可用实例 | 查看 Nacos 服务列表 |
| 504 | 网关等待下游超时 | 查看下游日志 |

全局异常处理把校验错误和业务异常收成 `code = 1`。未被收住的异常才是 HTTP 500。接口 `msg` 已是目标语言时，页面不再翻译一次。

## API 约定 {#api}

浏览器只访问网关，路径前加 `/api`。服务之间走 Feign，不经过该前缀。

| 前缀 | 服务 |
|------|------|
| `/auth` | 认证。令牌端点 `/auth/oauth2/token` |
| `/admin` | 系统管理 |
| `/gen` | 代码生成 |
| `/job` | 定时任务 |
| `/agi` | 智能体业务 |

新增前缀时同时改网关路由和 `src/api`。

| 动作 | 方法 | 路径 |
|------|------|------|
| 分页 | GET | `/page`，参数 `current`、`size` |
| 详情 | GET | `/details/{id}` |
| 新增 | POST | 资源根路径，JSON |
| 修改 | PUT | 资源根路径，JSON 带主键 |
| 删除 | DELETE | 资源根路径，体为 id 数组 |

已经使用 `GET /{id}` 或在路径中带 id 的接口保持原样。

```http
Authorization: Bearer <access_token>
Content-Type: application/json
```

登录使用表单和 Basic，密码字段是加密后的字符串。需要加密的业务请求使用头 `Enc-Flag`，密钥与 `VITE_PWD_ENC_KEY` 相同。内部接口使用 `@Inner`。

`@HasPermission`、`sys_menu.permission` 和 `v-auth` 三者相同。只有前端判断时，直接调用接口仍然会执行。OpenAPI 在网关入口调试时携带 Bearer；密码模式不能用文档页默认的 JSON 试调。
