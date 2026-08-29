# P0 后端自测清单

从仓库根目录 `D:\shenicest` 启动：`mvn spring-boot:run`，端口 **3000**。

**Windows 注意**：请用 `curl.exe`（不是 PowerShell 的 `curl` 别名），命令写在一行，不要用 `\` 换行。

## 0. 准备

```cmd
curl.exe -s -X POST http://localhost:3000/api/v1/sessions
```

记下返回的 `sessionId`，后续替换 `{SID}`。

## P0-1 · AI 编排 + Fallback

```cmd
curl.exe -s -X PUT http://localhost:3000/api/v1/sessions/{SID}/profile -H "Content-Type: application/json" -d "{\"userStage\":\"career_changer\",\"clarityLevel\":\"unknown\",\"currentStatus\":\"student\",\"backgroundText\":\"曾参与校园项目需求梳理\"}"
```

期望：`resumeEvidenceStatus` 为 `ready` 或 `fallback`（不是「异步进行中」）。

## P0-2 · 履历证据落库

```cmd
curl.exe -s http://localhost:3000/api/v1/resume/evidence -H "X-Session-Id: {SID}"
```

期望：`status` 非 `pending`。

## P0-3 · 岗位推荐

```cmd
curl.exe -s -X POST http://localhost:3000/api/v1/jobs/recommend -H "X-Session-Id: {SID}" -H "Content-Type: application/json" -d "{\"rejectedJobIds\":[]}"
```

## P0-4 · 会议室场景 + 报告

```cmd
curl.exe -s -X POST http://localhost:3000/api/v1/scenes/PRODUCT_S1/answers -H "X-Session-Id: {SID}" -H "Content-Type: application/json" -d "{\"roleId\":\"ai_pm\",\"answerType\":\"preset\",\"selectedOptionId\":\"PRODUCT_S1_A\"}"
```

```cmd
curl.exe -s -X POST http://localhost:3000/api/v1/reports/generate -H "X-Session-Id: {SID}"
```

期望：`taskEvidenceByJob.ai_pm` 有 `meeting_scene` 来源。

## 岗位 ID 对照（场景 roleId）

| 场景 | roleId |
|------|--------|
| PRODUCT_S1 | `ai_pm` |
| UI_S1 | `ai_ux` |
| OPS_S1 | `ai_operator` |
| DATA_S1 | `ai_researcher` |
| DEV_S1 | `ai_consultant` |

## Demo 模式

```cmd
curl.exe -s -X POST http://localhost:3000/api/v1/reports/generate -H "X-Demo-Mode: true"
```
