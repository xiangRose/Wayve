# WAYVE 当前产品 PRD（Current Product PRD）

> 本文件由 `docs/product/**` 中的 canonical product contracts 派生，是供飞书阅读的整合视图，不是第二套 Product SOT。

状态：**CURRENT PRODUCT PRD — APPROVED FOR FEISHU SOT**
更新时间：2026-08-29

## MVP Scope Cut Override（Current）

Hard Skill MVP = **Scenario-based Hard Skill Assessment**（5 个岗位、每岗 4 套连续情境、每套 6 道单选题；后台评分，前端不展示单题分值）。Persistent Work Sample、Hard Skill Behavior Event chain、consequence/revision/deliverable、Hard Skill Evidence Replay runtime、Work Sample-based Hard Skill Judge 均为 **Future / Post-MVP**。

## 1. 产品定义（Product Definition）

WAYVE 帮助用户在承诺某个职业方向之前，先体验 AI 时代的真实工作。产品结合：

- 真实的岗位工作样本（Role Work Sample）；
- 可观察的行为（Observable Behavior）；
- 有边界的硬实力准备度评估（Bounded Hard Skill Readiness Assessment）；
- 情境化工作协作画像（Working Portrait）；
- 下一步验证实验（Next Experiment）。

WAYVE 不是心理测评、简历匹配器、招聘测试，也不是职业结论系统。它不判断用户的永久能力、潜力或职业适配度。

## 2. 用户问题（User Problem）

用户可以阅读岗位介绍，却很少有机会体验一项工作实际如何展开，也很难看到自己在真实工作情境中如何行动。WAYVE 让用户亲自尝试工作、观察后果、理解未知，并决定下一步验证什么。

## 3. 产品原则（Product Principles）

- **先体验，再承诺（Preview before Commit）**。
- 推荐只用于导航，不用于能力评价。
- Profile 是可选的。
- Role Work Sample 是真实工作，不是知识考试。
- 只有经过情境化回放，Behavior 才能成为 Evidence。
- Hard Skill Readiness 只描述当前被观察到的岗位工作表现。
- `not_observed` 不拥有 performance score。
- Interest 与 Reflection 是独立的用户自述来源。
- 一次体验不能建立稳定人格、稳定特质或趋势。
- 最终职业决定权属于用户。
- 不产生隐藏的确定性职业结论、Unified Job Fit Score 或基于背景声望的推断。

## 4. 目标用户（Target Users）

- 学生；
- 初入职场用户；
- 转行用户；
- 具有相邻经验、正在探索 AI Product、AI UI Design、AI Operations、AI Data Evaluation 或 AI Application Development 的用户。

## 5. 五个岗位（Five Roles）

当前 active role ID 为：

- `ai_product`：AI Product
- `ai_ui_design`：AI UI Design
- `ai_ops`：AI Operations
- `ai_data_eval`：AI Data Evaluation
- `ai_app_dev`：AI Application Development

五个岗位都是平等的探索选项。

AI Operations 的 MVP Scenario Quiz 主要聚焦产品增长、激活、留存和反馈闭环运营；未来 Work Sample 场景可以覆盖其他产品运营语境，而不改变 active role ID。

## 6. 端到端用户旅程（End-to-end Journey）

```text
Entry
→ 可选 Profile
→ 岗位导航
→ Role Preview
→ Hard Skill Scenario Quiz
→ Collaboration Sprint（Standard 默认包含，可 Skip for now）
→ Reflection
→ Integrated Report
→ Next Mission
→ Growth Track
```

## 7. Profile 与 Recommendation

用户可以跳过 Profile，直接进入任何岗位。

Profile 可以用于：

- 生成导航推荐及推荐理由；
- 形成单独来源的 Background Evidence。

Profile 不得影响：

- Work Sample 输入；
- Work Sample rubric；
- performanceLevel；
- Collaboration Evidence；
- Interest interpretation。

Recommendation rank、rejection 和 navigation score 不得进入任何能力评价。

## 8. Role Work Sample

原 Career Trial 统一升级为岗位工作样本（Role Work Sample）。每个岗位保留自己的 work object 和 interaction grammar，同时共享以下内部语义主轴：

```text
scenario
→ first judgment
→ evidence gathering
→ twist
→ reconsideration
→ final decision
```

一个 Work Sample 应定义：

- 场景与工作对象；
- 约束；
- 用户行动；
- consequence；
- revision；
- final deliverable；
- requirement coverage；
- evidence replay；
- beginner scaffolding；
- `not_observed` 条件。

Requirement coverage 不是题目数量。一个 Work Sample 不需要设计成六道题或六个页面。

## 9. Requirement Model

每个岗位拥有完整的六维 Role Requirement Catalog，并带有经过验证的 `roleImportance` 值 7、8、9 或 10。

`roleImportance` 表示该 requirement 在岗位标准画像中的相对重要程度，不表示：

- passing threshold；
- 用户目标分；
- 用户当前表现；
- capability threshold；
- Career Fit；
- potential。

Session 只观察 Work Sample 能够支持的 requirement 子集。完整 catalog 与单次体验的 observation coverage 必须分开。未被观察到的 requirement 是未知覆盖，不是低能力。

## 10. Hard Skill Assessment

Hard Skill Assessment 的正式链路为：

```text
Behavior
→ Evidence Extraction
→ Rubric Evaluation
→ Dimension Assessment
→ Bounded Claim
→ Report
```

每个 assessment 必须区分：

- `observationStatus)：`observed`、`partially_observed`、`not_observed`；
- `performanceLevel`：内部使用的 0–4 情境 rubric；
- `confidence`；
- 可回放的 evidence references；
- supports 与 limits。

`performanceLevel` 只有在以下两个条件同时满足时才允许存在：

1. `observationStatus` 为 `observed` 或 `partially_observed`；
2. 本次 session 为该 requirement 提供了有效评估机会。

Level 0 的含义是：

> 存在有效评估机会，但用户的响应没有展示可用的 requirement-level performance，或明显 off-task。

因此：

```text
not_observed ≠ 0
```

`not_observed` 没有 performanceLevel、numeric score 或表示低能力的视觉位置。

第一版用户界面不展示裸 0–4 数字，而是展示：

- contextual level；
- visual summary；
- bounded narrative；
- Evidence Replay。

未来只有在积累多个独立 Work Sample 后，才重新评估是否向用户开放 numeric exposure。

`roleImportance` 不得直接乘以 `performanceLevel` 生成用户 readiness score 或 fit score。不得创建 aggregate Career Fit score。

Deterministic layer 负责：

- event validity；
- state；
- consequence；
- revision；
- observationStatus。

LLM 可以：

- 规范化有限结构化自由回答；
- 生成 bounded explanation；
- 生成 supports、limits 和 narrative。

LLM 不得：

- 发明事件；
- 修改 rubric truth；
- 将缺少观察变成低表现；
- 推断 fit、potential 或 stable ability。

## 11. Collaboration Simulation

第一版 MVP shared world 为 **Nova V3 / AI Product Launch Review**；Launch War Room 为 legacy label。

Standard Experience 默认包含 Collaboration Sprint，并提供 `Skip for now`。

如果用户跳过：

- report 仍正常生成；
- Collaboration 的 observationStatus 为 `not_observed`；
- 不产生 performance penalty；
- Working Portrait 限制跨角色协作 claim；
- report 明确写出“本次未观察跨角色协作行为”。

Demo Mode 可以使用 shortened Collaboration path。

Launch War Room 采用 **Reconverging Deterministic State Graph**，包含四个 Decision Beats：

1. Information Exchange
2. Initial Proposal
3. Twist / Conflict Response
4. Final Commitment

每个 Beat 都提供：

- 3 个 plausible strategy families；
- 每个 strategy 的收益与代价；
- 有限结构化 free response；
- free-response normalization；
- deterministic consequence；
- consequence 后重新汇合至下一 shared state。

不构建 3 × 3 × 3 × 3 的完全独立剧情树。

五个角色的体验差异来自：

- Private Information；
- Role Constraint；
- Available Action；
- Information Access；
- Role-specific consequence interpretation；
- Final role trade-off。

每个岗位都必须拥有：

- exclusive information；
- unique actionable agency；
- information another role needs；
- meaningful final trade-off。

Collaboration Evidence 评价跨角色工作行为，不替代 role-specific professional execution。

## 12. Working Portrait

Working Portrait 是基于内部 observation lenses 生成的外部 bounded narrative。

它回答：

- 发生了什么；
- 用户的行为带来了什么作用；
- 哪里出现了 tension；
- 还不知道什么。

内部可以使用 information seeking、clarification、evidence sharing、constraint awareness、conflict response、uncertainty handling、revision、ownership 等 observation lenses。

这些 lenses 不是用户标签、人格类型、soft-skill 分数或 badge。

Working Portrait 不生成：

- personality type；
- stable trait；
- soft-skill score；
- trait badge；
- career fit；
- Resilience Score。

## 13. Reflection

Reflection 是：

- optional；
- user-authored；
- persistent；
- source-labeled。

Reflection 可以：

- 进入 report narrative；
- 帮助排序 Next Mission；
- 与 system observation 形成 tension。

Reflection 不可以：

- 修改 Hard Skill Readiness；
- 覆盖 Task Evidence；
- 覆盖 Collaboration Evidence；
- 变成 ability score。

用户体验与系统观察不一致时，两种视角都保留，不自动互相覆盖。

## 14. Integrated Report

Report 遵循四项 Presentation Principles：

1. **Narrative Spine**：叙事是主骨架；
2. **Visual Readability**：视觉帮助快速理解；
3. **Evidence Explainability**：重要判断必须可解释、可回放；
4. **Minimal Labeling**：避免把用户变成标签。

### Primary Narrative Layer

主叙事语义顺序为：

1. Exploration Summary / role context；
2. What the work required and what happened；
3. Hard Skill Readiness Snapshot；
4. Working Portrait；
5. Response to Change；
6. User Reflection / Background Context / Unknowns & Tensions；
7. Next Mission。

这些内容不要求分别占据独立页面。

### Evidence Drill-down Layer

Evidence Replay 是贯穿报告的 cross-cutting explainability mechanism，不是必须线性阅读的章节。

任何重要 claim 都应能进入 Replay，查看：

- supporting work material；
- behavior；
- consequence；
- interpretation；
- confidence；
- limits。

Replay 可以从以下内容进入：

- Hard Skill；
- Working Portrait；
- Response to Change；
- Tension；
- Next Mission rationale。

Report 只冻结信息架构与表达原则，不冻结 radar、bar、timeline、card、accordion、drawer 等具体 UI 组件。

## 15. Evidence Replay

Evidence Replay 将重要判断连接回：

- 工作材料；
- 用户行动；
- 用户理由；
- consequence；
- interpretation；
- confidence；
- limits。

Simulation trace 只作为 consequence context，不作为 participant behavior。

## 16. Next Mission

```text
Evidence Gap / Unknown
→ Next Experiment
→ Growth Track
```

Interest 和 Reflection 可以帮助选择用户愿意尝试的方向，但都不是 ability evidence。

Current MVP 可以把刚生成的 Next Mission 记录为 `pending`，直到用户完成并经过 review 的下一次真实 experiment。这个 pending 是 MVP 状态支持，不是永久的 Core Journey 节点。

## 17. Data / Evaluation Boundaries

以下来源必须保持区分：

- Background；
- Task Evidence；
- Collaboration Evidence；
- Interest；
- Reflection；
- Recommendation；
- Working Portrait。

相似行为可以拥有不同 claim target，但必须使用：

- 不同 requirement ID；
- 不同 claim target；
- 不同 rubric；
- 不重复表达同一结论；
- 不进行 double counting。

禁止：

- 健康数据；
- prestige inference；
- hidden answer key；
- fabricated history；
- Unified Job Fit Score 变体；
- stable trait inference。

## 18. Experience Layers

Demo、Standard、Extended 是用于探索不同体验深度和认知负担的产品层级假设，不是硬性时长 SLA。

- Demo Mode：可使用 shortened Collaboration path；
- Standard Experience：默认包含 Hard Skill Scenario Quiz、Collaboration Sprint、Reflection，同时支持 `Skip for now`；
- Extended Experience：未来可加入更丰富的多轮协作和多次实验。

## 19. MVP / Standard Scope

MVP 应保留：

- 五种 role grammar；
- 每个岗位四套 Scenario Quiz path；
- replayable evidence；
- bounded assessment；
- optional Reflection；
- pending Next Mission。

Standard 默认包含 shared Collaboration Sprint，并允许 `Skip for now`。Radar、bars、timeline、cards 等具体表现仍属于 UX exploration，不是冻结组件。

## 20. Future Extensions

未来可以扩展：

- 更多岗位场景；
- 多次独立 Work Sample；
- 更丰富的 Collaboration branches；
- 已 review 的后续 Mission；
- Evidence history；
- 用户确认的 Direction Update；
- 多次独立 Work Sample 后的 numeric exposure；
- Hard Skill aggregation 的后续产品校准。

任何扩展都不得隐式引入 fit scoring 或 stable trait inference。

## 21. Product Open Questions

**No blocking product questions for current SOT.**

未来 Product / UX Calibration 包括：

- visual component selection；
- repeated-assessment aggregation；
- 多次独立 Work Sample 后的 numeric exposure；
- additional collaboration scenarios。

## 22. Acceptance Criteria

- 五个 canonical roles 仍可自由选择。
- Profile 是可选的，且与 task assessment 隔离。
- 每个岗位都有独立 interaction grammar、consequence、revision 和 replay 的真实 Work Sample。
- Role Requirement Catalog 与 Session Observation Coverage 分离。
- `not_observed` 永远没有 performance score，也不能渲染为 level 0。
- Hard Skill MVP 来自 Scenario Quiz；Work Sample rubric、consequence、revision、deliverable、replay runtime 与 Judge 均为 Future。
- Standard 默认包含 Collaboration，并提供 `Skip for now`；跳过不产生 performance penalty，且限制 Working Portrait claim。
- Collaboration Evidence 有独立 claim target，且不发生 double counting。
- Working Portrait 是 bounded narrative，不是 trait 或 badge。
- Reflection 是 optional、persistent、separate，不能覆盖 system evidence。
- Report 以 narrative 为主线，并通过 cross-cutting Evidence Replay 提供可解释性，同时保持视觉可读性和 minimal labeling。
- Next Mission 来自真实 evidence gap，并在后续真实 evidence 产生前保持 pending。
- 本 PRD 不授权 backend、frontend、schema migration、seed、OpenAPI 或 remote Git 变更。
