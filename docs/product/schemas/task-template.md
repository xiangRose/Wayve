# TaskTemplate / Future Hard Skill Work Sample Schema

状态：CURRENT PRODUCT SOT — APPROVED FOR FEISHU SOT

TaskTemplate 是 Role Work Sample 的稳定容器。它保存 authored content、deterministic state 和 consequence references，不嵌入最终评估结论。

## 必要结构

```json
{
  "type": "TaskTemplate",
  "jobId": "ai_product",
  "experienceMode": "structured_decision",
  "scenario": {},
  "semanticFlow": ["scenario","first_judgment","evidence_gathering","twist","reconsideration","final_decision"],
  "steps": [],
  "requirements": [],
  "consequenceRegistry": [],
  "revisionRules": {},
  "eventSchema": {},
  "replayMap": {},
  "beginnerScaffolding": {},
  "notObservedRules": {}
}
```

## 契约

每个 step 声明 work object、可用信息、用户 action、emitted events 和 requirement references。相同 scenario version、state 和 action sequence 必须产生相同 consequence。Simulation trace 与 participant Behavior Event 分开保存。六阶段是语义主轴，不要求六个可见页面。

Task Content Library 负责场景文案、evidence cards、options、fixtures、consequence drivers、replay snapshots、difficulty 和 accessibility wording；不得修改 claim boundaries 或 scoring semantics。
