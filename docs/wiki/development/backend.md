# 后端开发

改 Java 之前先看 `.cursor/rules/backend-conventions.mdc`。这一页讲模块怎么落、接口怎么写、本地怎么验证。

## 环境

- JDK 17+，Maven 3.9+
- IDE 使用 IntelliJ IDEA 即可
- 格式化：在 `apps/bubble-cloud` 执行 `mvn spring-javaformat:apply`
- 缩进使用制表符，和 Spring Java Format 一致

基础设施和启动顺序见 [快速开始](/wiki/guide/#quick-start)。

## 代码分层

```text
bubble-api-{模块}     实体、DTO、Feign。其它服务只能依赖这里
bubble-biz-{模块}     Controller、Service、Mapper、本服务的 XML
bubble-common-*       与具体表无关的能力
```

Controller 返回 `R<T>`。成功用 `R.ok(data)`，失败用 `R.failed(msg)` 或 `R.failed(ErrorCodes.XXX)`。不要再增加另一套状态字段。

Controller 可以写简单的 `Wrappers` 条件。有事务、多表或远程调用时放进 `ServiceImpl`。不要把 Mapper 注进 Controller。Mapper 继承 MyBatis-Plus `BaseMapper`，Service 继承 `IService` / `ServiceImpl`。大段 SQL 写在 XML 里，不写在 `@Select` 上。

主键和时间在 Java 里用包装类型。空的 `Long` 应保持 null，不要变成 0 去更新。

## 新增服务

1. 父 POM 的 `<modules>` 注册该目录。
2. 对外 jar 的版本写进 `bubble-common-bom`。
3. 启动类同时具备资源服务器、Feign 扫描和 `@EnableDoc`。
4. `application.yml` 只放端口、应用名和 Nacos。端口先登记到 [端口表](/wiki/architecture/#ports)。
5. Nacos 命名空间 `bubble` 中有 `{服务名}-dev.yml`，网关有对应前缀。
6. `logback-spring.xml` 和 `Dockerfile`。镜像从 `target/*.jar` 复制，先 `mvn package`。

## 常用注解

| 注解 | 作用 |
|------|------|
| `@EnableCustomResourceServer` | 当前进程校验 Bearer Token |
| `@EnableCustomFeignClients` | 扫描 Feign |
| `@EnableDoc("名称")` | 打开该服务的 OpenAPI |
| `@Inner` | 只允许服务间调用 |
| `@HasPermission("标识")` | 和 `sys_menu.permission` 对应 |
| `@SysLog("做了什么")` | 异步写入 `sys_log` |
| `@Cacheable` / 缓存常量 | 键必须带 `bubble-cloud::` 前缀 |

权限标识用现有的 `模块_资源_动作` 风格，例如用户查询、删除。新增按钮时，菜单数据和注解一起加，否则界面有按钮、接口 403。

## 标准 CRUD

系统管理接口已经形成这些路径，新接口尽量对齐：

| 动作 | 方法 | 示例 |
|------|------|------|
| 分页 | GET | `/admin/user/page` |
| 详情 | GET | `/admin/user/details/{id}` |
| 新增 | POST | `/admin/user` |
| 修改 | PUT | `/admin/user` |
| 删除 | DELETE | `/admin/user`，body 为 id 数组 |

分页参数是 `current` 和 `size`。列表条件用实体字段或单独的查询对象，不要把分页对象和实体字段混成无法文档化的 Map，除非现有接口已经这样并且只是在修 bug。

## 缓存

菜单、用户、字典有缓存。改完数据要删对应缓存，或者走已经会删缓存的 Service 方法。手工清理时：

- 可以删 `bubble-cloud::menu_details`
- 不要删 `bubble-cloud::token::`

验证码键是 `bubble-cloud::DEFAULT_CODE_KEY:`。登录问题先看这个键在不在，再看认证日志。

## 文件和参数

上传大小在 `bubble-biz-backend` 的 `spring.servlet.multipart`，当前上限 10MB。这个值在进程启动时绑定，改 yml 后要重启。`sys_public_param.public_value` 长度是 2000，网站配置里的长文本放得下。

系统内置参数不能走普通「按 id 更新」。网站配置写入 `LOGIN_ERROR_TIMES` 和 `PASSWORD_EXPIRE_DAYS` 时按 key 更新；缺行则插入。

## 前后端联调

前端 API 在 `apps/bubble-ui/src/api`。路径不要带 `/api`，那个前缀是 Vite 和 Nginx 加的。

菜单的 `path` 和 `component` 必须能对上 `src/views` 下的文件。父菜单的重定向应跳到第一个未隐藏的子菜单。

## 验证

至少做这些事再认为改动完成：

1. `mvn -pl 你改的模块 -am compile` 通过。
2. 重启该进程，确认 Nacos 里是新实例。
3. 用管理端把接口走一遍：成功、空数据、无权限。
4. 如果动了菜单或公共参数，刷新后看缓存是否更新。
