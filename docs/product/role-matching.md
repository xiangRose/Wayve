# Role Matching

Status: Hackathon P0 Frozen
Source of Truth: Feishu
Repository Snapshot Updated: 2026-08-28

Role matching answers one product question: which roles should the user try first?

It does not answer whether the user is good at a role. It does not create a hiring, screening, or permanent ability judgment. Users must always be able to choose any canonical role, including roles outside the recommendations.

## Profile Input Fields

P0 profile data may include:

| Field | Meaning | Recommendation Use |
| --- | --- | --- |
| `userStage` | User stage such as beginner, career changer, or experienced. | Context only; may shape copy, not ranking by itself. |
| `clarityLevel` | Whether the user has a clear direction, partial preference, or unknown direction. | Context only; may influence whether to recommend broader exploration. |
| `currentStatus` | Student, graduate, employed, unemployed, freelance, or other. | Context only; not a ranking feature by itself. |
| `workPreferenceTags` | Preferred working modes. | Allowed recommendation source. |
| `aiUsageStyleTags` | How the user currently uses AI. | Allowed recommendation source. |
| `experienceTags` | Real experience extracted from profile or resume. | Allowed recommendation source. |
| `skillTags` | Skills explicitly provided or extracted with source. | Allowed recommendation source. |
| `careerIntentTags` | Active career interests stated by the user. | Allowed recommendation source. |
| `rejectedJobIds` | Roles the user explicitly rejects. | Allowed negative signal. |
| `backgroundText` | User-provided context. | Only after extraction into allowed tags with source. |
| `resumeText` | Optional resume or background content. | Only after extraction into real experience tags or skill tags. |

## Participating Fields

Only these sources may contribute to `navigationScore`:

- User work-style preferences
- AI usage style
- Real experience tags
- Skill tags
- Active career intent
- Explicit rejection

Each participating feature must retain its source category so the recommendation reason can be explained without ability judgment.

## Forbidden Fields

These fields must not participate in recommendation:

- Gender
- Age
- School prestige
- Company prestige
- Photo

If such data exists in a resume or profile, it must be ignored for role matching. School or company names may be retained as raw resume context only when needed for user display, but their prestige must not be scored or inferred.

## Five-Role Feature Mapping

| jobId | Work-Style Preferences | AI Usage Style | Experience / Skill Tags | Career Intent Tags |
| --- | --- | --- | --- | --- |
| `ai_product` | Prioritization, ambiguity handling, cross-functional decisions, trade-offs. | Uses AI to prototype ideas, compare options, summarize user needs. | Product thinking, requirement writing, user feedback, project coordination, data-informed decisions. | Product, AI product, product manager, product strategy. |
| `ai_ops` | Experimentation, iteration, user activation, execution cadence. | Uses AI to generate campaigns, segment users, analyze feedback, build workflows. | Operations, growth, community, funnel analysis, campaign planning, retention. | Operations, growth, AI operations, user activation. |
| `ai_data_eval` | Detail orientation, consistency checking, criteria design, quality judgment. | Uses AI to compare outputs, label examples, check errors, improve prompts. | Data labeling, evaluation, QA, rubric design, research coding, benchmark review. | AI evaluation, data quality, model evaluation, annotation. |
| `ai_app_dev` | Building, debugging, systems thinking, implementation trade-offs. | Uses AI to code, connect APIs, build demos, automate workflows. | Programming, prototyping, API integration, automation, frontend/backend development. | AI engineer, app developer, software development, prototyping. |
| `ai_ui_design` | Interaction clarity, information hierarchy, user flow, feedback design. | Uses AI for design exploration, user flow drafts, interface copy, prototypes. | UI design, UX design, information architecture, prototyping, usability feedback. | Product design, UX, UI, interaction design. |

## navigationScore

`navigationScore` is an internal navigation ranking value. It estimates which role trials are likely to be useful starting points based on the user's stated preferences, AI usage, experience tags, skill tags, intent, and explicit rejections.

`navigationScore` must not be displayed as an ability score. It must not be merged into evidence, report radar, or Current Evidence Profile.

Suggested P0 inputs:

- Positive tag-role matches increase `navigationScore`.
- Active career intent may increase `navigationScore` when it maps to a role.
- Explicit rejection sets the role as hidden or heavily deprioritized unless the user later reselects it.
- Sparse profile data should reduce certainty, not produce stronger claims.

## Top 3

P0 should return up to three recommended roles:

- `recommendations[0..2]`
- Each item includes `jobId`, display name, recommendation reason, and internal `navigationScore`.
- The UI may hide the numeric value and show reasons only.
- The full canonical role list remains available for free choice.

## Tie-break

When roles have similar `navigationScore`, use deterministic but non-evaluative tie-breaks:

1. Explicit career intent match.
2. Stronger source quality, such as real experience tag over vague preference.
3. More diverse exploration coverage across product, design, operations, evaluation, and development.
4. Stable canonical order: `ai_product`, `ai_ui_design`, `ai_ops`, `ai_data_eval`, `ai_app_dev`.

Tie-breaks must not use school prestige, company prestige, age, gender, photo, or inferred personality.

## Recommendation Reasons

Reasons should cite allowed inputs and explain role work style.

Approved pattern:

> You mentioned [source]. This is related to [role work activity], so this role may be worth trying first.

Chinese product copy should avoid phrases equivalent to "best fit", "most suitable", "naturally suited", "born for", or "not suitable".

## Refusing Recommendations

Users can reject a recommended role. Rejection means:

- The role should be removed from the current Top 3.
- The role remains selectable from the full role list.
- Rejection must not be interpreted as negative ability evidence.
- Rejection may be stored only as navigation preference.

## No Profile Case

If no useful profile data exists:

- Do not fabricate personalized reasons.
- Provide neutral starting points.
- Default first suggestion may be `ai_product` only because it is the P0 Hero Role and fastest complete loop.
- Make the full role list available.
- Explain that the user can start anywhere.

## Strict Separation from Evaluation

Role matching outputs cannot become Evidence. They cannot affect Current Evidence Profile, radar display, analysis report capability statements, or Next Mission gap analysis.

The only bridge from matching to trial is user navigation: which role the user starts with.
