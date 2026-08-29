# Launch War Room — Five Private Briefs

| 角色 | Exclusive information | Unique constraint | Action agency | Information others need | Shared-state fields owned | Final trade-off |
|---|---|---|---|---|---|---|
| Product | activation target、scope、business priority | 一个 launch slice | scope / success criterion / defer | 向 Dev 说明 value/deadline；向 Ops 说明 cohort | launchScope、success criterion、rollback proposal | learning speed vs scope certainty |
| Operations | cohort funnel、campaign calendar、baseline | window 与资源固定 | cohort / intervention / timing / guardrail | 向 Product 提供 segment；向 UI 提供 user interpretation | targetCohort、campaignWindow、reach、guardrail | reach vs fatigue/retention |
| Data Evaluation | badcase clusters、boundary uncertainty、coverage gap | audit slots 与 gate budget | sampling / gate / retest / release posture | 向 Product/Dev 提供 quality risk/confidence | qualityGate、coverage、release posture | coverage vs review load |
| Application Development | runtime path、p95、cost、capacity、fallback | latency ceiling 与 capacity | routing / source scope / fallback / rollout mode | 向 Product 说明 feasible scope；向 Eval 说明 instrumentation | technicalFeasibility、rolloutMode、fallback | quality vs latency/cost |
| UI Design | loading/partial/uncertainty/recovery evidence | unresolved 不得表现 confirmed | state disclosure / recovery / handoff | 向 Product/Ops 说明 user comprehension/interruption | userStateAcceptance、state contract、handoff | clarity/control vs interaction load |

## Beat agency detail

| 角色 | Beat 1 | Beat 2 | Beat 3 | Beat 4 |
|---|---|---|---|---|
| Product | reveal target/constraint | propose scope | integrate constraints, cannot override blocked authority | commit scope + metric |
| Operations | reveal cohort/window | propose target/intervention | change reach/timing/guardrail | commit campaign owner + monitor |
| Data Evaluation | reveal risk/coverage | set gate posture | block Full Launch or require staged/additional eval | commit gate + retest/rollback |
| Application Development | reveal bottleneck | propose routing/rollout | change feasibility via fallback/degraded mode | commit technical owner + p95 gate |
| UI Design | reveal user-state risk | propose state contract | block Full Launch until disclosure/recovery/handoff acceptable | commit user-facing acceptance + usability monitor |

所有角色都能改变 shared state；UI/Eval 不再是 supporting NPC。
