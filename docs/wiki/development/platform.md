# 平台能力

这些能力已经在公共模块和可视化服务里，业务代码直接用，不要在每个服务里重写一套。

## 接口文档

服务启动类上的 `@EnableDoc("名称")` 会暴露该进程的 OpenAPI。网关把分组收在 8666 上。调试时：

1. 先登录管理端，拿到访问令牌。
2. 文档页的 Authorize 填 `Bearer <token>`。
3. 密码模式本身要用表单和 Basic，不走 JSON Body。

服务没注册时，网关分组里看不到它。这和文档注解无关。

## Feign

跨服务调用的接口定义在 `bubble-api-*`，实现留在提供方。调用方只依赖 api jar。

内部接口标 `@Inner`。Feign 需要带上内部调用标记，否则提供方当匿名请求拒绝。超时和降级使用 `bubble-common-feign` 里已经接好的 Sentinel，不要在业务里手写无限重试。

## 缓存

Redis 键集中在 `CacheConstants`，前缀 `bubble-cloud::`。新键继续挂在这个前缀下面，避免和本机其它项目冲突。

管理端有缓存查看页，对应工具里的数据监控。删除键之前看清前缀。`token::` 是登录态，`menu_details` 是菜单，`DEFAULT_CODE_KEY:` 是验证码。

## 操作日志和审计

需要留痕的方法加 `@SysLog("简短描述")`。日志异步进 `sys_log`，失败不应把业务事务回滚。管理端在系统日志页面按人和时间查询。

## 校验和异常

入参用 Bean Validation。字段错误由全局异常处理转成 `R.failed`，`code` 为 1，`msg` 给前端展示。不要在 Controller 里 catch 后返回另一套 JSON。

错误文案需要中英文时，使用 `ErrorCodes` 和国际化资源，见 [错误码](/wiki/reference/#errors)。

## XSS

`bubble-common-xss` 过滤请求中的 HTML。富文本字段如果必须保留标签，使用已经约定的忽略方式，不要关掉整个过滤。

## 文件

上传接口在 backend，元数据进 `sys_file`，内容进 S3 兼容存储或本地目录，具体由 Nacos 里的 OSS 配置决定。前端使用 `src/components` 里的上传组件，限制类型和大小。服务端 `max-file-size` 和 `max-request-size` 目前是 10MB。

## 定时任务

`bubble-quartz` 提供 `/job/sys-job`。任务要是 Spring Bean 里的公开方法，类名和方法名写在任务定义里。执行记录在 `sys_job_log`。Quartz 的集群表是 `QRTZ_*`，不要手改触发器行。

改任务后在管理端执行一次，确认日志里有成功或明确的异常，而不是只看到「保存成功」。

## 代码生成

`bubble-codegen` 从 `gen_datasource_conf` 选择数据源，再读表结构。前端在「开发平台」。

使用时：

1. 先在数据源管理里保存连接，并确认能连上。
2. 导入表时带上当前数据源。切换数据源后列表必须重新查询。
3. 打开设计页时 `dsName` 和 `tableName` 都要有。缺任何一个就不要请求 `/gen/table/`。
4. 模板版本检查同样要求这两个参数，否则会弹出与当前表无关的更新提示。
5. 生成代码是起点。包名、作者和权限标识按本仓库规范改完再提交。

## 动态数据源

代码生成和需要切换库的功能使用 `bubble-common-datasource`。切换只包住必要的查询，用完回到主库。主库仍是 `bubble`。不要在一个事务里跨两个数据源更新。

## 监控

`bubble-monitor` 是 Spring Boot Admin。本地进程端口 8902。各业务服务要能被 Nacos 发现，监控台才能列出实例。日志级别可以在实例页面调整，重启后回到 logback 配置。

容器端口映射和进程端口目前不一致，说明见 [端口与服务](/wiki/architecture/#ports)。
