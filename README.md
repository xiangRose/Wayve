# JobSearch 后端 — 功能分包架构（P0A）

> 前端队友独立开发 `front/`（待 init），本仓库只负责 **Java 后端 + AI 能力**。

## 包结构（按业务能力，不是按 controller/service 顶层拆分）

```
src/main/java/com/Grassroot/JobSearch/
├── JobSearchApplication.java      # 启动入口
├── config/                        # 全局配置（CORS、常量）
├── common/                        # 公共工具（异常、JSON 加载、枚举）
├── session/                       # P0-1 匿名会话 + 探索设置
├── job/                           # P0-2 五岗位列表 + 推荐
├── task/                          # P0-3 微任务六步（核心）
├── report/                        # P0-4 探索报告 + 三层结果
├── llm/                           # P0-5 大模型 HTTP 客户端（Day 2 接 key）
└── ai/                            # P0-6 Prompt 编排 + 校验 + 兜底
```

**说明：** 整个 Spring Boot 应用 = 后端；`llm/` + `ai/` = AI 层，与 `session/`、`task/` 并列，部署在同一个 JAR 里。

## P0 优先级与状态

| 优先级 | 包 | 接口 | 状态 |
|--------|-----|------|------|
| P0-1 | session | POST /sessions, PUT .../profile | ✅ 框架 |
| P0-2 | job | GET /jobs, POST /jobs/recommend | ✅ 框架 |
| P0-3 | task | POST /tasks, POST .../step, .../feedback | ✅ 框架 |
| P0-4 | report | POST /reports/generate, GET ... | ✅ 框架（demo 兜底） |
| P0-5 | llm | — | 🔲 Day 2 填 API Key |
| P0-6 | ai | 六大模块 | 🔲 Day 2 读 AI/prompts |

## 快速启动

```powershell
cd d:\shenicest\JobSearch
.\mvnw.cmd spring-boot:run
```

- API：http://localhost:3000/api/v1/health
- Swagger：http://localhost:3000/swagger-ui.html

## 给前端队友

```
Base URL:  http://localhost:3000/api/v1
Session:   Header X-Session-Id: {uuid}
Demo:      Header X-Demo-Mode: true
```

接口契约见 `docs/openapi.yaml`。

## 给产品队友

改 `src/main/resources/seed/` 下 JSON → 删 `data/` 文件夹 → 重启服务。

## Prompt 资产（项目根目录，Java 只读）

```
../AI/prompts/     ← 六大 Prompt（Day 2 接入）
../AI/fallbacks/   ← AI 失败兜底 JSON
```
