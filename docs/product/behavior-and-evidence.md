# 行为、证据与评估

状态：CURRENT PRODUCT SOT — APPROVED FOR FEISHU SOT

## MVP Scope Cut Override

Hard Skill MVP = Scenario-based Hard Skill Assessment。Work-sample-based Hard Skill assessment、persistent work object、Behavior Event chain 与 Hard Skill Replay runtime 均为 Future / Post-MVP。

## 来源边界

| 来源 | 可以支持 | 不得做 |
|---|---|---|
| Background | 背景上下文和独立来源的既往经历证据 | 改变 task 结果或 rubric |
| Role Work Sample | 岗位专业执行与 Hard Skill Readiness | 证明 fit 或稳定能力 |
| Collaboration Sprint | 一次共享情境中的跨岗位工作方式 | 替代岗位专业证据 |
| Interest | 用户表达的兴趣和意愿 | 成为能力证据 |
| Reflection | 用户对本次体验的解释 | 覆盖系统观察 |

## Evidence Source Boundary

同一个行为可能支持不同 claim target。例如向工程角色询问 latency：将限制纳入产品决策属于 Product Hard Skill；主动澄清、共享信息和处理分歧属于 Collaboration Evidence。若同一 Event 被两类 Evidence 引用，必须使用不同 requirement ID、claim target 和 rubric，且不得重复计分。

## Observation / Performance

`observationStatus`：

- `observed`：捕获了足够的相关行为；
- `partially_observed`：捕获了部分行为，但上下文或覆盖不完整；
- `not_observed`：没有产生可用观察。

`performanceLevel` 只有在 observationStatus 为 `observed` 或 `partially_observed`，且 session 提供了有效评估机会时才允许存在。

Level 0 表示：存在有效评估机会，但响应没有展示可用的 requirement-level performance，或明显 off-task。

`not_observed ≠ 0`。`not_observed` 没有 performanceLevel、numeric value、performance penalty 或表示低能力的视觉位置。

## Hard Skill Assessment

正式链路：

`Behavior → Evidence Extraction → Rubric Evaluation → Dimension Assessment → Bounded Claim → Report`

Deterministic layer 负责 Event 合法性、state、consequence、revision、observationStatus 和 authored rubric 条件。LLM 只负责有限 free response normalization、bounded explanation、supports、limits、tension 和 narrative，不得发明 Event、修改 rubric truth、推断 fit 或把缺少观察变成低表现。

第一版用户不看裸 0–4 数字，只看 contextual level、visual summary、bounded narrative 和 Evidence Replay。Numeric exposure 属于未来多次独立 Work Sample 后的 UX Calibration。

## roleImportance

`roleImportance` 表示 requirement 在岗位标准画像中的相对重要程度，不是用户得分、目标线、passing score、capability threshold、Career Fit、potential 或 suitability。不得直接与 performanceLevel 相乘生成用户 readiness 或 fit score。

## Evidence Replay

每个重要 claim 都必须能够回放到 source Event、work material、user action、reason、consequence、interpretation、confidence 和 limits。
