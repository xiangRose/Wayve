# AI Product Requirement-specific Rubrics — Batch 1 Calibration

每项只看自己的 Behavior + Evidence relationship。`not_observed` 没有 performanceLevel；`partially_observed` 只对已覆盖部分作窄 claim。

## user_insight

| Level | 定义 |
|---|---|
| 4 | 从用户反馈、漏斗/分群行为识别具体断点，区分“未到达价值”和“到达后价值”，并用于 priority/test。 |
| 3 | 指出主要用户断点并引用至少一种用户/行为证据，但分群差异或下游价值不完整。 |
| 2 | 提到痛点或流失位置，但未把证据与决策连接。 |
| 1 | 泛泛说“改善体验/提升留存”，没有 supplied user evidence。 |
| 0 | 有用户数据机会，却提交无关或明显相反解释。 |
| partially_observed | 只有反馈或只有漏斗证据；只能作单一来源 claim。 |
| not_observed | 没有查看/引用用户或行为 evidence。 |

Evidence required：具体 feedback、funnel/segment signal 与决策关系。Evidence insufficient：只打开材料、只写“用户不喜欢”。False positive：把 retention 数字直接当动机；把 hint framing 当主动洞察。Claim boundary：不证明一般同理心或研究能力。

## problem_definition

| Level | 定义 |
|---|---|
| 4 | 明确谁在什么步骤遇到什么问题、造成什么影响、这次不解决什么，并随新证据修正范围。 |
| 3 | 问题范围、用户阶段和影响基本清楚，有一个边界遗漏。 |
| 2 | 能复述症状，但未形成可行动边界，或把方案当问题。 |
| 1 | 只重复“注册多、留存低”等现象。 |
| 0 | 有完整 scenario 机会却提出与 activation 无关的问题。 |
| partially_observed | 只界定症状或人群，缺少影响/边界一侧。 |
| not_observed | 没有可回放的问题陈述。 |

Evidence required：actor-stage-impact-boundary。Evidence insufficient：只提交功能名称。False positive：把“做导入改进”当问题定义。Claim boundary：不证明市场判断。

## product_judgment

| Level | 定义 |
|---|---|
| 4 | 在冲突证据与一槽位约束下形成判断，说明现在做什么、暂缓什么，并在 twist 后合理 retain/revise。 |
| 3 | 判断与主要证据、约束一致，解释选择与一个代价。 |
| 2 | 选择合理，但主要靠直觉或只处理单一指标。 |
| 1 | 选择存在但 unsupported，或把任意结果称为最佳。 |
| 0 | 有完整判断机会却无 priority/deferred decision，或 materially off-task。 |
| partially_observed | 有初始或最终判断，但没有 consequence response。 |
| not_observed | 没有可回放的产品判断行为。 |

Evidence required：board before/after、evidence relationship、consequence response。Evidence insufficient：最终选项本身。False positive：把“选中高价值项”当 judgment。Claim boundary：不证明一般 product sense。

## ai_feasibility

| Level | 定义 |
|---|---|
| 4 | 使用 effort、dependency、model/post-processing 或 testability 约束，解释其如何改变 scope、metric 或 rollout，并形成 actionable engineering request。 |
| 3 | 引用主要工程约束并纳入选择，遗漏一个依赖或验证限制。 |
| 2 | 注意到 effort 差异，但未说明交付/验证影响。 |
| 1 | 只说“技术复杂/容易上线”，无 supplied evidence。 |
| 0 | 有工程机会却违反已知约束且无承认。 |
| partially_observed | 只获得 effort 或 dependency，未形成影响链。 |
| not_observed | 未请求、查看或引用工程信息。 |

Evidence required：工程对象、role-directed request、scope/metric consequence。Evidence insufficient：仅写“低成本”。False positive：把 validation metric 当 feasibility。Claim boundary：不证明编码或架构能力。

## prioritization_tradeoff

| Level | 定义 |
|---|---|
| 4 | 明确容量槽、延期项、至少两项 competing value/cost，并用 guardrail 或 metric 管理代价。 |
| 3 | 明确优先级、延期范围和一个真实代价。 |
| 2 | 完成排序，但代价是口号或延期含义不清。 |
| 1 | 只说“都重要”或不承担取舍。 |
| 0 | 有一槽位机会却未形成可执行 priority/defer state。 |
| partially_observed | 有排序但无代价，或有代价但无 deferred scope。 |
| not_observed | 没有可回放的 constrained board state。 |

Evidence required：capacity slot、deferred items、trade-off、metric/guardrail。Evidence insufficient：单一点击。False positive：把 roleImportance 当用户应选项。Claim boundary：不证明长期 roadmap 能力。

## cross_team_push

| Level | 定义 |
|---|---|
| 4 | 在确有 dependency 或关键未知时，完成 `information need → role-directed request → received input → interpretation → decision integration → handoff/next action` 全链路，并明确 owner。 |
| 3 | 完成请求、收到输入、解释并纳入决策，但 handoff 或 owner 不完整。 |
| 2 | 发起角色定向请求并引用输入，但未真正整合，或只完成链路前半段。 |
| 1 | 只点击/查看 Dev/Ops，或泛称“需要协作”。 |
| 0 | 存在明确且必要的 role dependency，用户却无视关键输入、做出与已知约束冲突的 unsupported assumption，或声称完成不存在的 handoff。 |
| partially_observed | 有真实 dependency 且完成 request→input 或 interpretation→decision 的部分链路；也适用于只整合了一个必要角色。 |
| not_observed | 场景没有真实必要的 role interaction，或当前 supplied evidence 已足够完成判断、没有合理的 coordination opportunity。 |

Evidence required：当场景存在 genuine dependency 时，需要请求对象、接收内容、用户解释、决策整合、handoff/next action；若不存在 genuine dependency，则不强迫请求角色。Evidence insufficient：点击查看、礼貌表达、泛化协作语句。False positive：“我会和工程沟通”；把“没找 Dev”一律判低分。Claim boundary：不证明一般领导力或稳定协作能力。
