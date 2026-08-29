# Launch War Room — Content Playable Contract

## Shared World

AI Meeting Assistant 即将上线：activation drop、质量 badcase、p95 latency/cost、AI 输出 uncertainty、campaign deadline 同时存在。五岗在同一 shared state 上作出 constraint-integrated commitment。

## Shared State

```yaml
launchScope: core_activation | core_plus_quality | deferred
rolloutMode: full | staged | limited | hold | additional_evaluation
qualityGate: pass | conditional | blocked
technicalFeasibility: pass | conditional | blocked
userStateAcceptance: pass | conditional | blocked
targetCohort: personal | enterprise | mixed | limited_cohort
campaignWindow: open | narrow | missed
rollbackCondition: defined | missing
unresolvedRisks: []
```

`full` 的硬 eligibility 条件是 `qualityGate=pass`、`technicalFeasibility=pass`、`userStateAcceptance=pass`、`rollbackCondition=defined` 且存在 target cohort。`campaignWindow` 默认是业务 consequence：`open` 支持完整 reach，`narrow` 可能迫使 staged/limited 或缩小 cohort，`missed` 不自动等同技术禁止；只有 fixture 明确规定不可逆外部承诺时才升级为 hard constraint。

## Binding State Authority

- Product：scope、business priority、success criterion；不能覆盖其他专业 blocked 条件。
- Operations：target cohort、campaign timing、intervention、resource、guardrail。
- Data Evaluation：quality gate、sampling、retest、release posture。
- Application Development：routing、source scope、fallback、rollout feasibility。
- UI Design：user-facing state contract、uncertainty disclosure、recovery、handoff acceptance。

## Four Beats

1. Information Exchange：交换证据 / 声明 constraint / 对齐 launch criterion。
2. Initial Proposal：narrow ship / staged gate / hold for evidence。
3. Twist / Conflict Response：guardrail / scope reduction / fallback-human handoff。
4. Final Commitment：提交完整 commitment artifact，不在 owner、metric、rollback、experiment 之间三选一。

每个 Beat 三种策略均有 benefit 与 cost；执行后 reconverge 到共享 next state。

## Strategy / consequence table

| Beat | Strategy family | Benefit | Cost | Shared-state effect |
|---|---|---|---|---|
| Information Exchange | 先交换关键证据 | 降低关键未知 | 讨论时间增加，可能延迟承诺 | unresolvedRisks 更具体；campaignWindow 可能变窄 |
| Information Exchange | 先声明个人 constraint | 快速暴露 binding limits | 可能过早收窄方案 | technicalFeasibility / qualityGate / userStateAcceptance 更早进入 conditional |
| Information Exchange | 先对齐共同 launch criterion | 形成共同判定语言 | 初始信息深度较低 | rollbackCondition 与 success criterion 更清晰 |
| Initial Proposal | Ship narrow slice | 保留 deadline 与学习速度 | scope/覆盖减少 | launchScope=narrow；rolloutMode=limited 或 staged |
| Initial Proposal | Stage-gated launch | 在有限范围验证 | 运营 reach 与学习速度下降 | rolloutMode=staged；需满足 gate 才扩大 |
| Initial Proposal | Hold for more evidence | 降低未知质量风险 | 错过 campaign window、延迟学习 | rolloutMode=hold；unresolvedRisks 增加但可转 additional_evaluation |
| Twist / Conflict Response | 保护关键 guardrail | 降低高风险 badcase / UX 误解 | 覆盖和速度下降 | qualityGate 或 userStateAcceptance 保持 conditional/blocked |
| Twist / Conflict Response | 缩减 scope 保 deadline | 保住 campaign window | 用户价值与覆盖减少 | launchScope=deferred/core；targetCohort 变窄 |
| Twist / Conflict Response | fallback / 人工兜底 | 保持可用性与连续性 | 成本、人工负担、体验不一致 | technicalFeasibility 可恢复 pass/conditional；rollback 更复杂 |
| Final Commitment | owner + metric | 执行责任清晰 | 前期承诺成本高 | rollbackCondition=defined；unresolvedRisks 有 owner |
| Final Commitment | rollback gate | 提高可逆性 | 需要额外监控和阈值 | rolloutMode 受 gate 保护；Full Launch 可能延后 |
| Final Commitment | follow-up experiment | 保留学习闭环 | 短期结果不确定 | additional_evaluation / next experiment 被写入 state |

## Normalized collaboration vocabulary

允许的 normalized action/event：`information_requested`、`clarification_asked`、`evidence_shared`、`constraint_acknowledged`、`disagreement_stated`、`proposal_made`、`proposal_revised`、`uncertainty_explicit`、`ownership_taken`、`gate_condition_set`、`shared_state_changed`、`rollback_condition_added`。

用户自由回答先归一化为 `claims / evidenceReferenced / constraintsAcknowledged / tradeoffs / uncertainties / requestedActions / proposedAction`，再进行确定性校验。语言流畅度、礼貌程度和 option index 不产生额外 credit。

## Collaboration Evidence Contract

Collaboration Evidence 的 claim target 是“本次 Launch War Room 中可回放的协作事件”，不是 personality、soft-skill score 或稳定 trait。一个事件可同时支持 role hard skill 与 collaboration，但必须使用不同 requirementId、不同 rubric 与不同 claim wording，禁止 additive credit。Simulation trace、其他角色的 private brief 内容和系统自动 consequence 不得伪装成参与者行为。

## Concrete conflict tensions

| Tension | Supplied evidence | No-free-lunch consequence | 可行解决路径 |
|---|---|---|---|
| Deadline vs Quality | 校园活动 48 小时内收窄；一组 high-risk badcase 尚未复测 | 赶窗口留下质量未知；完整复测可能错过 campaign reach | limited/staged + additional evaluation，或 hold 并牺牲窗口 |
| Scope vs Technical Performance | 核心路径 p95 2.5 秒；跨文件综合约 3.8 秒；峰值容量有限 | 全 scope 影响延迟/容量；缩 scope 牺牲价值覆盖 | narrow scope、routing、fallback、limited cohort |
| UX Safety vs Interaction Cost | partial checklist 易被误认为完成；整页 retry 会丢已确认项目 | 增加 disclosure/recovery/handoff 提升成本；省略则增加误解/重工 | staged path、明确 partial、局部恢复、人工 handoff |

不同优先级会导致不同合法路径；不存在同时最大化 deadline、quality、scope、latency 与 UX clarity 的免费方案。

## Final Commitment Artifact

```yaml
rolloutMode: full | staged | limited | hold | additional_evaluation
launchScope: core_activation | core_plus_quality | deferred
targetCohort: personal | enterprise | mixed | limited_cohort
primaryOwner: role or named owner
actionOwners: [domain owners]
successMetric: one primary outcome
monitorMetric: one or more monitor signals
keyGuardrail: quality / UX / ops guardrail
rollbackCondition: executable threshold or trigger
unresolvedRisk: one bounded risk
followUpExperiment: next evidence-producing action
```

`primaryOwner` 不默认等于 Product；`actionOwners` 至少覆盖实际改变状态的 domain roles。

## Role parity invariant

每个角色在四个 Beat 都能 reveal information、采取 action、改变至少一个 shared-state 字段、产生 consequence，并影响下一 Beat。不存在 Product 单方面 Full Launch override。
