# Batch 1 Golden Cases

## AI Product

| Case | Input / behavior | Expected judging boundary |
|---|---|---|
| Level 4 | 证据、约束、代价、revision、metric、handoff 均明确 | Level 4；六项中仅对有 evidence 的 requirement 出 claim |
| Level 3 | 主要 evidence 与选择一致，遗漏一个 guardrail | Level 3 |
| Level 2 | 有 priority 与理由，未解释 evidence relationship | Level 2 |
| Level 1 | 流畅术语，无 supplied evidence reference | ≤ Level 1 |
| Level 0 | 有机会但无 priority/deferred，或 materially off-task | Level 0 |
| not_observed | 没有 user insight 或 cross-team usable evidence opportunity | 无 performanceLevel |
| partially_observed | 有 priority，但未覆盖 feasibility/handoff | 窄 claim；不把未覆盖部分当失败 |
| eloquent-but-unsupported | “应该提升留存和体验”，无卡片/约束 | 不因语言流畅加分 |
| initially-wrong-but-self-corrected | 初始摘要质量，twist 后基于导入证据改为导入 | revision evidence Level 3–4 可能成立 |
| correct-conclusion-wrong-evidence | 选对切片但引用不支持的证据 | evidence claim 降级；不奖励偶然结论 |
| paraphrase | “先做最小可回滚版本” | normalize 到 narrow/staged strategy family |

## Launch War Room Collaboration Evidence

| Case | Observable event | Expected boundary |
|---|---|---|
| strong | 主动请求关键证据、明确 constraint、承认冲突、修订 proposal、承担 owner | collaboration events 可 replay；不生成 personality label |
| partial | 分享信息但未确认他人 constraint，或提出 action 但未说明 consequence | `partially_observed` collaboration claim |
| Level 0-like failure | 有发言机会但 materially off-topic / 无法回应 shared state | 仅对应 collaboration opportunity 可为 Level 0，不影响 role hard skill 未覆盖项 |
| not_observed | Skip for now，或该 Beat 没有参与者可用 action | `observationStatus=not_observed`，无 penalty |
| eloquent unsupported | 复述“协作很重要”，没有 evidence/action/request | 不转成 collaboration evidence |
| self-correction | 先提 full launch，看到 Eval/UI/Dev/ops blocked condition 后转 staged | `proposal_revised` + `constraint_acknowledged` 可支持 response-to-change |
| wrong evidence | 声称 Eval 已通过，但实际只看到 preliminary sample | evidence mismatch；不奖励最终 rollout choice |
