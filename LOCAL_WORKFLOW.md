# 本地开发与提交指南

仓库已整理为 monorepo，**不要再使用 `JobSearch/` 子目录**。

## 目录 → 分支对应

| 你改的文件在哪 | 从哪个分支拉代码 | PR 合并到哪 |
|----------------|------------------|-------------|
| `front/**` | `dev/frontend` | `dev/frontend` → `integration` → `main` |
| `src/**`（Java、application.yml） | `dev/backend` | `dev/backend` → `integration` → `main` |
| `src/main/resources/seed/**`、`research/**` | `dev/content` | `dev/content` → `integration` → `main` |
| `docs/product/**` | `dev/product` | `dev/product` → `integration` → `main` |

## 标准提交流程

### 前端示例

```powershell
cd d:\shenicest
git fetch backend
git checkout dev/frontend
git pull backend dev/frontend
git checkout -b feature/frontend/你的功能名

# 改 front/ 下的文件后：
git add front/
git commit -m "feat(frontend): 描述你的改动"
git push -u backend feature/frontend/你的功能名
```

然后在 GitHub 提 PR，**base 选 `dev/frontend`**。

### 后端示例

```powershell
git checkout dev/backend
git pull backend dev/backend
git checkout -b feature/backend/你的功能名

git add src/
git commit -m "feat(backend): 描述你的改动"
git push -u backend feature/backend/你的功能名
```

PR base 选 **`dev/backend`**。

### 内容 / seed 示例

```powershell
git checkout dev/content
git pull backend dev/content
git checkout -b content/ai-product/update-seed

git add src/main/resources/seed/ research/
git commit -m "content(ai-product): 更新岗位 seed"
git push -u backend content/ai-product/update-seed
```

PR base 选 **`dev/content`**。

## 启动项目

```powershell
cd d:\shenicest
.\mvnw.cmd spring-boot:run
```

访问 http://localhost:3001/

## 五岗 jobId（不要自创）

`ai_product` · `ai_ops` · `ai_data_eval` · `ai_app_dev` · `ai_ui_design`

## 不要提交的文件

- `.env`（密钥）
- `data/`（本地 H2 数据库）
- `target/`、`.idea/`、`.cursor/`
- `research/**/.venv/`
- Word 文档（除非团队明确要求）
