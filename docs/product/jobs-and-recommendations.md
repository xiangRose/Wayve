# Job Definitions and Recommendations

Status: Hackathon P0 Frozen
Source of Truth: Feishu
Repository Snapshot Updated: 2026-08-28

This document exclusively owns canonical jobs and navigation recommendation behavior. It does not define evaluation or report conclusions.

## Canonical Jobs

| jobId | Display name | Definition |
| --- | --- | --- |
| `ai_product` | AI Product | Defines AI product problems, priorities, evidence needs, and trade-offs under constraints. |
| `ai_ops` | AI Operations | Designs activation, retention, feedback, and experiment loops for AI products. |
| `ai_data_eval` | AI Data Evaluation | Evaluates AI outputs, data quality, labeling criteria, and evidence consistency. |
| `ai_app_dev` | AI Application Development | Connects product intent, model behavior, and implementation constraints into AI application flows. |
| `ai_ui_design` | AI UI Design | Turns AI capabilities and uncertainty into understandable interfaces, feedback states, and user control. |

The P0 Hero Role is `ai_product`. This is an execution convenience because existing `ai_pm` content is closest to the complete loop; it is not a product ranking of roles.

## Legacy ID Migration

| Legacy ID | Rule |
| --- | --- |
| `ai_pm` | Safe semantic migration to `ai_product`. |
| `ai_operator` | Safe semantic migration to `ai_ops`. |
| `ai_ux` | Safe semantic migration to `ai_ui_design`. |
| `ai_consultant` | No mechanical rename; reuse only reviewed content that belongs to a canonical role. |
| `ai_researcher` | No mechanical rename; reuse only reviewed content that belongs to a canonical role. |

Legacy IDs document migration only and must not appear as active user choices.

## Recommendation Question

Role recommendation answers only: **Which role trials may be useful starting points?**

It does not answer whether the user is capable, suitable, employable, or likely to succeed. The complete canonical role list remains available even when recommendations exist.

## Allowed Inputs

Only the following may contribute to `navigationScore`:

- `workPreferenceTags`
- `aiUsageStyleTags`
- sourced `experienceTags`
- sourced `skillTags`
- `careerIntentTags`
- `rejectedJobIds`

User stage, clarity, and current status may shape explanation or exploration breadth but do not rank a role by themselves. Raw resume or background text must first become sourced allowed tags.

Forbidden inputs include gender, age, school prestige, company prestige, photo, health data, and inferred personality.

## Role Feature Mapping

| jobId | Work activities reflected in allowed tags |
| --- | --- |
| `ai_product` | Prioritization, ambiguity handling, requirements, user feedback, coordination, product experiments. |
| `ai_ops` | Activation, retention, campaign or community operations, funnel analysis, execution cadence. |
| `ai_data_eval` | Output comparison, labeling, rubric design, consistency checking, QA, benchmark review. |
| `ai_app_dev` | Programming, prototyping, APIs, automation, debugging, implementation trade-offs. |
| `ai_ui_design` | User flows, information hierarchy, prototyping, interaction states, usability feedback. |

## navigationScore and Top 3

`navigationScore` is an internal navigation ranking value. It is not displayed as an ability score and never enters Evidence, Current Evidence Profile, Analysis Report, or Next Mission gap analysis.

P0 returns up to three recommendations. Each contains canonical `jobId`, display name, reason, and internal `navigationScore`. Numeric values may remain hidden.

Tie-break order:

1. Explicit career-intent match.
2. Stronger source quality, such as sourced experience over vague preference.
3. Diverse exploration coverage.
4. Stable order: `ai_product`, `ai_ui_design`, `ai_ops`, `ai_data_eval`, `ai_app_dev`.

## Recommendation Copy

Approved pattern:

> You mentioned [source]. This relates to [role work activity], so this role may be worth trying first.

Do not say “best fit,” “most suitable,” “naturally suited,” “born for,” or equivalent deterministic language.

## Rejection and Sparse Profiles

Rejecting a recommendation removes it from the current Top 3 but leaves it selectable. Rejection is a navigation preference, not negative evidence.

With insufficient profile data, provide neutral starting points and do not fabricate personalized reasons. `ai_product` may appear first only because it is the fastest complete P0 path.

## Isolation Contract

The only bridge from recommendation to evaluation is navigation: the user selects a trial. Recommendation inputs, rank, reasons, rejection, and `navigationScore` cannot become Evidence or affect any capability statement.
