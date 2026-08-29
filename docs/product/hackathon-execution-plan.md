# Hackathon Execution Plan

Status: Active Execution Snapshot (current P0 revision 2026-08-29)
Source of Truth: Feishu
Repository Snapshot Updated: 2026-08-28

This document exclusively owns repository alignment gaps, migration order, P0 acceptance, and follow-up scope. It records implementation work; it does not redefine product behavior.

## Frozen Execution Decisions

- Hackathon P0 is five playable complete Mini Career Trials: `ai_product`, `ai_ops`, `ai_data_eval`, `ai_app_dev`, and `ai_ui_design`. Depth may differ; `ai_product` is the Showcase, not a privileged career option.
- Five roles are equal browsing choices and direct profile-free entry is supported; recommendation remains optional navigation.
- Canonical job IDs are `ai_product`, `ai_ops`, `ai_data_eval`, `ai_app_dev`, and `ai_ui_design`.
- Recommendation is navigation-only.
- Evidence is replayable and bounded.
- Analysis Report has no unified Job Fit Score.
- Growth Track ends at Pending in P0.
- Direction Update is user-confirmed and requires real new evidence.
- The `ai_ui_design` scene trial is FROZEN / PASS and canonical: AI Document-to-Checklist Flow - Partial Result and Recovery.
- Role mechanics are distinct: product priority/evidence/revision; ops lifecycle diagnosis/intervention/revision; data-eval case inspection/quality gate/retest/rollout revision; app-dev runtime/source configuration/fixed request suite/regression revision; UI-design state/transition editing/uncertainty/recovery/deterministic simulation/revision.
- The Task Content Library owns concrete fixtures and deterministic consequence/replay content; teammate tiers (`2/3/4/5`) are authoring calibration only.
- Product Docs own WHAT/WHY and semantic boundaries; implementation technical specs own implementation/data organization; Frozen Role Trial Specs own mechanics; Task Content Library owns authored content. No layer silently overrides another outside its domain.
- This product-doc change authorizes no backend, API, seed, frontend, or production-data work.

## Repository Alignment Audit

| Requirement | Repository snapshot | Gap / action | Priority |
| --- | --- | --- | --- |
| Five canonical jobs | Seed jobs still contain `ai_pm`, `ai_ux`, `ai_consultant`, `ai_operator`, and `ai_researcher`. | Migrate safe IDs; review consultant/researcher content before reuse. | P0 |
| `ai_product` Showcase Trial | Closest template is `ai_pm.career_changer.json`, estimated at 7 minutes. | Content-migrate ID and tighten to 3–5 minutes. | P0 |
| Reusable TaskTemplate | Backend stores JSON by job and scaffold. | Preserve container; align schema and add structured event fields. | P0 |
| Replayable Evidence | Step submissions accept answer and events; demo report uses `stepSource`. | Map to canonical `sourceStep`, source Event IDs, replay, supports, and limits. | P0 |
| Separated report sources | Existing report separates resume, task summary, and interest signals but uses radar-first naming. | Align UI/API language with Current Evidence Profile and canonical reading order. | P0 |
| Navigation recommendation | Prompt boundary exists; fallback IDs and OpenAPI examples remain legacy. | Migrate IDs and add internal `navigationScore` in a later implementation change. | P0 |
| Demo consistency | Demo session uses legacy IDs and `resumeRadar`. | Migrate without fabricating history or trends. | P0 |
| Remaining role trials | Complete templates do not exist for `ai_ops`, `ai_data_eval`, and `ai_app_dev`. | Author and implement playable complete Mini Career Trials; depth may differ. | P0 |
| `ai_ui_design` scene trial | Frozen/pass canonical Document-to-Checklist Trial exists. | Implement as the fifth playable complete Mini Career Trial. | P0 |
| Frontend role config | No production frontend is present in this repository snapshot. | Coordinate with the owning repository. | External |

## Legacy Locations to Migrate Later

- `src/main/resources/seed/jobs.json`
- `src/main/resources/seed/task-templates/ai_pm.career_changer.json`
- `src/main/resources/seed/task-templates/ai_ux.career_changer.json`
- `src/main/resources/seed/demo-session.json`
- `src/main/resources/AI/fallbacks/job-recommendation.json`
- `docs/openapi.yaml`
- `src/main/java/com/Grassroot/JobSearch/ai/SeedDataLoader.java`

This list records gaps only. No listed file is modified by the product-doc restructuring.

## Recommended Implementation Sequence

1. Migrate safe canonical role IDs and review legacy content.
2. Complete the 3–5 minute `ai_product` Showcase loop.
3. Align structured Behavior Event and Evidence replay contracts.
4. Align Analysis Report fields and UI with source separation and canonical reading order.
5. Update demo fallback without fake history.
6. Add playable complete content for `ai_ops`, `ai_data_eval`, `ai_app_dev`, and `ai_ui_design` using the shared container and their distinct mechanics.

## P0 Acceptance

- Active Product Docs have one owner per responsibility.
- Five canonical IDs are used for active roles.
- All five Mini Career Trials are playable end to end: entry, preview, scenario, action, consequence, revision, deliverable, Interest, Behavior, bounded Evidence, Replay, seven-part report compatibility, and Next Mission. Depth may differ; a preview without playable completion does not satisfy P0.
- `ai_product` is the Showcase Trial, not a privileged or sole real Trial.
- Recommendations use `navigationScore` and never evaluate ability.
- User may choose non-recommended roles.
- The `ai_product` trial is 3–5 minutes and maps to six internal `sourceStep` values.
- Behavior Events and Evidence support replay.
- Background Evidence, Task Evidence, and Interest Feedback remain separate.
- Analysis Report follows the one canonical reading order.
- Growth Track shows `Trial Completed -> Evidence Captured -> Analysis Report -> Next Mission -> Pending`.
- No New Evidence or Direction Update exists without a real later experiment; Direction Update is user-confirmed.
- No implementation file changes occur in this documentation change.

## Follow-up

- Expand authored depth and implementation quality for all five Trials after the shared container is stable.

## Current Status Dimensions

Functional Engineering: PARTIAL (implementation validation remains tracked separately). Product Experience Fidelity: PARTIAL (five-role P0 contract represented; final Demo validation remains implementation work). Five-role Content Readiness: P0 scope is five playable Trials; depth may differ. Five-role Implementation: PARTIAL. Requirements Compliance: PASS for this revision. Repository Integration: PARTIAL. Public Demo: PARTIAL. Backend Production Integration: BLOCKED on Java 17.
- Multi-cycle Growth Track using only real experiments.
- Career Evidence Review across actual repeated evidence.
- Evidence map from Role -> Requirement -> Evidence -> Unknown -> Mission.
- Mission revision based on reviewed New Evidence.
- Mission-specific reminders, not general life logging.
