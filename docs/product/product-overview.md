# P0 Product Overview

Status: Hackathon P0 Frozen
Source of Truth: Feishu
Repository Snapshot Updated: 2026-08-29

This document exclusively owns WAYVE's product definition, P0 scope, information architecture, and global product boundaries. Detailed rules are delegated to the linked canonical owners.

## Product Definition

WAYVE is an AI career trial product. It helps users explore AI-era work directions through realistic short trials, observable behavior, bounded evidence, and a next experiment.

The frozen product chain is:

Job Definition -> Career Trial -> Behavior -> Evidence -> Analysis Report -> Next Mission

Recommendation helps a user choose where to start. Evaluation uses only replayable trial behavior or explicitly supplied background. WAYVE is not a hiring tool, psychometric test, or permanent ability profile.

## Hackathon Goal

P0 demonstrates one trustworthy Current Exploration Cycle across five playable complete Mini Career Trials:

1. A user enters Career Exploration without needing a profile.
2. The product presents the five canonical roles equally; up to three navigation recommendations may be shown as optional starting points.
3. The user may choose any canonical role, including a non-recommended role.
4. The user completes a 3–5 minute career trial for any of the five roles end to end.
5. Behavior Events become replayable, bounded Evidence.
6. The Analysis Report separates background, task evidence, and Interest Feedback.
7. The product proposes one Next Mission tied to an unknown or evidence gap.
8. Growth Track shows the Next Mission as pending until a real later experiment occurs.

## Target Users

P0 serves students, early-career users, career changers, and people with adjacent experience who want direct work evidence before choosing an AI-related direction.

## P0 Scope

- Five canonical job definitions.
- Navigation-only role recommendations using internal `navigationScore`.
- Five playable complete Mini Career Trials: `ai_product`, `ai_ops`, `ai_data_eval`, `ai_app_dev`, and `ai_ui_design`. Depth may differ; `ai_product` is the Showcase, not a privileged career option.
- A reusable career-trial container.
- Structured Behavior Event capture and replayable Evidence.
- Analysis Report with the canonical reading order defined in [Analysis and Growth](analysis-and-growth.md).
- One evidence-gap-based Next Mission.
- Current Exploration Cycle in Growth Track.
- Seeded demo fallbacks that preserve all product boundaries.
- Distinct role mechanics remain governed by their Frozen Role Trial Specs.

## Out of Scope

- Unified Job Fit Score or equivalent career-decision score.
- Hiring, ranking, admission, or employment decisions.
- Stable personality, talent, suitability, or inability labels.
- Long-form interviews, knowledge exams, and self-rated ability questionnaires.
- New first-level modules outside the frozen chain.
- HealthKit, heart rate, sleep, steps, or body-health data.
- Tide Score, Signal Score, Energy Score, or any score that decides career direction.
- Client-side AI provider keys.
- Full journal, nudge, reminder, notification, account, or cloud-sync systems.
- Monthly or quarterly reports in P0.
- Fabricated historical cycles, completed missions, new evidence, trends, or direction changes.
- Frontend/backend implementation, data migration, and production integration work in this documentation-only change.

## Authority Domains

Product Docs define WHAT/WHY and semantic boundaries. Frontend and implementation technical specs define implementation and data organization within their technical domain. Frozen Role Trial Specs define role mechanics. The Task Content Library defines concrete authored fixtures and deterministic replay/consequence content. These layers are complementary and cannot silently override one another outside their authority domain.

## Information Architecture

WAYVE retains four first-level entries:

- Home — current exploration, latest report, and next mission.
- Career Exploration — Role -> Preview -> Trial.
- Growth Track — the Current Exploration Cycle; later, real exploration history.
- My — profile, background evidence, privacy, and settings.

Do not add Journal, Energy, Health, Tidal, or Signals as first-level entries.

## Global Boundaries

- Recommendations are navigation, never ability evaluation.
- Interest, energy, and engagement are self-report, never ability evidence.
- `not_observed` means the experience did not observe something; it never means low ability.
- A single trial may support only an Observed Signal or Observed Pattern, not a stable trait, recurring pattern, or trend.
- Direction Update requires a real later experiment and explicit user confirmation.
- Demo data must be visibly identified and cannot manufacture long-term growth.
- Product-pattern reuse does not imply code reuse or technology-stack change.

The detailed owners are [Jobs and Recommendations](jobs-and-recommendations.md), [Behavior and Evidence](behavior-and-evidence.md), and [Analysis and Growth](analysis-and-growth.md).

## Privacy

Collect only data needed for navigation, trial operation, evidence, and report generation. Gender, age, school prestige, company prestige, photo, health data, and visual identity must not influence recommendation or evaluation.

Where supported, session deletion removes session-linked profile, behavior, evidence, report, and interest data. Demo data remains distinguishable from real user data.

## Technology Boundary

WAYVE currently uses Java 17, Spring Boot, JPA, H2, and server-side AI architecture. This product documentation does not authorize importing SwiftUI, SwiftData, HealthKit, client-side provider keys, or any implementation from another product.
