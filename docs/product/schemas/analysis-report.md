# AnalysisReport Schema

状态：CURRENT PRODUCT SOT — APPROVED FOR FEISHU SOT

AnalysisReport 是对多个来源进行分层呈现的容器，永远不包含 Unified Job Fit Score。

## 必要结构

```json
{
  "type": "AnalysisReport",
  "reportId": "uuid",
  "sessionId": "uuid",
  "roleContext": {},
  "hardSkillAssessment": {},
  "backgroundEvidence": [],
  "taskEvidence": [],
  "collaborationEvidence": [],
  "workingPortrait": {},
  "responseToChange": {},
  "interestFeedback": [],
  "userReflection": {},
  "tensions": [],
  "unknowns": [],
  "evidenceReplay": [],
  "nextMission": {},
  "boundaryNotice": "..."
}
```

## Hard Skill Assessment

每个 dimension 包含 `requirementId`、`observationStatus`、可选的 `performanceLevel`、`confidence`、`evidenceRefs`、`supports` 和 `limits`。

- `observed` 或 `partially_observed` 且 session 提供有效评估机会时，才可有 0–4；
- Level 0 表示有效机会存在但未展示可用 requirement-level performance 或明显 off-task；
- `not_observed` 没有 performanceLevel 或 numeric value，不等于 Level 0；
- assessment 只针对当前 Role Work Sample。

## Presentation Boundary

Schema 定义数据意义，不冻结 UI 组件。Report 必须遵循 Narrative Spine、Visual Readability、Evidence Explainability 和 Minimal Labeling。Interest 与 Reflection 是用户自述来源，不得覆盖系统 Evidence。
