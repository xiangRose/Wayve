# AI UI Design Career Trial

Status: FROZEN / PASS
Canonical jobId: `ai_ui_design`
Source of Truth: Feishu
Repository Snapshot Updated: 2026-08-28

This canonical named trial is the approved **AI Document-to-Checklist Flow - Partial Result and Recovery**. It supersedes the older meeting-assistant component-budget candidate. It follows the shared Career Trial, Behavior/Evidence, and Analysis/Growth owners and does not authorize production implementation.

## Trial Contract

The user designs how a person experiences a supplied low-risk community-event brief becoming an editable checklist. The work object is semantic states, transitions, uncertainty, recovery, and state continuity. It is not component placement, visual design, Figma work, meeting-assistant work, extraction correctness, or a knowledge test.

The fixed fixture contains normal items, exactly one meaningful uncertain item, and exactly one unreadable/failed page or section. The user must make bounded decisions about processing/waiting and partial-result behavior; uncertainty disclosure and source/confirm/edit action path; failed-page recovery and preservation or reset of confirmed checklist work; and final flow risk and next usability test.

The experience uses a persistent state-flow workbench, one fixed supplied simulated user, a deterministic path, and at most one retain/revise rerun. It targets 3-5 minutes and exposes consequence drivers rather than scores.

## Semantic Flow and Consequence

The shared semantic stages remain `scenario`, `first_judgment`, `evidence_gathering`, `twist`, `reconsideration`, and `final_decision`. A first meaningful state change occurs within 20-30 seconds. The supplied user path is deterministic for a given flow configuration and may show friction, ambiguity, state loss, or state preservation. A failed first path is information, not a penalty.

## Participant Behavior vs Simulation Trace

Designer Behavior records only participant actions: state/flow inspection, waiting/partial decision, transition change, uncertainty path selection, recovery selection, simulation run, consequence inspection, retain/revise, and final submission. Each event uses exactly one canonical `sourceStep` and the shared event envelope.

`simulated_user_step` is a deterministic consequence/simulation trace. It may store simulated state, supplied end-user action, next state, friction or ambiguity, work loss or preservation, and consequence driver. It is **not** participant Behavior, is not a direct Evidence input, and cannot independently support a designer capability claim. Replay may link it as consequence context.

## Evidence and Report Boundaries

Each Evidence item maps participant Behavior to exactly one role requirement with a maximum bounded claim, explicit limits, source event IDs, source step, and replay. Fixture correctness, simulated user action, Interest, time, and scores cannot independently support a capability claim. `not_observed` means no usable Evidence was produced and never means inability.

Interest Feedback is explicit self-report after the Trial and remains outside Evidence and Current Evidence Profile. The output is compatible with the canonical seven-part Analysis Report and one Next Mission tied to an unknown or evidence gap. No ability score, fit score, radar, stable trait, or career verdict is allowed.

## Minimum Acceptance

The trial is complete when the user changes a state/transition, configures uncertainty and recovery, runs the fixed path, inspects a real consequence, retains or revises, and submits a final decision with risk and next test. Recovery must enact actual checklist continuity or reset. Dragging is never required; click and keyboard alternatives are available.
