# Career Trial Design

Status: Hackathon P0 Frozen
Source of Truth: Feishu
Repository Snapshot Updated: 2026-08-28

This document exclusively owns shared Career Trial behavior and the P0 `ai_product` vertical slice. Evidence interpretation belongs to [Behavior and Evidence](behavior-and-evidence.md); report presentation belongs to [Analysis and Growth](analysis-and-growth.md).

## Shared Trial Contract

Career trials are 3–5 minute slices of realistic work, not exams or disguised questionnaires. Every trial uses these internal semantic stages:

`scenario -> first_judgment -> evidence_gathering -> twist -> reconsideration -> final_decision`

The stages are evidence anchors, not mandatory pages. A visible experience may use four or fewer user-facing steps when it maps each action to exactly one internal `sourceStep`.

## Presentation Grammars

| Grammar | Use | Contract |
| --- | --- | --- |
| Structured decision | Prioritization, evaluation, and implementation judgments. | Visible steps normally map one-to-one to semantic stages. |
| Persistent work scene | Investigation, composition, simulation, and revision of one work object. | Scene acts may contain several semantic stages; emitted events retain one `sourceStep` each. |

The P0 `ai_product` slice uses structured decision. The candidate [AI UI Design Career Trial](trials/ai-ui-design.md) uses a persistent scene. A role-specific trial cannot redefine shared stage, evidence, report, or growth rules.

## Experience Requirements

- One work situation and goal remain understandable throughout.
- Interactions are lightweight and observable.
- Choices have visible consequences when the scenario supports them.
- Users can inspect relevant information without specialized prior knowledge.
- A failed attempt reveals information and allows reconsideration; it is not automatically negative evidence.
- Dragging always has a click or keyboard fallback.
- Short reasons may explain a choice but long free text is not the primary mechanic.

Preferred interactions include choice, sorting, matching, information selection, point-and-click investigation, constrained composition, simulation, and revision.

Avoid knowledge tests, personality tests, self-rated ability, generic strength prompts, percentage career scores, and cosmetic game framing around unchanged question pages.

## Structured Choice Authoring Standard

When a frozen role mechanic naturally uses a structured choice, content authors may reuse the teammate standard: one coherent work timeline; prompts of no more than three lines; four plausible competing options; approximately 12 seconds for the first judgment; one primary requirement per item; mixed action, validation, trade-off, and boundary decisions; randomized option positions; and QA checks for cueing, realism, and obvious one-right-three-wrong construction. This standard supports content authoring and QA only; it does not replace a role's interaction grammar or turn every Trial into multiple-choice questions.

## Stage Meanings

| sourceStep | Required meaning |
| --- | --- |
| `scenario` | Establish role, work object, goal, and constraints. |
| `first_judgment` | Capture an initial direction before all consequences are known. |
| `evidence_gathering` | Let the user seek or select relevant inputs. |
| `twist` | Reveal a meaningful consequence, contradiction, or edge case. |
| `reconsideration` | Provide a real opportunity to update the work. |
| `final_decision` | Preserve final work state, trade-off, and uncertainty. |

If a trial cannot observe a stage, it must state why. Related requirements become `not_observed`; the trial must not silently infer them.

## P0 Minimum Viable Role: ai_product

The user joins an AI meeting-assistant team. Registrations rise, first successful generation is low, and seven-day retention declines. Users who finish setup value the summary, but first use is difficult. With limited engineering capacity, the user must choose the most valuable near-term improvement.

| Stage | Experience |
| --- | --- |
| `scenario` | Understand the activation and retention problem. |
| `first_judgment` | Initially prioritize first-use completion, summary quality, or feature discovery and give a short reason. |
| `evidence_gathering` | Choose limited funnel, segment-retention, user-feedback, or effort information. |
| `twist` | Learn that enterprise retention is stronger, personal users drop at import, and users completing import value summaries. |
| `reconsideration` | Update or retain the priority with a bounded explanation. |
| `final_decision` | Choose simplify import, add templates, or improve quality; state trade-off, validation metric, and uncertainty. |

This trial may observe prioritization, evidence seeking, hypothesis revision, trade-offs, metric selection, and communication inside the scenario. It cannot prove long-term product leadership, general domain expertise, or career suitability.

## Five-role Frozen Contract Summary

The five roles share the trial, Behavior, Evidence, Replay, Interest, and report boundaries while retaining distinct work objects:

| jobId | Role purpose | Must-have mechanic | P0 status |
| --- | --- | --- | --- |
| `ai_product` | Product direction under user, business, and delivery constraints. | Evidence-informed priority, visible consequence, retain/revise, final deliverable. | One complete vertical slice. |
| `ai_ops` | Activation, retention, feedback, and experiment loops. | Diagnose a lifecycle gap, choose an intervention, inspect modeled consequence, revise. | Frozen contract; implementation follow-up. |
| `ai_data_eval` | Defensible quality standards and launch decisions. | Inspect cases, configure quality/review boundary, retest contradiction, revise rollout judgment. | Frozen contract; implementation follow-up. |
| `ai_app_dev` | AI application behavior under quality, latency, cost, and stability constraints. | Configure a fixed request suite, inspect regression/fallback consequence, revise runtime choice. | Frozen contract; implementation follow-up. |
| `ai_ui_design` | Understandable AI states, uncertainty, waiting, and recovery. | Edit a state/transition flow, simulate a supplied path, inspect consequence, revise. | Frozen contract; canonical authority conflict pending. |

Each role's detailed mechanics remain in its frozen Role Trial Spec. Requirement IDs and event mappings must be promoted to `RoleDefinition` and `TaskTemplate` before implementation; runtime inference is prohibited.

## Other Role Trial Ownership

| jobId | Candidate work focus | Status |
| --- | --- | --- |
| `ai_ops` | Diagnose activation or retention and choose a bounded experiment. | Follow-up content. |
| `ai_data_eval` | Inspect outputs and criteria, encounter contradiction, revise a rubric. | Follow-up content. |
| `ai_app_dev` | Connect AI capability to a workflow under implementation constraints. | Follow-up content. |
| `ai_ui_design` | Investigate, assemble, test, and revise an AI interaction flow. | Candidate spec and standalone prototype; not P0 production integration. |

## Behavior Capture Handoff

Trial implementations emit the common Behavior Event envelope defined by [Behavior and Evidence](behavior-and-evidence.md) and [TaskTemplate Schema](schemas/task-template.md). Trial files may define action-specific `payload`, but cannot define new evidence types or report reading orders.

## Copy Boundary

Allowed: “This behavior may support…”, “This was observed at…”, and “This task did not observe…”.

Forbidden: “You are good/bad at…”, “This role is your best fit”, “You are naturally suited”, or any stable personality or ability conclusion.
