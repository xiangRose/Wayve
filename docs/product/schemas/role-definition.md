# RoleDefinition and Requirement Catalog

Status: SOURCE VALIDATED — CURRENT PRODUCT SOT
Source of Truth: Feishu, projected locally
Source: 《评价体系雷达图.docx》, validated 2026-08-29

A RoleDefinition contains the complete standard requirement catalog for one role. It does not claim that every session observes every requirement.

## Calibration Rules

The source labels and importance values are retained by default. `roleImportance` is the relative importance of a requirement in the role standard profile. It is not a passing score, user target, user performance, capability threshold, or Career Fit score. Whether it participates in any weighted readiness aggregation remains an open Assessment / UX decision.

A session has separate observation coverage. `not_observed` has no performance level or score.

## REQUIREMENT CALIBRATION MATRIX

| role | originalLabel | proposedLabel | decision | rationale / current interpretation | overlap risk and boundary |
|---|---|---|---|---|---|
| AI Product | 用户洞察 | 用户洞察 | keep | Understands the real user problem from feedback, need signals, and pain points. | Asking users/teammates is Collaboration only unless it changes product problem framing. |
| AI Product | 问题定义 | 问题定义 | keep | Converts an ambiguous symptom into a solvable Problem Statement and causal boundary. | Clarification behavior is not itself problem-definition evidence. |
| AI Product | 产品判断 | 产品判断 | keep | Integrates user value, business value, product direction, and constraints into a decision. | A teammate question can support this only when the constraint is integrated into the decision. |
| AI Product | AI 可行性理解 | AI 可行性理解 | keep | Understands AI capability boundaries, technical cost, and implementation limits. | Receiving information is Collaboration; applying AI limits is Hard Skill. |
| AI Product | 优先级与取舍 | 优先级与取舍 | keep | Chooses what to do first under limited resources and states what is deferred. | Validation is part of the evidence pattern, not a label rename. |
| AI Product | 跨团队推动 | 跨团队推动 | keep | Obtains role input and moves a product decision toward shared execution. | Professional claim: decision/handoff quality. Collaboration claim: how the person asks, shares, handles disagreement, and builds alignment. |
| AI UI Design | 用户理解 | 用户理解 | keep | Detects user comprehension and operation friction from the supplied user path. | No stable empathy or personality claim. |
| AI UI Design | 交互逻辑 | 交互逻辑 | keep | Makes task flow continuous, understandable, and actionable. | Collaboration concerns how feedback is handled, not whether the flow logic works. |
| AI UI Design | 信息架构 | 信息架构 | keep | Organizes functions, hierarchy, and page structure around user goals. | Team alignment is separate Collaboration Evidence. |
| AI UI Design | AI 状态设计 | AI 状态设计 | keep | Designs truthful processing, partial, error, uncertainty, and recovery states. | Explaining a constraint is not sufficient without state/transition work. |
| AI UI Design | 界面表达 | 界面表达 | keep | Turns complex AI capability into clear, operable interface expression. | Does not imply visual polish or portfolio-level visual ability. |
| AI UI Design | 设计迭代 | 设计迭代 | keep | Uses observed feedback or consequence to modify the flow under constraints. | Professional claim: quality of the revised flow. Collaboration claim: response to review and disagreement. |
| AI Operations | 数据洞察 | 数据洞察 | keep | Finds the real issue in metrics, funnels, and cohorts. | Information seeking alone is not insight. |
| AI Operations | 用户分层 | 用户分层 | keep | Selects actionable user groups and explains behavioral differences. | Sharing rationale is Collaboration only when the claim is about interaction. |
| AI Operations | 运营归因 | 运营归因 | keep | Connects growth, usage, and retention changes to plausible causes and boundaries. | “增长/留存归因” was too narrow; original label is retained. |
| AI Operations | 策略设计 | 策略设计 | keep | Designs an operational action with audience, lever, timing, and guardrail. | Agreement-seeking is Collaboration, not strategy quality. |
| AI Operations | 资源与执行 | 资源与执行 | keep | Allocates limited time, budget, and resources to the key intervention. | “约束执行” would lose the source meaning of resource allocation. |
| AI Operations | 反馈迭代 | 反馈迭代 | keep | Adjusts strategy after new data or modeled results. | Professional claim: strategy update. Collaboration claim: response to team feedback. |
| AI Data Evaluation | 质量敏感度 | 质量敏感度 | keep | Detects subtle but consequential output problems and severity. | “质量风险” would omit perceptual sensitivity; escalation is Collaboration. |
| AI Data Evaluation | 规则与边界判断 | 规则与边界判断 | keep | Applies standards to ambiguous and edge cases. | Policy discussion is Collaboration; case judgment is Hard Skill. |
| AI Data Evaluation | Badcase 归因 | Badcase 归因 | keep | Identifies error type and plausible cause pattern. | Requesting domain input is not attribution by itself. |
| AI Data Evaluation | 数据洞察 | 数据洞察 | keep | Finds hidden anomalies behind averages through slices and metric conflicts. | Coverage/sampling is an observable sub-context, not a replacement label. |
| AI Data Evaluation | 评测设计 | 评测设计 | keep | Decides what to test and how to allocate evaluation resources. | Team alignment is separate. |
| AI Data Evaluation | 质量决策 | 质量决策 | keep | Uses evaluation evidence to decide whether quality is acceptable and what follows. | “Release Gate 决策” is a valid work object but too narrow as canonical label. |
| AI Application Development | 系统理解 | 系统理解 | keep | Understands modules, data flow, and their relationship to result behavior. | Asking for context is Collaboration, not system understanding. |
| AI Application Development | Debug 定位 | Debug 定位 | keep | Traces an anomaly toward a plausible root cause. | Escalation behavior is Collaboration. |
| AI Application Development | AI 技术理解 | AI 技术理解 | keep | Understands model, RAG, context, source, and fallback mechanisms. | “Runtime 理解” would narrow the original technical scope. |
| AI Application Development | 工程方案设计 | 工程方案设计 | keep | Converts business intent into a viable implementation path. | Handoff quality is Collaboration only when the claim target is teamwork. |
| AI Application Development | 性能与成本取舍 | 性能与成本取舍 | keep | Balances effect, latency, cost, and stability. | Capacity negotiation is Collaboration, not the trade-off itself. |
| AI Application Development | 交付与适应 | 交付与适应 | keep | Responds to new constraints and still produces a bounded deliverable. | “稳定降级交付” would overfit the current App Dev fixture. |

## Canonical Catalog

| role | requirement | roleImportance |
|---|---|---:|
| AI Product | 用户洞察 | 9 |
| AI Product | 问题定义 | 9 |
| AI Product | 产品判断 | 10 |
| AI Product | AI 可行性理解 | 7 |
| AI Product | 优先级与取舍 | 10 |
| AI Product | 跨团队推动 | 9 |
| AI UI Design | 用户理解 | 10 |
| AI UI Design | 交互逻辑 | 10 |
| AI UI Design | 信息架构 | 9 |
| AI UI Design | AI 状态设计 | 9 |
| AI UI Design | 界面表达 | 8 |
| AI UI Design | 设计迭代 | 8 |
| AI Operations | 数据洞察 | 9 |
| AI Operations | 用户分层 | 8 |
| AI Operations | 运营归因 | 9 |
| AI Operations | 策略设计 | 9 |
| AI Operations | 资源与执行 | 10 |
| AI Operations | 反馈迭代 | 10 |
| AI Data Evaluation | 质量敏感度 | 10 |
| AI Data Evaluation | 规则与边界判断 | 9 |
| AI Data Evaluation | Badcase 归因 | 9 |
| AI Data Evaluation | 数据洞察 | 9 |
| AI Data Evaluation | 评测设计 | 10 |
| AI Data Evaluation | 质量决策 | 8 |
| AI Application Development | 系统理解 | 9 |
| AI Application Development | Debug 定位 | 10 |
| AI Application Development | AI 技术理解 | 10 |
| AI Application Development | 工程方案设计 | 9 |
| AI Application Development | 性能与成本取舍 | 9 |
| AI Application Development | 交付与适应 | 8 |

## Session Observation Contract

A Work Sample should intentionally observe the requirements its scenario can support, normally about four to six. The full catalog remains intact. Each session records `observed`, `partially_observed`, or `not_observed`; missing coverage is not a low result. A valid opportunity with materially off-task or non-demonstrating performance may be assessed at level `0`; `not_observed` never receives a performance level.
