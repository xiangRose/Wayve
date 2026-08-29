# Role Work Sample 与 Collaboration

状态：CURRENT PRODUCT SOT — APPROVED FOR FEISHU SOT

## MVP Scope Cut Override

当前 Hard Skill 仅使用 Scenario-based Hard Skill Assessment；Role Work Sample 机制为 Future / Post-MVP。Collaboration MVP shared world 为 Nova V3 / AI Product Launch Review。

## Role Work Sample 契约

原 Career Trial 现在统一称为 Role Work Sample：一个有边界的真实岗位工作片段。共享内部语义主轴为：

`scenario → first_judgment → evidence_gathering → twist → reconsideration → final_decision`

这不是六个页面的要求。一个节点可观察多个 requirement，一个 requirement 可由多个节点支持；本次未合理观察到的 requirement 记录为 `not_observed`。

## 五岗交互语法

| 岗位 | Work Sample 语法 |
|---|---|
| AI Product | 证据检查、优先级、后果、修订 |
| AI UI Design | 状态 / 转移编辑、模拟用户、恢复、修订 |
| AI Operations | 漏斗 / cohort 诊断、干预、结果、修订 |
| AI Data Evaluation | badcase、rubric / gate、audit、retest |
| AI Application Development | trace、配置、固定测试集、regression、修订 |

五份 Frozen Trial Specs 是岗位机制来源，升级为 Role Work Sample，不另建题库。覆盖 requirement 不是题目数量。一个 Work Sample 通常稳定观察约 4–6 个相关 requirement，完整 catalog 仍然保留。

## Collaboration

Standard Experience 默认包含 Collaboration，并提供 `Skip for now`。跳过后 Collaboration 为 `not_observed`，不产生 performance penalty，Working Portrait 不得生成未经观察的跨角色 claim；report 必须说明本次未观察跨角色协作行为。Demo Mode 可使用 shortened Collaboration path。

## Launch War Room

第一版使用 Reconverging Deterministic State Graph，而不是组合式剧情树。四个 Decision Beats：

1. Information Exchange；
2. Initial Proposal；
3. Twist / Conflict Response；
4. Final Commitment。

每个 Beat 有三个有真实收益和代价的 strategy families，支持有限结构化 free response、normalization、deterministic consequence，并在后果后汇合至下一 shared state。禁止 3 × 3 × 3 × 3 独立剧情树。五个岗位必须各自拥有 exclusive information、unique actionable agency、他人需要的信息和 meaningful final trade-off。
