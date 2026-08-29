# CONTENT TEAM REVIEW PACK — Batch 1 Calibration

这是给内容同学的中文优先评审稿，不需要先读 schema。

## AI Product：用户实际看到的场景

你是 AI 会议助手 activation workstream 的产品负责人。最近注册人数上升，但首次成功生成和七天后回访都偏弱；工程本周只能交付一个主要改进。开场不会告诉你问题发生在哪一步。你先做暂定决定，再使用有限沟通预算定位问题，最后面对新事实确认或修改。

### 三个候选方案

1. **简化设置 / 导入**：如果早期设置是主断点，可能提升首次完成率；如果问题在摘要价值，投入会错过下游体验。
2. **首次使用模板**：如果用户缺少开始会议的结构，可能提升启动与生成率；如果用户已能启动但不回访，模板未必解决留存。
3. **提升摘要质量**：如果用户成功生成后不满意，可能改善满意度与 D7；如果主要流失发生在生成前，短期覆盖有限。

### Information Objects

| 对象 | 用户能看到什么 | 可能影响 |
|---|---|---|
| 漏斗 / 分群 | 注册、首次生成、第二次使用；不同用户组 D7 | 断点在哪、哪类人受影响 |
| 用户反馈 | 首次使用各步骤的原话 | 用户为什么卡住、成功后重视什么 |
| 工程约束 | 人日、依赖、A/B 可测性、模型/后处理影响 | 本周能否交付、如何验证 |
| 历史材料 | 过去变化或背景 | 补背景，但可能不直接回答当前问题 |
| Twist | 企业留存更强；个人用户在导入流失；完成导入者认可摘要价值 | 让原始 priority 产生上下游张力 |

### Limited-information framing

距离评审还有 8 分钟，你还能补两轮信息。每次只能找一个来源/对象，并会占用会议注意力。你可以问用户反馈侧、问 Ops / data owner、问 Engineering，或检查漏斗 / 分群。没看的信息不会自动扣分，但会成为 final uncertainty / nextTest。

### Twist 与 consequence

- 选导入：若早期断点证据充分，可能提升首次完成率，但仍需验证下游价值；
- 选模板：若启动缺少结构，可能提升首次生成，但对已发生的留存问题仍不确定；
- 选摘要：若成功生成后满意度低，可能改善 D7，但若断点在前置流程，短期覆盖有限。

没有唯一正确答案；retain 与 revise 都可以成立，但必须与实际 evidence relationship 一致。

### Final deliverable

选择、暂缓项、理由、代价、验证指标、未决问题、下一步向哪个角色请求什么行动。

### Beginner explanation

- 首次成功使用：第一次拿到有用的会议摘要。
- D7 回访：七天后是否回来继续使用。
- 分群：按使用方式或来源划分的用户组。
- 验证指标：判断改动是否有帮助的数字或行为。

### Optional hint

默认只提供 glossary。用户主动点击“给我一个思考提示”后显示：

> “你可以先定位：用户是在得到价值前卡住，还是得到价值后觉得不够好？再检查手里的证据是否真的支持这个判断。”

记录 `scaffoldingUsed.reasoningHint=true`。提示不扣分，但提示提供的 framing 不能被记录为用户主动发现。

## Launch War Room

同一个 AI Meeting Assistant 要不要在本周上线？同时存在 activation drop、quality badcase、p95 latency/cost、AI uncertainty、campaign window 与资源限制。

### 五岗 private brief

| 角色 | 私有信息 | 约束 | 能改变什么 |
|---|---|---|---|
| Product | activation target、scope、business priority | 一个 launch slice | scope、priority、success criterion |
| Operations | cohort funnel、campaign calendar、baseline | window 与资源固定 | target cohort、timing、intervention、reach、guardrail |
| Data Evaluation | badcase、boundary uncertainty、coverage gap | audit slots 与 gate budget | quality gate、sampling、retest、release posture |
| Application Development | runtime、p95、cost、capacity、fallback | latency ceiling 与 capacity | routing、source scope、fallback、rollout mode |
| UI Design | loading、partial、uncertainty、recovery evidence | unresolved 不能显示 confirmed | disclosure、recovery、handoff、user acceptance |

### 每岗四 Beat 可执行动作

| Beat | Product | Ops | Eval | Dev | UI |
|---|---|---|---|---|---|
| 1 开场 | 先报目标/约束 | 先报 cohort/window | 先报风险/coverage | 先报瓶颈/capacity | 先报用户状态风险 |
| 2 初始提案 | narrow / staged / hold | 选 cohort + intervention | 设 gate posture | 选 routing / rollout | 选 state contract |
| 3 冲突响应 | 缩 scope / defer | 改 reach / timing / guardrail | 调 sampling / gate / retest | 加 fallback / degraded mode | 加 disclosure / recovery / handoff |
| 4 最终承诺 | integrated scope | campaign owner/monitor | gate/retest condition | technical owner/p95 gate | user acceptance/usability monitor |

### Concrete fixture

| 事实 | 当前值 / 说明 |
|---|---|
| Activation baseline | 10,000 注册；4,100 首次生成；1,600 第二次使用 |
| 用户断点 | 开场未知；通过后续反馈、漏斗与分群材料逐步定位 |
| Quality evidence | 高风险 badcase 尚有一组未完成复测；gate 为 conditional |
| Technical evidence | 核心路径 p95 目标 2.5 秒；跨文件综合约 3.8 秒；峰值容量有限 |
| Campaign timing | 校园合作活动还有 48 小时，窗口正在收窄 |
| User-state evidence | 未标记 partial checklist 被误认为完成；整页重试会丢失已确认项目 |
| Resources | 本周只能支持一个主要工程切片；人工审核预算 6 个 audit slots |

### Final Commitment

不是三选一，而是一份完整承诺：`rolloutMode / launchScope / targetCohort / owner / successMetric / monitorMetric / keyGuardrail / rollbackCondition / unresolvedRisk / followUpExperiment`。

## 请内容同学重点 Review

- 像不像真实工作，而不是答题？
- 三个方案是否都 plausible？
- 是否存在明显正确答案？
- 中文是否自然、是否有翻译腔？
- beginner 是否看得懂但仍需专业判断？
- 术语是否首次解释？
- consequence 是否能由 supplied fixture 推出？
- UI / Eval / Dev / Ops 是否真的能改变 shared state？
- Product 是否被约束整合，而不是单方面 override？

## Concrete Content Draft

### AI Product opening copy

“距离产品评审还有 8 分钟。本周工程只能交付一个主要改进。最近注册人数增加，但首次成功生成和七天后回访都偏弱。你先写下暂定方向；之后还能补两轮信息。”

Opening 不出现“个人用户在导入流失”或“完成导入者喜欢摘要”。这些事实只通过后续材料与 twist 获得。

### 三个方向的对称证据矩阵

| 方向 | 最强支持证据 | 最强反证 | 主要受益人群 | 可能影响指标 | 工程/机会成本 | 重大未知 |
|---|---|---|---|---|---|---|
| 简化设置 / 导入 | 用户反馈称“不知道上传哪种记录”；漏斗在首次生成前变窄 | 若成功生成后满意度低，修导入无法改善 D7 | 首次使用的新用户 | 首次完成率、首次生成率 | 5 人日、依赖少；可能错过下游质量改进 | 个人与企业是否同样受益 |
| 首次使用模板 | 新用户常见会议缺少起点；模板可能减少空白页犹豫 | 若用户已成功生成但不回访，模板影响有限 | 没有明确会议结构的新用户 | 首次启动率、首次生成率 | 8 人日，需要内容准备；占用本周唯一切片 | 模板是否覆盖真实高频会议 |
| 提升摘要质量 | 反馈称摘要有遗漏或行动项冲突；满意度可能影响 D7 | 若主要流失发生在生成前，质量改进触达不到这些人 | 已完成生成、重视结果质量的用户 | 摘要满意度、D7 回访 | 12 人日，涉及模型/后处理，验证周期长 | 质量提升是否足以改变回访 |

### Follow-up responses

- 用户反馈侧：“有用户说不知道要上传哪种会议记录；也有人说摘要出来后有用，但第二次不知道从哪里开始。”
- Ops / data owner：“企业用户 D7 约 34%，个人用户约 11%；这只能说明分群差异，不能单独说明断点原因。”
- Engineering：“导入改动约 5 人日、依赖少；模板约 8 人日；摘要改进约 12 人日，涉及模型和后处理。”
- Funnel / cohort：“10,000 注册 → 4,100 首次生成 → 1,600 第二次使用；当前还没有按每一步拆开的个人用户数据。”

### War Room role contribution

用户作为不同角色进入时，不只是看到不同 brief：

- Product 交付 integrated scope 与 business commitment；
- Ops 交付 target cohort、timing、intervention 与 guardrail；
- Eval 交付 quality gate、sampling、retest 与 release posture；
- Dev 交付 routing、fallback、technical feasibility 与 p95 condition；
- UI 交付 state contract、recovery、handoff 与 user acceptance。

所有贡献都能改变 shared state，并由同一 concrete fixture 产生后续 consequence。
