# Batch 1 Rubric Contract

## Contextual level

| Level | Authoring rule |
|---|---|
| 4 | 主动引用关键证据，承认约束，解释 benefit/cost，行动与后果一致，并对新信息做可解释修订/确认。 |
| 3 | 大部分 requirement behavior 可用，证据—决策关系清楚，仅有小遗漏。 |
| 2 | 有局部可用行为，但证据关系、约束或 consequence interpretation 不完整。 |
| 1 | 主要是偏好、术语或 unsupported assertion；不能支撑完整 bounded claim。 |
| 0 | 有真实有效机会，但无 usable requirement performance 或 materially off-task。 |

`not_observed` 没有 performanceLevel；表示没有有效机会或没有 usable evidence。`partially_observed` 表示机会存在但只覆盖 requirement 的一部分，可给窄 contextual level。

## AI Product edge cases

- **Level 4**：选择导入改进，引用 funnel + feedback + effort，说明个人用户断点、摘要下游价值与 one-slot 代价；twist 后修改 metric 为 activation + D7 guardrail，并请求 Dev/Ops 下一步。
- **Level 3**：选择与证据基本一致，有 trade-off 与 metric，但未明确 segment limitation。
- **Level 2**：选择合理但只说“提升体验”，未连接 evidence 与 consequence。
- **Level 1 / eloquent-but-unsupported**：术语流畅，未引用任何卡片或约束；最高 Level 1。
- **Level 0**：有完整 evidence opportunity，却提交与产品决策无关内容或未形成任何 one-slot commitment。
- **not_observed**：未建立用户证据—决策关系；不能因为理由短就判 0。
- **partially_observed**：完成 priority 与 metric，但没有 feasibility 或 cross-team evidence；只对已覆盖部分作 claim。
- **initially-wrong-but-self-corrected**：初始选摘要质量，看到个人用户导入流失后说明假设变化并改为导入；revision 可达 Level 3–4，但必须有 before/after 与 evidence refs。
- **correct-conclusion-wrong-evidence**：最终选导入，但引用的卡片并不支持导入断点；结论偶然正确，evidence-related claim 通常不超过 Level 2。
- **canonical strategy paraphrase**： “先把本周范围缩到最核心路径” normalize 为 narrow-scope strategy，不因未使用 canonical wording 扣分。
