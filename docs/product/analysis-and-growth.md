# Analysis Report, Next Mission, and Growth Track

Status: Hackathon P0 Frozen
Source of Truth: Feishu
Repository Snapshot Updated: 2026-08-28

This document exclusively owns post-trial product presentation, Interest Feedback, Next Mission, Growth Track, and Direction Update. Evidence semantics come from [Behavior and Evidence](behavior-and-evidence.md).

## Analysis Report Purpose

The Analysis Report is an exploration summary, not a verdict. It keeps Resume / Background Evidence, Task Evidence, and Interest Feedback distinguishable and exposes unknowns and limits.

It must not include a unified Job Fit Score, permanent ability profile, deterministic suitability statement, or recommendation-derived capability claim.

## Canonical Product Reading Order

Every P0 report uses exactly this user-facing order:

1. Exploration Summary
2. Role Requirement Profile
3. Current Evidence Profile
4. Evidence Replay
5. Interest Feedback
6. Unknowns & Tensions
7. Next Mission

Engineering schema fields may be organized differently, but UI and product documentation must not introduce another reading order.

## Section Responsibilities

### Exploration Summary

Summarizes only the current exploration and observed evidence. It names boundaries and does not collapse evidence into one score or verdict.

### Role Requirement Profile

Explains what the selected role commonly requires. It is role context, not a portrait of the user.

### Current Evidence Profile

Shows available Background and Task Evidence against requirements, with source separation, unknowns, and confidence. A radar, if retained, represents current evidence coverage only.

### Evidence Replay

Lets users inspect actions and source context behind task-evidence claims. Claims without replayable events cannot appear as task evidence.

### Interest Feedback

Shows the user's stated engagement, energy, enjoyment, or willingness separately. It is never mixed into Evidence or Current Evidence Profile.

### Unknowns & Tensions

Names what the experience did not observe and where available evidence conflicts. Unknown is not weakness; tension is not a verdict.

### Next Mission

Proposes one bounded follow-up experiment tied to an unknown or evidence gap.

## Next Mission Contract

Each Next Mission includes:

- target canonical role;
- evidence gap or unknown;
- mission prompt;
- suggested steps;
- estimated time;
- deliverable;
- how a reviewed completion could update Current Evidence Profile.

Generation may consider explicit user interest when prioritizing options, but Interest Feedback is not proof of ability. A proposed mission remains pending until the user actually completes a later experiment.

## Growth Track

Growth Track is a time-based record of real career exploration experiments, not an ability-growth score.

Hackathon P0 shows one Current Exploration Cycle:

`Trial Completed -> Evidence Captured -> Analysis Report -> Next Mission -> Pending`

`Pending` is the terminal P0 state. Do not display New Evidence, completed follow-up, long-term history, repeated pattern, trend, or Direction Update unless a real new experiment occurred.

Only after a real later experiment:

`Pending -> New Evidence -> user-confirmed Direction Update`

New Evidence may update the Current Evidence Profile and mission choice. It does not automatically change direction.

## Direction Update

Direction Update is a long-term event explicitly confirmed by the user.

AI may display evidence, unknowns, tensions, Interest Feedback, comparisons across actually experienced roles, and mission options. AI cannot announce a career switch or infer Direction Update from recommendation rank, `navigationScore`, Interest Feedback alone, or `not_observed`.

## Demo Boundary

Demo mode may seed recommendations, task state, evidence examples, and a pending Next Mission to keep the P0 story resilient. Every seeded item must be visibly demo data and preserve source and limits.

Demo mode cannot fabricate completed later missions, New Evidence, historical cycles, recurring patterns, trends, or Direction Update.

## Product Language

- One trial: Observed Signal or Observed Pattern.
- Two independent trials: Repeated Signal.
- Three or more independent experiments: Recurring Pattern or Trend.
- Interest language describes self-report only.
- Not observed language explains coverage, never inability.
- Direction language is reflective and user-confirmed, never deterministic.

## Schema Handoff

[AnalysisReport Schema](schemas/analysis-report.md) owns engineering field shape only. It must support this exact reading order and cannot redefine product language.
