# 岗位调研 & 爬虫操作手册

> 对应 PRD：[WAYVE｜试途_Hackathon_总体_PRD.md](../../WAYVE｜试途_Hackathon_总体_PRD.md) §岗位调研规则  
> 目标：每岗 **15–30 条**公开样本 → 归纳 → 更新 `src/main/resources/seed/jobs.json`

## 一、整体流程（你一个人也能跑通）

```
手采/爬虫 → raw/*.json → LLM 归纳 → aggregated/*.summary.json → 人工校对 → jobs.json + task-templates
```

**P0 不阻塞 Demo**：现有 `jobs.json` 已可演示；调研是**增强可信度**，不是上线前置条件。

## 二、环境准备（一次性，约 5 分钟）

```powershell
cd d:\shenicest\JobSearch\research\scripts
python -m venv .venv
.\.venv\Scripts\Activate.ps1
pip install -r requirements.txt
```

若用 LLM 归纳，在项目根或 `JobSearch` 下建 `.env`（已在 `.gitignore`）：

```
AI_API_KEY=你的密钥
AI_BASE_URL=https://api.openai.com/v1
AI_MODEL_PRO=gpt-4o
```

可与后端 `application.yml` 共用同一套变量。

## 三、三种采集方式（按优先级）

### 方式 A：企业官网 / 公开文章（自动）

```powershell
python collect_url.py --job-id ai_product --source company_site `
  --url "https://jobs.bytedance.com/..." `
  --title "AI产品经理" --company "字节跳动"
```

- 适用：Career Page、技术博客、公开访谈
- 不适用：`zhipin.com`、`xiaohongshu.com`（脚本会拒绝，改用手采）

### 方式 B：BOSS / 小红书（手采，推荐）

1. 打印搜索链接：
   ```powershell
   python print_search_urls.py --job-id ai_product
   ```
2. 浏览器打开 BOSS，复制 JD 全文
3. 填入 `templates/manual_import.csv`，批量导入：
   ```powershell
   python import_manual.py ..\templates\manual_import.csv
   ```

协助同学可以只做「复制粘贴填 CSV」，你负责导入和归纳。

### 方式 C：单条 JSON 手填

复制 `templates/manual_sample.template.json` 到 `raw/ai_product/`，改内容保存。

## 四、样本进度自检

```powershell
Get-ChildItem ..\raw\*\*.json | Group-Object { $_.Directory.Name } | Format-Table Name, Count
```

| 岗位 | 目标 | 优先级 |
|------|------|--------|
| ai_product | 20+ | P0 路演主岗 |
| ai_ui_design | 20+ | P0 路演主岗 |
| ai_ops | 10+ | 预览岗 |
| ai_data_eval | 10+ | 预览岗 |
| ai_app_dev | 10+ | 预览岗 |

PRD 要求每岗先 **10–15 条**即可开始写任务，不必等爬虫「完美」。

## 五、LLM 归纳 → 写入产品

```powershell
# 至少 5 条样本后再跑
python aggregate_with_llm.py --job-id ai_product
python aggregate_with_llm.py --job-id ai_product --dry-run   # 先看 prompt 体积
```

输出：`aggregated/ai_product.summary.json`

人工校对后，把字段映射到：

| 归纳字段 | 产品文件 |
|----------|----------|
| definition, coreWorkObject, typicalWorkSnippet | `seed/jobs.json` |
| painPoints, deliverables | `seed/task-templates/*.json` 情境 |
| roleBoundaries | 报告文案 / prompt 02 |
| evidenceSources | 路演「模型来源」脚注 |

更新 seed 后，删除 H2 库文件 `data/jobsearch.*` 或换库名，重启后端以重新灌数据。

## 六、为什么不要硬爬 BOSS

- 登录态、签名、频率限制，2 天黑客松投入产出比极低
- PRD 明确：**反爬 → 人工采样 + 公开搜索补齐**
- 路演需要的是「有来源的归纳模型」，不是「全自动爬虫演示」

## 七、协助同学分工（你只收 JSON/CSV）

| 协助内容 | 交付物 |
|----------|--------|
| BOSS 手采 | 填好 `manual_import.csv` |
| 小红书体感 | 同上，source=xhs |
| 官网链接收集 | 发你 URL 列表，你跑 `collect_url.py` |
| 校对归纳 | 读 `aggregated/*.summary.json` 标红不靠谱句子 |

## 八、目录说明

```
research/
├── config/jobs.yaml       # 五岗关键词
├── raw/{jobId}/*.json     # 原始样本（可 git 提交）
├── aggregated/            # LLM 归纳结果
├── schema/                # 样本 JSON Schema
├── scripts/               # 爬虫与导入脚本
├── prompts/               # 归纳用 Prompt
└── templates/             # CSV / JSON 模板
```
