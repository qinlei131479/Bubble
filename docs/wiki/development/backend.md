# 后端开发

## 开发环境

- **JDK**: 17+
- **Maven**: 3.9+
- **IDE**: IntelliJ IDEA（推荐）
- **代码格式**: Spring Java Format（`mvn spring-javaformat:apply`）

## 模块分层

```text
bubble-api-{module}      → 实体、DTO/VO、Feign（jar，不可独立运行）
bubble-biz-{module}      → Controller / Service / Mapper（可部署）
bubble-common-{feature}  → 跨模块组件
bubble-visual-{feature}  → 可视化管理服务
```

## 新增模块清单

1. 父 POM `<modules>` 注册
2. `bubble-common-bom` 版本管理
3. 可部署服务：`application.yml` + `logback-spring.xml` + `Dockerfile`
4. 启动类：`@EnableDoc("模块名")` + `@EnableCustomFeignClients` + `@EnableCustomResourceServer`
5. Nacos 配置：`{spring.application.name}-{profile}.yml`

## 关键注解

| 注解 | 用途 | 所在模块 |
|------|------|----------|
| `@EnableCustomResourceServer` | OAuth2 资源服务器 | common-security |
| `@EnableCustomFeignClients` | Feign 扫描 | common-feign |
| `@EnableDoc("name")` | SpringDoc | common-swagger |
| `@Inner` | 内部调用免鉴权 | common-security |
| `@SysLog("描述")` | 操作日志 | common-log |
| `@HasPermission` | 方法权限 | common-security |

## 统一返回

- `R<T>`：成功 `R.ok(data)`，失败 `R.failed(msg)`

陀螺匠 OA 迁移已暂停，不再新增 `R.phpOk` / `R.phpFailed` 这一类 PHP 兼容返回。

## 编码规范

详见 `.cursor/rules/backend-conventions.mdc`，包含 Controller / ServiceImpl CRUD 模板和 Javadoc 要求。某个业务模块自己的实施计划写在 `docs/wiki/plans/`，不放进通用规范。
