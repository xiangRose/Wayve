# WAYVE｜五岗 Work Sample 与协作内容系统

状态：`CURRENT AUTHORING BLUEPRINT`（Batch 1 Calibration；Human Review accepted foundation with required changes）  
范围：LOCAL ONLY；不修改 production frontend/backend，不 push / PR / merge。

## A. CURRENT CONTENT ASSET AUDIT

| 资产 | 位置 | 当前价值 | 缺口 | 结论 |
|---|---|---|---|---|
| Product SOT / PRD | `Wayve/WAYVE-Product-PRD.md` | 已冻结边界、Journey、Evidence、Replay、Collaboration、Reflection | 无阻塞产品问题；需把内容契约落到作者字段 | KEEP |
| 五份 Frozen Trial Specs | `Wayve/local_exports/product_review/five_role_trial_finalization/specs/` | 每岗已有 scenario、interaction、consequence、revision、deliverable、evidence limits | requirement 仍是旧的 5–6 个局部 ID，尚未与新版六项 catalog 一一对齐 | ADAPT |
| 当前 Task Content Library | `Wayve/local_exports/product_review/five_role_trial_finalization/WAYVE_TASK_CONTENT_LIBRARY.md` | 五个可序列化 pack；稳定 content/fixture/option/consequence/replay IDs；已有 adoption matrix | 仍是 3–5 分钟 mini task；协作 brief、beginner scaffolding、golden cases 不完整 | ADAPT |
| 旧题库 | `Wayve/local_data/task_content_sources/5岗题库.docx` | 提供真实工作语料；A 集可直接增强 product/ops/eval/app-dev | 旧题型/题库结构，不可直接作为问卷；需抽取工作对象与约束 | ADAPT |
| Role research | `Wayve/local_data/role_research/**` | `ai_product`、`ai_ops` 有可用 JD 样本；ops 还有截图 | data_eval/app_dev/ui_design CSV 为空；ops taxonomy 较宽 | KEEP（证据分级） |
| Frontend registry / fixtures | `_worktrees/wayve-five-role-mvp/front/js/trial-registry.mjs`, `mvu-core.mjs`, `role-trial-ui.js` | 已有事件名、rerun limit、trace 与 no-fit 边界的实现线索 | 当前 UI 多为字段配置；不能当作新内容 SOT；仅用于兼容性检查 | ADAPT |
| Seed task templates | `Wayve/src/main/resources/seed/task-templates/*` | 可作为历史运行样本与迁移参考 | 仍含 `ai_pm` / `ai_ux`，不等同 canonical role IDs | DEPRECATE（运行入口）；KEEP（历史参考） |
| Gameplay research / prototype | `_worktrees/wayve-five-role-mvp/docs/product/experience-design-gameplay/**` | persistent scene、object discovery、state consequence、一次 revision | 只对 UI Design 直接成熟，尚未泛化到五岗 | ADAPT |

## B. CONTENT REUSE MATRIX

| 岗位 | 既有高质量素材 | KEEP | ADAPT | REWRITE | DEPRECATE |
|---|---|---|---|---|---|
| `ai_product` | 会议助手 activation pack；funnel/retention/feedback/effort cards；twist bundle | scenario、四类证据、one-slot constraint、retain/revise、replay IDs | 把 5 个旧 requirement 映射到六项 catalog；补 engineering feasibility 与跨团队信息交换 | 连续 decision workspace、最终 product decision artifact、beginner glossary、golden cases | “选唯一正确功能”、直接加权得分 |
| `ai_ui_design` | document-to-checklist state flow；waiting/partial/uncertainty/recovery simulation | persistent flow canvas、六状态 fixture、deterministic simulated user、rerun once | 映射六项 catalog；补 user-flow 与 information architecture 节点；将视觉表达限定为状态表达而非审美 | 完整 AI state workbench 与 revision contract | 视觉偏好题、模拟用户行为作为 participant evidence |
| `ai_ops` | activation/cohort/intervention pack；growth/retention language；ops JD patterns | lifecycle break、segment、intervention、modeled consequence、iteration | 扩展到 attribution、resource/execution、feedback loop；保留 canonical AI Operations 名称 | 可操作 campaign brief、资源 trade-off、deadline consequence、跨角色 handoff | 永久把 role taxonomy 命名为 Growth Ops |
| `ai_data_eval` | badcase、policy boundary、audit allocation、counterexample、release gate | badcase table、rule anchors、6-slot audit mechanic、retest | 映射 quality sensitivity / data insight / eval design / quality decision；补 sampling/coverage | 多轮 boundary → taxonomy → release gate work object | 普通“答案对不对”QA、把人工审计量当能力分 |
| `ai_app_dev` | retrieval integration A/B/C request suite；source/freshness/latency/cost/fallback | logs/request suite、fallback exercised distinction、deterministic results | 映射 system understanding/debug/AI tech/engineering trade-offs/delivery；补 root-cause evidence chain 与 regression | debug console + incident timeline + revision delivery note | 技术知识选择题、configured fallback 等同于 exercised fallback |

## C. FIVE ROLE WORK SAMPLE BLUEPRINTS

共同语义脊柱：`scenario → first judgment → evidence gathering → twist/consequence → revision → final deliverable`。每岗保留自己的 interaction grammar。

### `ai_product` — Activation Decision Room

工作对象：一个 AI meeting assistant 的 activation decision board（候选 slice、evidence inbox、capacity slot、deferred scope、decision note）。  
节点：先放入 one-slot priority；选择并排序两类证据；查看 segment/import consequence；retain 或 revise；提交 metric、trade-off、uncertainty。  
交付物：`selectedSlice / deferredSlices / reason / tradeoff / validationMetric / uncertainty / nextTest`。  
连续性：priority 改变 consequence implication；revision 保留 before/after board。

### `ai_ui_design` — Document-to-Checklist State Workbench

工作对象：六状态 interaction flow（processing、partial、uncertain、failed、recovery、completed）与 supplied user path。  
节点：先改 waiting/partial；配置 uncertainty disclosure/source/confirm/edit；选择 failed-section recovery；跑固定路径；查看 friction/state continuity；一次 revision。  
交付物：最终状态转移、recovery/preservation、latest simulated path、risk、next usability test。  
连续性：状态决策改变模拟用户动作与 work-state consequence；simulation trace 永远不是 participant behavior。

### `ai_ops` — Activation Loop Intervention Desk

工作对象：生命周期 dashboard、cohort cards、attribution notes、campaign brief、resource/deadline board。  
节点：定位 lifecycle break；按行为/意图/价值分层；选择归因证据；配置 intervention、channel、timing 与 resource；看到 modeled activation/retention/cannibalization consequence；迭代一次。  
交付物：`breakpoint / targetCohort / attribution / intervention / channel / timing / resource / guardrail / nextIteration`。  
连续性：干预改变 cohort-level modeled outcome；保留初始 hypothesis 与最终 iteration。

### `ai_data_eval` — Pre-release Evaluation Gate

工作对象：badcase table、policy/rule boundary cards、error taxonomy、eval-set builder、sampling/audit allocator、release gate。  
节点：先选 release posture；检查代表性 badcases 与边界；建立 taxonomy；分配有限 audit slots；查看 hidden/counterexample retest；修订 gate 或 sampling。  
交付物：`ruleBoundary / errorTaxonomy / evalSet / coveragePlan / auditAllocation / releaseGate / retestDecision / uncertainty`。  
连续性：sampling/gate 影响可见漏检与 review load；retest 产生 concrete counterexample，不提供“标准答案标签”。

### `ai_app_dev` — Retrieval Incident & Delivery Console

工作对象：incident timeline、logs、request traces、source/context panel、runtime config、latency/cost budget、fallback and regression suite。  
节点：先圈定 failure scope/root-cause hypothesis；检查 source/RAG/context；改 runtime routing/config；跑固定 A/B/C regression；看到 quality/latency/cost/stability consequence；修订并提交 delivery note。  
交付物：`rootCause / evidenceRefs / sourceScope / contextRule / runtimeConfig / latencyCostTradeoff / fallback / regressionResult / deliveryRisk`。  
连续性：配置改变 request suite 结果；fallback configured 与 exercised 分开记录。

## D. REQUIREMENT COVERAGE MAPS

覆盖标记：`P` primary observation，`S` supporting observation，`NO` 该 sample 不保证覆盖则输出 `not_observed`；不是零分。

`roleImportance` 作为岗位标准元数据随 requirement catalog 保留：

| roleId | requirementId（简写） | roleImportance |
|---|---|---:|
| `ai_product` | user_insight / problem_definition / product_judgment / ai_feasibility / prioritization_tradeoff / cross_team_push | 9 / 9 / 10 / 7 / 10 / 9 |
| `ai_ui_design` | user_understanding / interaction_logic / information_architecture / ai_state_design / interface_expression / design_iteration | 10 / 10 / 9 / 9 / 8 / 8 |
| `ai_ops` | data_insight / user_segmentation / operational_attribution / strategy_design / resource_execution / feedback_iteration | 9 / 8 / 9 / 9 / 10 / 10 |
| `ai_data_eval` | quality_sensitivity / rule_boundary / badcase_attribution / data_insight / evaluation_design / quality_decision | 10 / 9 / 9 / 9 / 10 / 8 |
| `ai_app_dev` | system_understanding / debug_localization / ai_technical_understanding / engineering_solution / performance_cost_tradeoff / delivery_adaptation | 9 / 10 / 10 / 9 / 9 / 8 |

这些值只用于描述岗位相对重要性，不参与用户加权、通过线、Career Fit 或 Unified Job Fit。

| 岗位 / requirement | scenario | first judgment | evidence | consequence | revision | final artifact |
|---|---|---|---|---|---|---|
| Product：用户洞察 | S |  | P | S | S | P |
| Product：问题定义 | P | P | S | S | S | P |
| Product：产品判断 | S | P | S | P | P | P |
| Product：AI 可行性理解 | S | S | P | P | S | P |
| Product：优先级与取舍 | S | P | S | P | P | P |
| Product：跨团队推动 | S | S | P（请求 engineering/ops evidence） | P | P | P |
| UI：用户理解 | P | S | P | P | P | S |
| UI：交互逻辑 | S | P | P | P | P | P |
| UI：信息架构 | S | P | P | P | P | P |
| UI：AI 状态设计 | P | P | S | P | P | P |
| UI：界面表达 | S | S | S | P（状态可理解性） | P | S |
| UI：设计迭代 | S |  | S | P | P | P |
| Ops：数据洞察 | P | P | P | P | S | P |
| Ops：用户分层 | S | P | P | P | S | P |
| Ops：运营归因 | S | S | P | P | P | P |
| Ops：策略设计 | S | P | P | P | P | P |
| Ops：资源与执行 | S | P | S | P | P | P |
| Ops：反馈迭代 | S |  | S | P | P | P |
| Eval：质量敏感度 | P | P | P | P | S | P |
| Eval：规则与边界判断 | S | P | P | P | P | P |
| Eval：Badcase 归因 | S | P | P | P | P | P |
| Eval：数据洞察 | P | S | P | P | S | P |
| Eval：评测设计 | S | P | P | P | P | P |
| Eval：质量决策 | S | P | S | P | P | P |
| App：系统理解 | P | P | P | P | S | P |
| App：Debug 定位 | P | P | P | P | P | P |
| App：AI 技术理解 | S | P | P | P | S | P |
| App：工程方案设计 | S | P | P | P | P | P |
| App：性能与成本取舍 | S | P | P | P | P | P |
| App：交付与适应 | S | S | S | P | P | P |

每行都必须在 authoring contract 中声明 usable opportunity；若场景未提供足够证据，输出 `not_observed`，绝不补 0。

## E. CONTENT AUTHORING CONTRACT

每个 `workSample` 必须是单一当前版本、可序列化、可 replay 的 package：

```yaml
contentId: stable-string
roleId: ai_product|ai_ui_design|ai_ops|ai_data_eval|ai_app_dev
version: current
scenario: {title, userRole, goal, context, constraints, timeBox}
workObjects: [{id, type, data, glossaryRefs}]
stages: [scenario, first_judgment, evidence_gathering, twist, reconsideration, final_decision]
interactions: [{id, type, options, validation, emits}]
consequences: [{id, inputState, benefit, cost, visibleDrivers, nextState}]
requirements:
  - requirementId
    roleImportance              # role standard metadata only; never a user score/threshold
    workObject
    observationOpportunity
    evidenceSource
    expectedObservableBehavior
    strongEvidence
    partialEvidence
    weakEvidence
    level0Condition
    notObservedCondition
    consequenceRelevance
    revisionRelevance
    claimBoundary
    replayMaterial
scaffolding: {minimumContext, glossary, embeddedExplanation, optionalHint, beginnerSafeUnknowns, specialistKnowledgeObserved}
deliverable: {fields, required, boundedLength}
replay: [{replayId, sourceEventIds, state, action, consequence, interpretation, limits}]
unknowns: []
nextMission: {evidenceGap, experiment, status: pending}
```

约束：每个 Evidence object 只绑定一个 requirement；deterministic layer 决定 event validity/state/consequence/observationStatus；LLM 仅做 normalize 与 bounded explanation，不得创造事件、修改 rubric truth 或推断 fit。

## F. RUBRIC STRUCTURE

内部 level 0–4 仅用于 contextual evaluation：

| Level | 定义 |
|---|---|
| 4 | 在 supplied context 中主动引用关键证据，明确约束与取舍，行动与后果一致，并在新信息后形成可解释修订/确认。 |
| 3 | 展示了大部分 requirement-level behavior；证据与决策关系清楚，存在小幅遗漏但不破坏工作产出。 |
| 2 | 有局部可用行为或理由，但证据关系、约束意识或 consequence interpretation 不完整。 |
| 1 | 仅有弱/表层信号，主要依赖偏好、术语或 unsupported assertion；不足以支撑完整 bounded claim。 |
| 0 | 存在真实有效机会，但未展示 requirement-level usable performance，或 materially off-task。 |

`not_observed`：没有有效观察机会或没有可用 evidence；不产生 performanceLevel。  
Rubric 评分优先级：行为与证据关系 > 结果是否“漂亮” > 文本流畅度。Eloquent-but-unsupported 最高不得超过 Level 1；wrong-evidence 只能按实际引用关系评估；self-correction 必须由 before/after + consequence evidence 支撑。

## G. LAUNCH WAR ROOM MASTER DESIGN

共同场景已批准为 **AI Meeting Assistant Launch**。在发布窗口前同时出现 quality risk、latency/cost、activation drop、UX uncertainty 与 deadline/resource collision。参与者分别拥有 Product、Ops、Data Evaluation、Application Development、UI Design 视角。Product 负责整合承诺，但不能覆盖其他角色的 binding professional constraints。

最小 shared state：`launchScope / rolloutMode / qualityGate / technicalFeasibility / userStateAcceptance / targetCohort / campaignWindow / rollbackCondition / unresolvedRisks`。`Full Launch` 只有在质量、技术、用户状态、运营窗口等必要条件均满足时才可进入；否则系统只能进入 fixture 定义的 staged / limited / hold / additional-evaluation 状态。

四个 Decision Beats（每个三种 realistic strategy family，均有 benefit + cost）：

1. **Information Exchange / Opening Move**：用户决定先交换证据、先声明 constraint 或先对齐 launch criterion；之后仍可进行其他信息交换。Opening 影响 time budget、discussion order 与 remaining uncertainty，三者分别换取速度、信息完整度或共识清晰度，代价是遗漏/延迟/对话成本。
2. **Initial Proposal**：ship narrow slice；stage-gated launch；hold launch for more evidence。分别换取 deadline、风险控制、质量信心，代价为 scope/learning/机会成本。
3. **Twist / Conflict Response**：保护关键 guardrail；缩减 scope 保留 deadline；引入 fallback/人工兜底。分别减少质量风险、交付风险、用户中断，代价为覆盖、体验一致性、成本。
4. **Final Commitment Artifact**：同时提交 rollout mode、scope、target cohort、primary owner、action owners、success/monitor metric、guardrail、rollback condition、unresolved risk、follow-up experiment。真正 trade-off 来自前面 scope、rollout、quality、latency/cost、user-state、cohort、timing 与 fallback 的组合，而不是末尾字段三选一。

每个 Beat 支持有限自由回应，normalize 为 claims、evidenceReferenced、constraintsAcknowledged、tradeoffs、uncertainties、requestedActions、proposedAction；随后进入 deterministic validation 与 bounded LLM interpretation，并 reconverge 到共享 next state。

## H. FIVE PRIVATE BRIEFS

| 角色 | Exclusive information | Unique constraint | Action agency | Must tell another role | Final trade-off |
|---|---|---|---|---|---|
| Product | 本周 activation target、可承诺 scope、业务优先级 | 只能承诺一个 launch slice | 定义 priority / scope / launch criterion | 向 Dev 说明 user value 与 deadline；向 Ops 说明目标 cohort | learning speed vs scope certainty |
| Operations | cohort funnel、campaign calendar、activation/retention baseline | 触达窗口与资源名额固定 | 选择 cohort、intervention、timing、guardrail | 向 Product 提供 segment evidence；向 UI 提供 user interpretation | reach vs fatigue / retention risk |
| Data Evaluation | badcase clusters、boundary uncertainty、coverage gap | audit slots 与 release gate 预算有限 | 设定 sampling、gate、retest | 向 Product/Dev 说明 quality risk 与 confidence | coverage vs review load |
| Application Development | runtime path、latency/cost/capacity、fallback feasibility | 峰值容量与 p95 latency 上限 | 选择 routing、fallback、rollout mode | 向 Product 说明 feasible scope；向 Eval 说明 instrumentation | quality vs latency/cost |
| UI Design | loading/partial/uncertainty/recovery user-state evidence | 不得把 unresolved AI output 表现为 confirmed | 选择 state disclosure、recovery、handoff | 向 Product/Ops 说明用户理解与中断风险 | clarity/control vs interaction load |

## I. STATE / CONFLICT / CONSEQUENCE MODEL

`state0 shared brief → beat input → normalized action → deterministic consequence → stateN shared brief`。  
Consequence 记录：`benefit / cost / affectedMetric / affectedRole / visibleDriver / nextDecisionBeat`。冲突不是“正确答案判定”，而是约束之间的真实张力；相同输入状态 + 相同 normalized action 必须得到相同结果。simulationTrace 与 participant Behavior 分离。

## J. FREE RESPONSE NORMALIZATION

先做 schema validation，再做 bounded interpretation：

```json
{
  "claims": [], "evidenceReferenced": [], "constraintsAcknowledged": [],
  "tradeoffs": [], "uncertainties": [], "requestedActions": [], "proposedAction": ""
}
```

Normalization 允许同义改写、中文/英文混用、短句拆分与 canonical option paraphrase；不允许补充用户未提及的证据、把语气当能力、把 collaboration event 直接变成 trait。无法可靠解析时保留 raw text，标记 `interpretation_uncertain`。

## K. REFLECTION CONTENT MODEL

每次 role sample 3–5 个短 prompt，最多 2 分钟：

- 哪一步最犹豫？
- 什么信息或后果最意外？
- 如果重做一次，你会改什么？
- 哪种工作最投入 / 最不喜欢？
- 下一步最想验证什么？

字段：`promptId / response / source=reflection / timestamp`。Reflection 不改变 Hard Skill；与系统 observation 不一致时记录为 tension，供报告叙事使用。

## L. AI JUDGE GOLDEN TEST PLAN

每个岗位至少准备以下 8 类 fixture（每类覆盖核心 rubric）：`strong / average / partial / valid-opportunity-failed(Level0) / not_observed / eloquent-unsupported / initially-weak-self-corrects / strong-answer-wrong-evidence`。另加 canonical strategy paraphrase、missing constraint、unexercised fallback、simulation-trace leakage tests。

判定验收：

- 同一 event/state/consequence 输入多次结果一致；
- `not_observed` 无 performanceLevel；Level 0 只在 valid opportunity + failed evidence 时出现；
- unsupported prose 不得因流畅度加分；
- consequence trace 不得进入 participant Behavior；
- role hard skill 与 collaboration evidence 使用不同 requirement/claim target，禁止 additive credit；
- replay 能回到 work object、action、reason、consequence、interpretation、limits。

## M. LOCAL DIRECTORY PLAN

不复制现有 canonical files；新增内容 workspace 仅放 authoring artifacts：

```text
Wayve/content/
  authoring-blueprint.md              # 本文件，single current version
  README.md                           # ownership / source precedence / validation
  work-samples/
    ai_product/README.md
    ai_ui_design/README.md
    ai_ops/README.md
    ai_data_eval/README.md
    ai_app_dev/README.md
  collaboration/launch-war-room/
    README.md
    private-briefs.md
  rubrics/README.md
  reflection/README.md
  golden-tests/README.md
```

实现时仍以 `local_exports/...` Frozen Specs 与 Product SOT 为 source of truth；新目录不生成 `v1/v2/final2/RC` 等并行活跃版本。

## N. CONTENT PRODUCTION PLAN

1. **Contract alignment**：冻结 canonical requirement IDs 与旧 pack ID 的映射，补齐每岗六项 catalog 的 observation opportunity。
2. **Role authoring**：逐岗把现有 pack 改写成 coherent work sample；先写 state/consequence，再写 copy。
3. **Scaffolding pass**：为每岗补 minimum context、glossary、hint、specialist knowledge boundary。
4. **Collaboration pass**：完成 Launch War Room shared state、五份 private brief、四 beat strategy families 与 normalization schema。
5. **Rubric + golden tests**：每岗 8 类 golden case，验证 no-score/no-fit/not-observed/replay 边界。
6. **Content QA**：做 determinism、coverage、replay、accessibility、language clarity 审计；不触碰 frontend/backend。
7. **Human Review gate**：将本蓝图与首轮样稿交 Product Owner / GPT Web；未获批准前停止大规模 production。

## Batch 1 Calibration Addendum

### AI Product final calibration

Opening 只提供注册增长、首次成功生成偏弱、D7 回访偏弱和本周一个工程槽位；不直接泄漏导入断点或摘要满意度。原“恰好选择两类证据”不再作为 active interaction。改为“距离评审还有 8 分钟，还能补两轮信息”：用户每次向一个来源/角色追问或打开一份材料。行动记录 `information_need → role_directed_request → received_input → user_interpretation`。未使用机会保留为 uncertainty，不自动扣分。

三个候选方向必须在不同 supplied evidence 下都合理：简化设置/导入、首次使用模板、提升摘要质量各自拥有支持证据、反证、受益人群、指标、成本和未知；不得使用一正确、两弱的结构。

### Requirement-specific rubric

AI Product 六项独立 rubric 已写入 `content/rubrics/ai-product-requirement-rubrics.md`。同一份 final note 不得自动让六项一起高分；Hint 提供的 reasoning 不计为用户主动发现。

`cross_team_push` 不再把“没有 role-directed request”一律判为 `not_observed`：没有真实必要协作机会时才是 `not_observed`；存在明确 dependency 而用户无视、做 unsupported assumption 或不整合必要输入时，可按 rubric 评为 Level 0/1/partial。已有充分 supplied evidence 时不强迫联系 Dev/Ops。

### War Room authority calibration

五岗各自拥有 domain state authority，但不拥有对所有 rollout 的对称 veto。`rollout eligibility` 由 shared state + deterministic rules 计算；scope reduction、staged rollout、limited cohort、fallback、recovery、additional evaluation、changed timing 均可解决或重新进入 conditional/pass。`campaignWindow` 默认是 business consequence，不自动等同 hard technical gate。

### Beat calibration

- Beat 1 是 `Opening Move`：决定先做什么；之后仍可进行其他信息交换。它影响 time budget、discussion order 与 remaining uncertainty。
- Beat 4 是 `Final Commitment Artifact`，不是三选一。最终必须表达 rollout mode、scope、cohort、owner、success/monitor metric、guardrail、rollback、unresolved risk、follow-up experiment。真正 trade-off 来自前面各 domain state。

### Chinese-first review

AI Product 与 Launch War Room 的用户可见文案以自然中文为主；canonical IDs / event IDs / schema fields 保留英文。

## O. RISKS / PRODUCT QUESTIONS

### 风险

- `ai_data_eval`、`ai_app_dev`、`ai_ui_design` CSV 为空，role research 证据等级低；当前蓝图只能依赖 Frozen Specs + 旧题库，不应宣称外部岗位统计代表性。
- `ai_ops` 子类型过宽；第一版需明确 activation/growth/feedback-loop 场景是首个 sample，不是永久 taxonomy。
- 现有 frontend registry 的 requirement IDs 与新版六项 catalog 不同；若不先做映射，AI Judge 会出现 unsupported claim 或错误 not_observed。
- 五岗 work sample 若同时加入过多 interaction，会重新变成“题型集合”；每岗必须保持一个主工作对象、一次 consequence、一次 revision。
- collaboration 四 beat 的策略代价必须由 fixture 实证，不能用 option index 或先验偏好评分。

### 已应用的 Product Owner 决策

- 五岗新版六项 Requirement IDs 已成为唯一 active canonical IDs；legacy IDs 仅作 mapping / provenance / migration reference。
- Launch War Room 第一版统一使用 AI Meeting Assistant Launch。
- AI Operations 第一套 sample 聚焦 activation、retention、feedback loop、product operations；其他 subtype 进入 future backlog。
- Standard Experience 默认包含 Collaboration Sprint；`Skip for now` 记录 `observationStatus=not_observed`，不产生 performance penalty。
- 五岗都必须拥有 binding state authority；UI Design 与 Data Evaluation 不再作为 supporting NPC 设计。

本 Blueprint 当前没有未决的 Product Owner blocker；后续 Human Review 重点转为 Batch 1 内容质量与可玩契约验收。
