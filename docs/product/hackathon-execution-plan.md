# Engineering Snapshot 与迁移说明

状态：SUPPORTING ENGINEERING SNAPSHOT — subordinate to current Product SOT
更新时间：2026-08-29

本文件记录实现缺口和迁移顺序，不重新定义当前产品语义。Hackathon P0 的 3–5 分钟是历史 scope context，不是当前正式产品的时长 SLA。

## 当前缺口

- seed 和 OpenAPI 仍含 legacy role IDs；
- backend 尚未完整持久化 canonical Behavior Events、Evidence、Collaboration Evidence、Reflection 和 Hard Skill Assessment；
- report 代码仍暴露 legacy `resumeRadar` 与 `taskEvidenceSummary`；
- 当前模板仍是 legacy snapshot，需要从 Frozen Specs 和 Task Content Library promotion；
- 《评价体系雷达图.docx》已完成 source validation，相关 requirement 与 roleImportance 已进入 canonical catalog。

## 迁移顺序

1. promotion canonical role、requirement、event 和 content IDs；
2. Future only：建立 registry-driven Hard Skill Work Sample 与 deterministic consequence；MVP 使用 Scenario Quiz；
3. 持久化 immutable Behavior Events 和 Replay links；
4. 增加分离的 Evidence、Hard Skill Assessment、Collaboration Evidence、Reflection 和 Working Portrait；
5. 将 report 迁移到当前信息架构；
6. 在 shared state engine 稳定后加入 Nova V3 / AI Product Launch Review Collaboration Sprint；
7. 清理 active API / seed 中的 legacy names，同时保留 migration metadata。

本文件不授权 backend、frontend、schema migration、seed、OpenAPI、prompt deployment 或 remote Git 操作。
