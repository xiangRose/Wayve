# AI UI Design Career Trial

Status: P0 Candidate / Prototype Validated
Canonical jobId: `ai_ui_design`
Source of Truth: Feishu
Repository Snapshot Updated: 2026-08-28

This is the canonical product specification for the candidate scene-based `ai_ui_design` career trial. It follows [Career Trial Design](../career-trials.md), [Behavior and Evidence](../behavior-and-evidence.md), and [Analysis and Growth](../analysis-and-growth.md). It does not change the frozen P0 Hero Role (`ai_product`) or authorize production integration.

## Trial Promise

The user experiences a short slice of AI product experience design: investigate conflicting inputs, assemble a usable AI flow under a component constraint, test it, and revise one failure. The trial evaluates only observable behavior inside this scenario. It does not test visual taste, software proficiency, or long-term design ability.

## Scenario

**Demo Day Rescue: repair the key flow of an AI meeting assistant.**

An internal demo starts at 15:00. The current product puts six entrances on the home screen, generation takes 12–20 seconds without clear feedback, results show no source, and failure only says “Something went wrong.” The user joins as an AI UI Designer and has 3–5 minutes to help a first-time user turn meeting notes into action items.

## Mission and Constraints

Within a budget of five interface components, help the simulated user:

1. Find the primary start action.
2. Understand that AI generation is in progress.
3. inspect the source of generated action items.
4. Recover or continue when generation fails.

The user is not required to use Figma or know AI terminology. Professional work is compressed into exploration, click/add or drag/drop, simulation, and one revision.

## Experience Structure

The shared six-stage trial semantics map to three scene acts for this trial:

| Shared semantic stage | Scene act | User experience |
| --- | --- | --- |
| `scenario` | Briefing | Accept the design ticket and understand the work goal. |
| `first_judgment` | Explore | Choose what to inspect first and form an initial view of the problem. |
| `evidence_gathering` | Explore | Inspect user, product, engineering, analytics, model, or design-system objects. |
| `twist` | Test | The simulation exposes a missing loading, source, or recovery state. |
| `reconsideration` | Revise | Return to the same canvas and change at least one design choice. |
| `final_decision` | Review | Keep a final layout and review observed behavior, limits, and interest. |

The UI may present only `Explore -> Design -> Test & Revise`; emitted events must retain the shared semantic `sourceStep` values so evidence remains comparable and replayable.

## Persistent Workspace

Desktop layout uses a persistent workspace rather than one question per page:

- Left: discoverable work objects.
- Center: laptop canvas that changes from current product to design canvas to testable prototype.
- Right: mission, pinned inputs, unresolved risks, and help.
- Top: three work-state indicators—Explore, Design, Test—not a score or exam progress bar.

On narrow screens, sections stack. Drag-and-drop always has a click-to-add and move/remove fallback.

## Work Objects

| Object | Information revealed | Evidence category |
| --- | --- | --- |
| User note | A first-time user cannot find where to begin. | User need |
| PM message | The demo request asks to expose all six entrances. | Product constraint |
| Engineering ticket | At most five components may be added; existing components can be reused. | Delivery constraint |
| Analytics | 38% of new users do not complete first generation. | Product evidence |
| Model status | Typical delay is 12–20 seconds and about 8% of requests fail. | AI behavior constraint |
| Design system | Input, action, progress, result, source, edit, retry, and help components are available. | Reuse constraint |
| Laptop prototype | The persistent object the user investigates, changes, tests, and revises. | Work output |

Objects are optional to inspect. Inspection order, breadth, pinning, and time are recorded; missing an object is not evidence of inability.

## Canvas and Components

The canvas has `primary`, `support`, and `recovery` zones. Users may place no more than five components from:

- Meeting input
- Generate action
- Progress state
- Result card
- Source affordance
- Manual edit
- Retry action
- Help entry

There is no single full-score layout. The simulation checks functional coverage:

- Input and Generate are discoverable as primary actions.
- Progress is visible during generation.
- Source is available with the result.
- Retry or Manual edit provides recovery.
- The five-component budget is respected.

## Simulation and Revision

The simulated user follows the layout the player created:

1. Attempts to start.
2. Waits through compressed generation ticks.
3. Checks the result and its source.
4. Encounters one deterministic failure branch.

Missing components create an explainable blocked or uncertain state at the relevant point. There is no punitive game over. The user can return to the same canvas for one guided revision and run the simulation again.

Outcome language is limited to:

- `ready_for_demo`: all functional checks are covered.
- `usable_with_gaps`: three or four checks are covered.
- `flow_blocked`: zero to two checks are covered.

These are task-state outcomes, not career scores. A blocked first attempt followed by a relevant revision may produce useful iteration evidence.

## Observable Events

Every event follows the shared `TaskTemplate.eventSchema` and adds structured `payload` where needed.

| actionType | payload examples | Evidence use |
| --- | --- | --- |
| `object_opened` | `objectId`, `category`, `visitOrder` | Exploration sequence and breadth. |
| `object_pinned` | `objectId` | Inputs deliberately retained. |
| `component_added` | `componentId`, `zone`, `componentCount` | Hierarchy, state coverage, and constraint handling. |
| `component_moved` | `fromZone`, `toZone` | Reconsideration inside design. |
| `component_removed` | `componentId`, `reasonCode` | Trade-off under budget. |
| `simulation_started` | `layoutSnapshot` | Replay anchor for the tested design. |
| `simulation_step` | `phase`, `outcome`, `missingComponentIds` | Functional consequences of choices. |
| `revision_started` | `failedChecks` | Response to observed failure. |
| `simulation_completed` | `outcome`, `resolvedChecks` | Bounded task performance. |
| `interest_submitted` | `engagementLevel`, `continueWillingness` | Interest Feedback only. |

`timeSpentMs` is context, not a speed score. Help use, inspection order, and first-attempt failure must not automatically reduce support level.

## Trial-specific Evidence Mapping

| Requirement | Observable behavior | Maximum supported claim | Non-claim |
| --- | --- | --- | --- |
| User-problem orientation | Inspects and uses the user note when prioritizing the main path. | May support attention to an explicit user problem in this scenario. | Does not prove general empathy or research skill. |
| Evidence use | Consults relevant user, analytics, engineering, or model inputs before or during design. | May support using available evidence to inform a constrained design. | Does not prove long-term research judgment. |
| Information hierarchy | Makes input/action discoverable and separates supporting/recovery controls. | May support basic hierarchy reasoning in this prototype. | Does not evaluate visual craft or production UI quality. |
| AI state awareness | Includes relevant progress, source, or recovery behavior. | May support awareness of AI delay, explainability, or failure states. | Does not prove model, safety, or technical expertise. |
| Constraint trade-off | Keeps within budget and removes or deprioritizes components. | May support making a scoped interface trade-off. | Does not prove delivery leadership. |
| Iteration | Uses test feedback to resolve at least one functional gap. | May support willingness to revise from observed consequences. | Declining revision does not prove low ability. |

All claims require `sourceEventIds`, a semantic `sourceStep`, an observed action, `supports`, `limits`, and replay data.

## Interest Feedback

After the task, ask separately:

> How did this process of organizing information, making trade-offs, and testing feel?

Suggested values are `engaged`, `neutral`, and `draining`, plus whether the user wants another similar trial. Store the response only as `interestFeedback`. Do not call it Energy evidence, merge it into Current Evidence Profile, or use it to infer capability or suitability.

## Trial-specific Report Field Mapping

| Trial output | AnalysisReport destination |
| --- | --- |
| Functional outcome and revision | `taskEvidence` |
| Exploration, hierarchy, state, and trade-off events | `taskEvidence` and `evidenceReplay` |
| User-provided feeling and willingness | `interestFeedback` |
| Untested visual craft, collaboration, production delivery, and research depth | `unknowns` |
| Conflicts between choices, evidence, and stated interest | `tensions` |
| A bounded follow-up experiment | `nextMission` |

Do not produce a percentage, radar from this trial alone, unified fit score, personality label, “strength/weakness” verdict, or career-direction instruction.

## Accessibility and Responsive Behavior

- Scene objects and components are real buttons or keyboard-operable controls with visible focus.
- Dialogs expose title relationships, close with Escape, and restore focus.
- Dragging is never the only interaction.
- State is communicated by text and structure, not color alone.
- Reduced-motion preference disables non-essential motion.
- Simulation status is announced through an appropriate live region.
- Touch targets are at least 42 px and body text is at least 14 px in the standalone prototype.

## Demo Fallback

Demo mode pins representative user, engineering, and model inputs, then installs Input, Generate, Progress, and Result. It intentionally omits Recovery so the presenter can demonstrate `test -> explainable failure -> revision -> successful rerun` in 60–90 seconds.

Demo mode must be visibly labeled and must not fabricate prior trials, evidence history, or user background.

## Acceptance Criteria

1. The main path can be completed in 3–5 minutes; demo mode in 60–90 seconds.
2. The user can proceed without inspecting every object, and event order remains accurate.
3. A sixth component is rejected with a clear constraint explanation.
4. Missing functional components produce a causal simulation state, not a score deduction.
5. A user can revise and rerun; report evidence cites both attempts where relevant.
6. Reset clears all in-memory scene, layout, simulation, timing, and report state.
7. Interest remains separate from task evidence and Current Evidence Profile.
8. All task-evidence claims are replayable and include explicit limits.
9. The standalone prototype works without network requests and has no blocking console errors.

## Implementation Boundary

This specification validates product behavior only. It does not authorize changes to backend, API, seed data, or production frontend. The existing standalone prototype is a reference artifact and may differ from this canonical contract until a separate implementation task aligns it.
