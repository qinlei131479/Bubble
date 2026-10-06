# 上游同步基线

下次同步从下面两条提交**之后**开始，不要重复合并这些提交本身。

| 方向 | 上游仓库 | 提交 |
| --- | --- | --- |
| 后端 `apps/bubble-cloud` | [pig-mesh/pig](https://github.com/pig-mesh/pig) `master` | `768a2a8a56ea25ecd32364137dbee2fa2a03eb46` |
| 前端 `apps/bubble-ui` | [pig-mesh/pig-ui](https://github.com/pig-mesh/pig-ui) `master` | `ff6ec3f6ff27f2b537e73d21405e601d9fa28a24` |

两条都是 2026-09-20 的 `merge: sync dev into master`。本地保留 Bubble 的网关端口 `8666`、OAuth 客户端，以及 AGI 菜单（`AI大模型`，路径 `/agi`）。陀螺匠 OA 菜单树（根路径 `/oa`）已从系统菜单删除，不再随上游同步保留。
