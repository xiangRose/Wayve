# Hackathon Execution Plan

Status: Active Execution Snapshot
Source of Truth: Feishu
Repository Snapshot Updated: 2026-08-28

This document exclusively owns repository alignment gaps, migration order, P0 acceptance, and follow-up scope. It records implementation work; it does not redefine product behavior.

## Frozen Execution Decisions

- `ai_product` is the single Hackathon P0 minimum viable vertical slice; “Hero” is historical implementation shorthand only.
- Five roles are equal browsing choices and direct profile-free entry is supported; recommendation remains optional navigation.
- Canonical job IDs are `ai_product`, `ai_ops`, `ai_data_eval`, `ai_app_dev`, and `ai_ui_design`.
- Recommendation is navigation-only.
- Evidence is replayable and bounded.
- Analysis Report has no unified Job Fit Score.
- Growth Track ends at Pending in P0.
- Direction Update is user-confirmed and requires real new evidence.
- The `ai_ui_design` scene trial is frozen and canonical; production integration remains follow-up work outside the current P0.
- This product-doc change authorizes no backend, API, seed, frontend, or production-data work.

## Repository Alignment Audit

| Requirement | Repository snapshot | Gap / action | Priority |
| --- | --- | --- | --- |
| Five canonical jobs | Seed jobs still contain `ai_pm`, `ai_ux`, `ai_consultant`, `ai_operator`, and `ai_researcher`. | Migrate safe IDs; review consultant/researcher content before reuse. | P0 |
| `ai_product` P0 vertical slice | Closest template is `ai_pm.career_changer.json`, estimated at 7 minutes. | Content-migrate ID and tighten to 3–5 minutes. | P0 |
| Reusable TaskTemplate | Backend stores JSON by job and scaffold. | Preserve container; align schema and add structured event fields. | P0 |
| Replayable Evidence | Step submissions accept answer and events; demo report uses `stepSource`. | Map to canonical `sourceStep`, source Event IDs, replay, supports, and limits. | P0 |
| Separated report sources | Existing report separates resume, task summary, and interest signals but uses radar-first naming. | Align UI/API language with Current Evidence Profile and canonical reading order. | P0 |
| Navigation recommendation | Prompt boundary exists; fallback IDs and OpenAPI examples remain legacy. | Migrate IDs and add internal `navigationScore` in a later implementation change. | P0 |
| Demo consistency | Demo session uses legacy IDs and `resumeRadar`. | Migrate without fabricating history or trends. | P0 |
| Remaining role trials | Complete templates do not exist for `ai_ops`, `ai_data_eval`, and `ai_app_dev`. | Add after Hero loop stabilizes. | Follow-up |
| `ai_ui_design` scene trial | Frozen canonical Document-to-Checklist Trial exists; production support is unverified. | Integrate only in a separately approved implementation change after the `ai_product` loop. | Follow-up |
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
2. Complete the 3–5 minute `ai_product` Hero loop.
3. Align structured Behavior Event and Evidence replay contracts.
4. Align Analysis Report fields and UI with source separation and canonical reading order.
5. Update demo fallback without fake history.
6. Add remaining role content after the shared container is stable.
7. Evaluate production integration of the candidate `ai_ui_design` scene trial separately.

## P0 Acceptance

- Active Product Docs have one owner per responsibility.
- Five canonical IDs are used for active roles.
- The `ai_product` vertical slice is complete end to end: entry, preview, scenario, action, consequence, revision, deliverable, Interest, Behavior, bounded Evidence, Replay, seven-part report compatibility, and Next Mission.
- Other four roles have frozen specs, P0 depth allocation, and implementation backlogs, but no fake partial production experiences.
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

- Complete canonical templates for non-Hero roles.
- Integrate and validate the `ai_ui_design` scene trial after the Hero loop.
- Multi-cycle Growth Track using only real experiments.
- Career Evidence Review across actual repeated evidence.
- Evidence map from Role -> Requirement -> Evidence -> Unknown -> Mission.
- Mission revision based on reviewed New Evidence.
- Mission-specific reminders, not general life logging.
