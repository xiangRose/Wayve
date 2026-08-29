# 启动指南

```powershell
cd d:\shenicest
.\mvnw.cmd spring-boot:run
```

- 前端 + API：http://localhost:3001/
- Health：http://localhost:3001/api/v1/health
- Swagger：http://localhost:3001/swagger-ui.html

重置 seed：删除 `data/` 后重启。

## IDEA 启动注意

1. **Run Configuration → Working directory** 设为仓库根目录 `d:\shenicest`
2. 若仍报错，先关掉其他正在运行的实例，避免 H2 文件锁
3. **JDK 17**

## 接入大模型

复制 `.env.example` 为 `.env`（已在 `.gitignore`），填入 `AI_API_KEY`，或设置环境变量：

```powershell
$env:AI_ENABLED="true"
$env:AI_API_KEY="你的密钥"
```

## 目录说明

| 目录 | 对应分支 | 内容 |
|------|----------|------|
| `front/` | `dev/frontend` | 前端页面、样式、交互 |
| `src/` | `dev/backend` | Spring Boot 后端 |
| `src/main/resources/seed/` | `dev/content` | 岗位 seed、task-templates |
| `research/` | `dev/content` | 调研脚本、aggregated 数据 |
| `docs/product/` | `dev/product` | PRD、产品规范 |

详细提交流程见 `LOCAL_WORKFLOW.md`。
