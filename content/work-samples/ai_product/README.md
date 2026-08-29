# AI Product Reference Work Sample

状态：`BATCH 1B — REFERENCE AUTHORING`

## 场景

你是 AI 会议助手 activation workstream 的产品负责人。注册增长，但首次成功生成与 D7 回访偏低；本周工程只能交付一个有意义的改进。开场不会告诉你问题发生在哪一步。你需要先形成暂定判断，再通过有限追问定位问题，面对新信息后确认或修改决策。

## Work Object

持续存在的 `Activation Decision Board`：候选切片、容量槽、延期区、证据收件箱、决策记录。所有动作都改变同一块工作板。

## Information Objects

- 漏斗 / 分群卡：注册 → 首次生成 → 第二次使用，以及不同用户组的回访；
- 用户反馈卡：用户在首次使用各步骤的原话；
- 工程可行性卡：人日、依赖、可测试性；
- 历史材料：过去变化与背景；
- twist bundle：通过追问逐步揭示 setup/import、segment、summary satisfaction 与 feasibility 的组合关系。

## Interaction Contract

1. 排列三个候选切片，把一个放入容量槽，两个放入延期区，并写短理由。
2. 距离评审还有 8 分钟，你还能补两轮信息。每次只能向一个来源追问或打开一个具体材料，并会占用会议注意力；可以问用户反馈侧、Ops / data owner、Engineering，或检查漏斗 / 分群。
3. 查看与当前切片相关的 consequence implication；未使用的机会保留为真实 uncertainty，不自动判错。
4. 选择 retain 或 revise；可修改切片、延期项、metric、trade-off、uncertainty。
5. 提交 bounded decision note。

无唯一正确切片。相同 initial state + evidence path + twist facts + final state 必须产生相同 replay 与 consequence。

## Limited Information / Communication Budget

这不是“请选择两张正确卡片”。用户只有两次快速追问机会：

| 追问行动 | 可能获得 | 代价 / 未知 |
|---|---|---|
| 问用户反馈侧 | 某一步的具体痛点与成功后价值 | 可能错过分群或工程确认 |
| 问 Ops / data owner | cohort 差异、时间窗口与监测口径 | 对用户动机或技术可行性仍不确定 |
| 问工程侧 | effort、依赖、上线风险 | 对用户动机与分群仍不确定 |
| 检查漏斗 / 分群 | 断点位置与人群差异 | 不能直接解释动机或解决方案可行性 |

每次追问记录 `information_need → role_or_source_requested → received_input → user_interpretation`。未查看对象进入 final uncertainty / nextTest；只有存在真实 dependency 时，才要求 role-directed coordination。

## Three-option evidence matrix

| 方向 | 最强支持证据 | 最强反证 | 受益人群 | 可能指标 | 成本 / 机会成本 | 重大未知 |
|---|---|---|---|---|---|---|
| 简化设置 / 导入 | 用户说不知道上传哪种记录；首次生成前漏斗变窄 | 成功生成后不满意时，修导入难改善 D7 | 新用户 | 首次完成率、首次生成率 | 5 人日、依赖少；错过质量改进 | 是否各分群同样受益 |
| 首次使用模板 | 常见会议缺少起点；模板可减少空白页犹豫 | 已成功生成但不回访时，模板影响有限 | 缺少会议结构的新用户 | 启动率、首次生成率 | 8 人日、需内容准备 | 是否覆盖真实高频会议 |
| 提升摘要质量 | 摘要遗漏/行动项冲突反馈；可能改善 D7 | 生成前流失用户接触不到质量改进 | 已成功生成用户 | 满意度、D7 | 12 人日、涉及模型后处理、验证更慢 | 提升是否足以改变回访 |

## Consequence Contract

| 当前切片 | 可见收益 | 可见代价 / 未知 |
|---|---|---|
| 简化设置/导入 | 若证据显示早期 setup/import 是主断点，可提升首次完成率 | 若问题在摘要价值，投入会错过下游体验 |
| 首次使用模板 | 若常见会议缺少起点，可能提升首次启动与生成率 | 若用户已能启动但不回访，模板未必解决留存 |
| 提升摘要质量 | 若用户成功生成后不满意，可改善满意度与 D7 | 若主要流失发生在生成前，短期覆盖有限 |

## Final Deliverable

`selectedSlice / deferredSlices / reason / tradeoff / validationMetric / uncertainty / nextTest`

中文用户可见提示：

- “你会把哪个改进放进本周唯一的交付槽？”
- “你会先查看哪两类信息？”
- “新信息出现后，你会保留原判断，还是调整方向？”

## Six-Requirement Evidence Contract

| canonical requirementId | 可观察机会 | Evidence source | claim boundary |
|---|---|---|---|
| `user_insight` | 将用户反馈、分群行为与问题定义联系起来 | feedback/funnel/retention comparison | 只支持本场景的用户证据联系，不证明一般用户洞察 |
| `problem_definition` | 明确“注册增长但首次价值/导入断裂”的问题 | scenario + initial reason + final note | 只支持本次 activation problem framing |
| `product_judgment` | 在新信息与约束下形成产品选择 | board state + consequence response | 不证明一般产品 sense |
| `ai_feasibility` | 使用 effort/dependency/model constraint 调整选择 | engineering card + trade-off | 不证明真实工程设计能力 |
| `prioritization_tradeoff` | 一槽位、延期范围、metric 与代价 | initial/final board | 不证明长期 roadmap 能力 |
| `cross_team_push` | 请求并引用 Ops/Dev 信息，形成 owner/action handoff | evidence request + final nextTest | 不证明稳定领导力或跨团队表现 |

每个 Evidence object 只绑定一个 canonical requirement。`roleImportance` 仅作为岗位标准元数据：9/9/10/7/10/9。

## Scaffolding

### Minimum context

注册、首次成功生成、D7 回访、工程一槽位、三个候选切片的 plain-language 解释。

### Glossary

- **首次成功使用**：新用户第一次得到有用的会议摘要。
- **D7 回访**：用户七天后是否再次使用。
- **分群**：按使用方式或来源划分的用户组。
- **验证指标**：用来判断改动是否有帮助的可观察数字或行为。

### Embedded explanation

每张卡先显示一句结论，再提供“查看细节”；不要求计算公式、SQL、roadmap 术语或 AI 模型知识。

### Optional hint

默认不显示 reasoning hint。用户主动点击后显示：“你可以先定位：用户是在得到价值前卡住，还是得到价值后觉得不够好？再检查手里的证据是否真的支持这个判断。”记录 `scaffoldingUsed.reasoningHint=true`；提示 framing 不算主动洞察，不机械扣 performanceLevel，但可降低 claim confidence / 主动性措辞强度。

### Specialist boundary

必须观察：证据与产品决策的关系、约束与取舍、对新信息的修订。无需观察：复杂市场 sizing、完整 PRD、视觉设计、真实实验执行。

## Not Observed / Level 0

- `not_observed`：没有有效观察机会，或 supplied evidence 已足够完成判断而不存在 genuine role dependency。若存在必要 dependency 却被忽略，则按 rubric 评为 Level 0/1/partial，而不是自动 not_observed。
- Level 0：机会真实存在但用户 materially off-task，例如看到完整证据后提交与会议助手无关的答案。

## Replay

`replay_initial_commit`、`replay_evidence_path`、`replay_twist_response`、`replay_final_test`；每条包含 work object、action、reason、consequence、requirement、interpretation、limits。
