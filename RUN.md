# 启动指南

```powershell
cd d:\shenicest\JobSearch
.\mvnw.cmd spring-boot:run
```

- API：http://localhost:3000/api/v1/health
- Swagger：http://localhost:3000/swagger-ui.html

重置 seed：删除 `data/` 后重启。

## IDEA 启动注意

1. **Run Configuration → Working directory** 设为 `$MODULE_WORKING_DIR$`（即 `JobSearch` 项目根目录）
2. 若仍报错，先关掉其他正在运行的实例（含之前的 `mvn spring-boot:run`），避免 H2 文件锁
3. **JDK 17**，Maven 重新 import 一次

## Day 2 开启 AI

设置环境变量 `AI_API_KEY`，并将 `application.yml` 中 `app.ai.enabled` 改为 `true`。
