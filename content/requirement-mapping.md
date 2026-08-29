# Canonical Requirement Mapping — Batch 1A

canonical IDs 是唯一 active requirement system；legacy IDs / events 仅作为 migration reference。

## Mapping rules

- 一个 legacy event 可支持多个 potential claims，但生成 Evidence 时只能绑定一个 canonical requirement。
- legacy requirement 不得与 canonical requirement 并列评分。
- 没有合理 opportunity 时为 `not_observed`，不是 0。

## Canonical mappings

| role | canonical requirementId | legacy requirement mapping | legacy event mapping | usable observation opportunity | migration note |
|---|---|---|---|---|---|
| ai_product | user_insight | `evidence_judgment`（部分） | `evidence_compared`, `evidence_opened` | 将 feedback/funnel/retention 与用户问题联系 | 拆分，不等同 |
| ai_product | problem_definition | 无直接 ID | `priority_committed`, `short_reason_submitted` | 说明 activation break 的范围与用户影响 | 新增 canonical claim |
| ai_product | product_judgment | `constrained_prioritization`（部分） | `priority_committed`, `reconsideration_recorded` | 结合证据、后果、约束形成产品判断 | 扩展旧 ID |
| ai_product | ai_feasibility | `testable_next_step`（弱相关） | `evidence_compared`, `tradeoff_uncertainty_submitted` | 使用 effort/dependency/model constraint | 不得将 metric 等同 feasibility |
| ai_product | prioritization_tradeoff | `constrained_prioritization` | `option_deferred`, `tradeoff_uncertainty_submitted` | 一槽位下明确延期、代价与 guardrail | 新增 trade-off field |
| ai_product | cross_team_push | `decision_communication`（不等同） | `evidence_categories_selected`, `final_decision_submitted` | 请求并转交 Ops/Dev 信息与行动 | 需独立 handoff evidence |
| ai_ui_design | user_understanding | `supplied_state_and_path_inspection`（部分） | `simulated_friction_inspected` | 将 user goal/action/friction 联系 | 查看不等于理解 |
| ai_ui_design | interaction_logic | `bounded_interaction_flow_decision` | `flow_transition_inspected`, `final_interaction_flow_submitted` | 状态/转移逻辑与动作边界 | 形成 before/after 链 |
| ai_ui_design | information_architecture | `waiting_and_partial_flow_decision`（部分） | `partial_result_treatment_changed` | ready/uncertain/failed 信息层级 | 扩展状态分组 |
| ai_ui_design | ai_state_design | `uncertainty_and_action_disclosure`（部分） | `uncertainty_path_selected`, `recovery_path_selected` | waiting/partial/uncertain/failed/recovery 全链 | 不局限 uncertainty |
| ai_ui_design | interface_expression | 无直接 ID | `simulated_friction_inspected` | 状态文字、操作可见性、恢复 affordance | 非视觉审美 |
| ai_ui_design | design_iteration | `simulated_path_interpretation_and_revision` | `interaction_flow_reconsidered` | 根据路径后果 retain/revise | 保留 before/after |
| ai_ops | data_insight | `lifecycle_diagnosis`（部分） | `diagnosis_updated`, `lifecycle_signals_compared` | 从 dashboard 识别断点与分群差异 | 扩展 data chain |
| ai_ops | user_segmentation | `operational_targeting`（部分） | `target_cohort_selected` | 用行为/来源/价值定义 cohort | 与 intervention 分离 |
| ai_ops | operational_attribution | `result_interpretation`（部分） | `result_drivers_inspected`, `result_interpretation_submitted` | 比较 competing explanations 与 baseline | 不等同 modeled result |
| ai_ops | strategy_design | `operational_targeting`（部分） | `intervention_selected`, `trigger_timing_configured`, `channel_selected` | 组合 intervention、timing、channel | 拆出策略对象 |
| ai_ops | resource_execution | 无直接 ID | `trigger_timing_configured`, `final_strategy_submitted` | 在 campaign window 与 resource slot 下执行选择 | 新增绑定约束 |
| ai_ops | feedback_iteration | `strategy_revision`（部分） | `strategy_rerun`, `strategy_reconsidered` | 根据 modeled/user feedback 修改策略 | 必须出现反馈关系 |
| ai_data_eval | quality_sensitivity | `case_and_pattern_inspection`（部分） | `cases_compared`, `failure_patterns_compared` | 识别 severity 与 high-risk consequence | inspection alone 不足 |
| ai_data_eval | rule_boundary | `standard_configuration` | `severity_anchors_set`, `routing_rule_changed` | 将边界连接到 pass/review/block | anchors alone 不足 |
| ai_data_eval | badcase_attribution | `case_and_pattern_inspection`（部分） | `failure_patterns_compared` | badcase → error taxonomy → rule | 新增归因链 |
| ai_data_eval | data_insight | `retest_interpretation`（部分） | `retest_summary_inspected` | sampling coverage、miss pool、review load | 扩展 distribution reasoning |
| ai_data_eval | evaluation_design | `limited_evaluation_audit_allocation` | `evaluation_audit_budget_allocated`, `inspection_focus_changed` | eval set、sampling、coverage、audit allocation | 保留 six-slot mechanic |
| ai_data_eval | quality_decision | `bounded_rollout_judgment`（部分） | `gate_reconsidered`, `rollout_judgment_selected` | gate/counterexample/monitoring 形成 release state | 不等同 binary answer |
| ai_app_dev | system_understanding | `supplied_failure_and_trace_inspection`（部分） | `workspace_file_inspected`, `source_trace_inspected` | 连接 request path、source/context、runtime | 查看 trace 不足 |
| ai_app_dev | debug_localization | `bounded_application_configuration`（部分） | `initial_failure_opened`, `application_configuration_changed` | hypothesis → evidence → narrowed scope | 配置不等于定位 |
| ai_app_dev | ai_technical_understanding | `bounded_application_configuration`（部分） | `source_trace_inspected`, `result_drivers_inspected` | 理解 RAG/source/fallback 对结果的影响 | supplied behavior only |
| ai_app_dev | engineering_solution | `representative_consequence_testing`（部分） | `representative_suite_run`, `application_results_compared` | 方案选择与 observed trade-off 绑定 | 测试不等于设计 |
| ai_app_dev | performance_cost_tradeoff | `regression_response_and_revision`（部分） | `regression_inspected`, `application_configuration_reconsidered` | 明确 latency/cost/quality 至少两者取舍 | 需显式 trade-off |
| ai_app_dev | delivery_adaptation | `bounded_delivery_uncertainty`（部分） | `delivery_risk_next_test_submitted`, `application_delivery_submitted` | latest regression、fallback、delivery risk | uncertainty note alone 不足 |

## Conflicts

旧系统的场景 ID 与 canonical catalog 不同；本文件不修改 frontend/backend，不批量重命名 event。Content layer 只输出 canonical requirement claim，并保留 legacy provenance。
