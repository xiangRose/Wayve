# Future / Post-MVP — AI UI Design Role Work Sample

状态：FUTURE / POST-MVP — FROZEN REFERENCE ARCHITECTURE（NOT MVP）

## 工作样本契约

名称：AI Document-to-Checklist Flow — Partial Result and Recovery。

用户在一个文档转清单的 AI 产品中，设计处理进度、部分结果、不确定信息和失败恢复流程。体验使用 persistent state-flow workbench、固定 simulated user、deterministic path，并允许最多一次 retain/revise rerun。时长由 experience layer 决定，不是硬性 SLA。

## 语义流程与后果

`scenario → first_judgment → evidence_gathering → twist → reconsideration → final_decision`

用户编辑状态和转移，而不是视觉样式。系统模拟用户如何面对 partial result、uncertainty、failure 和 recovery，并展示 confusion、trust、state loss / preservation 等 consequence。

## Participant Behavior 与 Simulation Trace

用户操作产生 Participant Behavior Events。simulated user 的行为只属于 consequence trace，不是用户行为，不得单独进入 Evidence。

## Evidence 与 Report 边界

Evidence 只能支持本次 supplied flow 中观察到的交互逻辑、AI 状态、信息层级、恢复和修订。不得推断视觉设计能力、长期 UX 能力、人格或职业适配度。
