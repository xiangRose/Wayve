# 岗位、Profile 与 Recommendation

状态：CURRENT PRODUCT SOT — APPROVED FOR FEISHU SOT

## 五个 Canonical Roles

| jobId | 岗位工作 | 主要工作对象 |
|---|---|---|
| `ai_product` | 定义 AI 产品问题、优先级、证据需求和取舍 | 产品决策 |
| `ai_ui_design` | 让 AI 能力、不确定性和恢复过程可理解 | 交互流程 |
| `ai_ops` | 通过增长、反馈、执行和迭代运营 AI 产品 | 漏斗 / cohort / 干预 |
| `ai_data_eval` | 评估输出、标准、bad case 和 release gate | rubric / 评测门槛 |
| `ai_app_dev` | 在效果、延迟、成本和稳定性约束下交付 AI 应用 | runtime / source 配置 |

## Entry 与 Profile

Profile 是 optional。用户可以直接进入任意岗位。Profile 可生成导航建议和单独来源的 Background Evidence，但不得改变 Work Sample 输入、rubric、performanceLevel 或 Collaboration Evidence。

## Recommendation Contract

Recommendation 只回答“哪个岗位值得先体验”。它使用偏好、意图和有来源的经验标签，不进入 Evidence、Hard Skill Assessment、Working Portrait 或 Next Mission gap scoring。五岗始终都可选择。

AI Operations 的 MVP Scenario Quiz 主要聚焦 product growth、activation、retention、feedback-loop operations；未来 Work Sample 场景可覆盖其他 product-operations 语境，不改变 active role ID。

## 数据隔离

`profileContext → navigation explanation`

`profileContext → Background Evidence`

`task events → Task Evidence + Hard Skill Assessment`

`collaboration events → Collaboration Evidence + Working Portrait`

`interest responses → Interest Feedback`
`reflection responses → User Reflection`

任何来源不得静默跨边界。
