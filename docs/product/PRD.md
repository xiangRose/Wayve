# WAYVE Hackathon Product Requirements

Status: Hackathon P0 Frozen
Source of Truth: Feishu
Repository Snapshot Updated: 2026-08-28

This GitHub documentation is the current engineering execution snapshot for the WAYVE hackathon repository. Product discussions, meeting notes, and decision history remain governed by Feishu as the source of truth. If this repository snapshot conflicts with Feishu, Feishu wins and the repository gap must be recorded before implementation changes are made.

## Product Definition

WAYVE is an AI career trial product. It helps users explore AI-era job directions by trying realistic work scenarios, recording observable behavior as evidence, and turning that evidence into a bounded analysis report and next mission.

The frozen core chain is:

Job Definition -> Trial Scenario -> User Behavior -> Evidence -> Analysis Report -> Next Mission

Recommendations only help users choose where to start. Evaluation only uses evidence that can be traced back to real user actions in the trial or user-provided background. The product must avoid deterministic career judgment.

## Hackathon Goal

P0 must demonstrate one complete, trustworthy exploration loop:

1. A user creates or enters an exploration session.
2. The product recommends up to three roles for trial navigation without making ability claims.
3. The user can freely choose a role, including a non-recommended role.
4. The user completes a 3-5 minute career trial.
5. The system records behavior events and converts them into replayable evidence.
6. The report separates background evidence, task evidence, and interest feedback.
7. The report proposes one next mission without producing a unified job fit score.

## Target Audience

P0 targets users who want to explore AI-related career directions but do not yet have enough direct work evidence to choose confidently. This includes students, early-career users, career changers, and users with adjacent project experience.

The product is not a hiring tool, screening tool, psychometric test, or permanent ability profile.

## P0 Scope

- Five canonical AI job definitions.
- Role matching for trial navigation using `navigationScore`.
- One complete Hero Role career trial for `ai_product`.
- A reusable task container that can host all canonical roles.
- Behavior event capture from trial interactions.
- Evidence objects that link to `sourceStep`.
- Analysis report that separates Resume / Background Evidence, Task Evidence, and Interest Feedback.
- Current Evidence Profile, if radar-style UI is retained.
- Next Mission generation based on observed evidence gaps and user target.
- Demo mode using seeded data and fallbacks when AI generation is unavailable.

## Out of Scope

- A unified Job Fit Score.
- Hiring, ranking, admission, or employment decisions.
- Permanent personality or ability labeling.
- Long-form interviews, knowledge exams, or self-rated ability questionnaires.
- New first-level product modules outside the frozen chain.
- Mechanical renaming of `ai_consultant` or `ai_researcher` into a new canonical role.
- Large-scale code migration in this documentation PR.

## Canonical Roles

The five frozen canonical `jobId` values are:

| jobId | Display Name | P0 Meaning |
| --- | --- | --- |
| `ai_product` | AI Product | Defines AI product problems, priorities, evidence needs, and trade-offs under constraints. |
| `ai_ops` | AI Operations | Designs activation, retention, feedback, and experiment loops for AI products. |
| `ai_data_eval` | AI Data Evaluation | Evaluates AI outputs, data quality, labeling criteria, and evidence consistency. |
| `ai_app_dev` | AI Application Development | Builds AI application flows by connecting product intent, model behavior, and implementation constraints. |
| `ai_ui_design` | AI UI Design | Turns AI capabilities and uncertainty into understandable, usable interfaces and feedback states. |

Legacy IDs must not coexist long-term with canonical IDs:

| Legacy ID | Migration Rule |
| --- | --- |
| `ai_pm` | Safe semantic migration to `ai_product`. |
| `ai_operator` | Safe semantic migration to `ai_ops`. |
| `ai_ux` | Safe semantic migration to `ai_ui_design`. |
| `ai_consultant` | No mechanical rename. Reuse valuable scenario content only after content-level review. |
| `ai_researcher` | No mechanical rename. Reuse valuable scenario content only after content-level review. |

## Hero Role

The P0 Hero Role is `ai_product`.

This is an execution convenience, not a product priority statement. The existing backend data for `ai_pm` is closest to the frozen loop:

TaskTemplate -> Behavior Event -> Evidence -> AnalysisReport

The `ai_pm` task content may be migrated at the content level into `ai_product` after review.

## User Flow

1. User enters session and may provide profile or background information.
2. Product calculates role navigation recommendations.
3. Product presents Top 3 role starting points and explains why each may be worth trying.
4. User accepts a recommendation, rejects recommendations, or chooses any other canonical role.
5. User completes a role-specific trial scenario in the shared task container.
6. Task interactions produce Behavior Events.
7. Behavior Events are converted into Evidence with clear scope and limitations.
8. User provides Interest Feedback after the task.
9. Analysis Report presents separated evidence and unknowns.
10. User selects or confirms a target direction.
11. Product proposes a Next Mission to gather missing evidence.

## Career Trial

P0 trial design must feel like work, not a test. It should be completed in 3-5 minutes and use low-friction interactions:

- Choice
- Sorting
- Matching / connecting
- Short reasons

Avoid:

- Long text Q&A
- Knowledge exams
- Personality tests
- Self-rated ability questionnaires

The Hero Role trial for `ai_product` follows:

Scenario -> First Judgment -> Evidence Gathering / Information Choice -> Twist -> Reconsideration -> Final Decision

## Evidence Model

Behavior Events record what the user did in the trial. Evidence interprets one or more Behavior Events against a role requirement, while preserving uncertainty and limits.

Every Evidence object must answer:

- What did the user do?
- At which step did it happen?
- Why is it related to the capability or requirement?
- What is the maximum it can support?
- What can it not support?

The system must strictly distinguish:

- "Not observed" from "insufficient capability"
- Background evidence from task evidence
- Interest feedback from evidence
- Recommendation inputs from evaluation evidence

Recommendation data must not flow into the Evidence Radar or Current Evidence Profile.

## Analysis Report

The report is an exploration summary, not a verdict. It must include:

- Role Requirement Profile
- Current Evidence Profile
- Evidence Replay
- Resume / Background Evidence
- Task Evidence
- Interest Feedback
- Unknowns
- Tensions
- Next Mission
- Boundary notice

If a radar is retained, it must be labeled and interpreted as Current Evidence Profile. It must not be presented as a permanent ability profile.

## Next Mission

Next Mission is the recommended follow-up action for gathering missing evidence. It should be specific, lightweight, and tied to a known evidence gap or unknown.

Each Next Mission should include:

- Target role
- Evidence gap
- Mission prompt
- Suggested steps
- Estimated time
- Deliverable
- Future evidence use

## Recommendation Boundary

Recommendations are navigation only. They may suggest which roles are worth trying first, but they must not output ability judgments.

Allowed recommendation sources:

- User work-style preferences
- AI usage style
- Real experience tags
- Skill tags
- Active career intent
- Explicit rejection

Forbidden recommendation sources:

- Gender
- Age
- School prestige
- Company prestige
- Photo

Recommendation language must avoid deterministic terms such as "best for", "naturally suited", or their Chinese equivalents.

The internal ranking variable is `navigationScore`. Do not use `fitScore`.

## Evaluation Boundary

Evaluation only uses evidence that can be replayed to real user actions or explicitly supplied background. The system must not infer stable traits from absence of evidence.

Allowed evaluation sources:

- Trial Behavior Events
- User-provided Resume / Background Evidence
- Explicit Interest Feedback, stored separately

Forbidden evaluation behavior:

- Unified Job Fit Score
- Permanent ability labels
- Treating unobserved capability as weakness
- Letting recommendation data influence Current Evidence Profile

## Privacy Boundary

P0 should collect only what is needed for navigation, trial operation, and report generation. Sensitive attributes and visual identity data must not be used for recommendation or evaluation.

Session deletion must remove session-linked profile, behavior, evidence, report, and interest data where supported by implementation. Demo mode data should be clearly separate from real user data.

## Demo Mode

Demo mode exists to keep the hackathon flow resilient. It may use seeded recommendations, task templates, and report fallbacks, but it must preserve product boundaries:

- Demo recommendations remain navigation-only.
- Demo evidence must still describe source and limits.
- Demo reports must separate background evidence, task evidence, and interest.
- Demo copy must avoid deterministic career claims.

## Acceptance Criteria

- `docs/product` contains one canonical current product documentation set with no version suffixes.
- PRD states the frozen source of truth, snapshot date, and repository/Feishu boundary.
- Five canonical `jobId` values are documented.
- Legacy `jobId` migration rules are documented.
- `ai_product` is documented as Hero Role for execution convenience only.
- Role matching uses `navigationScore`, Top 3, refusal handling, and no ability judgment.
- Task design documents the 3-5 minute work-scenario container.
- Evidence evaluation defines replayable evidence and strict boundary language.
- Schemas define `RoleDefinition`, `TaskTemplate`, and `AnalysisReport` as stable first-level containers.
- Repo alignment gaps are documented before code migration.
- No new business capability is implemented in this PR.

## Decision Log

| Decision | Status | Notes |
| --- | --- | --- |
| Core product chain has five stages from Job Definition to Next Mission. | Frozen | No new first-level function in P0. |
| Five canonical roles replace legacy role IDs. | Frozen | Migration must be explicit and reviewed. |
| `ai_product` is the Hero Role. | Frozen for P0 | Chosen because existing backend `ai_pm` data is closest to the loop. |
| Recommendations are navigation-only. | Frozen | Must not evaluate ability. |
| Evidence must be replayable. | Frozen | Every claim must trace to a source step or background source. |
| No unified Job Fit Score. | Frozen | Use separated evidence profiles and unknowns. |
| Radar, if retained, means Current Evidence Profile. | Frozen | It is not a permanent ability portrait. |
| User may choose non-recommended roles. | Frozen | Recommendations cannot block exploration. |

## Repo Alignment Audit

| PRD Requirement | Existing Backend Capability | Gap | Recommended Action | Category |
| --- | --- | --- | --- | --- |
| Five canonical roles use `ai_product`, `ai_ops`, `ai_data_eval`, `ai_app_dev`, `ai_ui_design`. | `src/main/resources/seed/jobs.json` contains `ai_pm`, `ai_ux`, `ai_consultant`, `ai_operator`, `ai_researcher`. | Seed data is still legacy. | Migrate safe IDs and content-review consultant/researcher material before reuse. | SMALL CHANGE |
| Hero Role is `ai_product`. | `src/main/resources/seed/task-templates/ai_pm.career_changer.json` provides the closest product task. | Template ID and filename are legacy; time is 7 minutes. | Content-level migrate to `ai_product`, tighten to 3-5 minutes. | SMALL CHANGE |
| Task container supports all roles. | `TaskTemplate` stores `jobId`, `scaffoldType`, and JSON content; `TaskService` can serve templates by job and scaffold. | Only two templates exist. | Reuse container; add canonical role templates incrementally. | SUPPORTED |
| Behavior Event capture. | `StepSubmitRequest` accepts `answer` and `events[]`. | Evidence conversion contract is not fully documented in API schema. | Add structured event schema when implementation migration begins. | SMALL CHANGE |
| Evidence is replayable through `sourceStep`. | Demo report includes task evidence with `stepSource`. | Naming differs from frozen `sourceStep`; background evidence replay is partial. | Rename or map `stepSource` to `sourceStep`; add replay fields. | SMALL CHANGE |
| Resume / Background Evidence, Task Evidence, and Interest are separated. | `ExplorationReport` has `resumeRadarData`, `taskEvidenceSummary`, and `interestSignals`. | `resumeRadarData` name can imply radar-first evaluation. | Treat as Current Evidence Profile in UI/API docs; consider field alias later. | SUPPORTED |
| No unified Job Fit Score. | No `fitScore` string found in repository scan. | OpenAPI recommendation lacks `navigationScore`. | Keep avoiding fit score; add `navigationScore` to recommendation contract later. | SMALL CHANGE |
| Recommendation is navigation-only. | `AI/prompts/02-job-recommendation.md` says navigation only and bans ability conclusions. | Fallback reasons are generic and old IDs remain. | Update fallback IDs and add allowed/forbidden sources. | SMALL CHANGE |
| Report boundaries are explicit. | `AI/prompts/06-report-generation.md` separates resume, task, interest and bans deterministic terms. | Uses old PRD refs and resume radar naming. | Align terms with this PRD. | SMALL CHANGE |
| Demo data follows canonical roles. | `src/main/resources/seed/demo-session.json` uses old IDs and `resumeRadar`. | Demo report can reinforce old terminology. | Migrate demo data after canonical seed migration. | SMALL CHANGE |
| API/OpenAPI reflects canonical role IDs. | `docs/openapi.yaml` mentions five jobs but examples use `ai_pm`; schemas are generic. | Examples and recommendation schema are out of date. | Update examples and add `navigationScore` after product docs land. | SMALL CHANGE |
| Frontend config aligns to canonical roles. | No frontend config was present in this backend repository snapshot. | Not applicable in this repo. | Coordinate with frontend repo if separate. | P0 NOT REQUIRED |
| `ai_data_eval` and `ai_app_dev` role trials exist. | No current seed or template exists for these roles. | New content required. | Add after Hero Role is migrated and container is stable. | NEW |

## jobId Migration Notes

Repository locations containing legacy IDs in this snapshot:

- `src/main/resources/seed/jobs.json`
- `src/main/resources/seed/task-templates/ai_pm.career_changer.json`
- `src/main/resources/seed/task-templates/ai_ux.career_changer.json`
- `src/main/resources/seed/demo-session.json`
- `src/main/resources/AI/fallbacks/job-recommendation.json`
- `docs/openapi.yaml`
- `src/main/java/com/Grassroot/JobSearch/ai/SeedDataLoader.java`

Safe migration candidates:

- `ai_pm` -> `ai_product`
- `ai_operator` -> `ai_ops`
- `ai_ux` -> `ai_ui_design`

Content-review candidates:

- `ai_consultant`
- `ai_researcher`

Do not mechanically rename `ai_consultant` or `ai_researcher`. Extract scenario fragments only if they support one of the canonical roles after review.

## Follow-up Backlog

| Item | Reason | P0 Need |
| --- | --- | --- |
| Migrate `jobs.json` to canonical roles. | Required to remove long-term legacy ID coexistence. | Yes |
| Rename/migrate `ai_pm` task template to `ai_product`. | Required for Hero Role consistency. | Yes |
| Tighten Hero Role trial to 3-5 minutes. | Current template estimates 7 minutes. | Yes |
| Add `navigationScore` to recommendation schema. | Needed for explicit role matching contract. | Yes |
| Add structured Behavior Event and Evidence schemas to OpenAPI. | Needed for replayability and frontend/backend alignment. | Yes |
| Update demo session and fallback data. | Needed for demo consistency. | Yes |
| Add templates for `ai_ops`, `ai_data_eval`, `ai_app_dev`, `ai_ui_design`. | Needed for complete five-role experience. | After Hero loop |
| Coordinate frontend role config. | Frontend not present in this repo snapshot. | External |
