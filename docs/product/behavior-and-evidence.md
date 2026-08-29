# Behavior Events and Evidence

Status: Hackathon P0 Frozen
Source of Truth: Feishu
Repository Snapshot Updated: 2026-08-28

This document exclusively owns the transformation from observable behavior into bounded Evidence. It does not own trial presentation or report reading order.

## Separation Model

The system keeps four concepts distinct:

1. Recommendation data chooses navigation only.
2. Behavior Events record what happened.
3. Evidence interprets behavior or supplied background against a role requirement.
4. Interest Feedback records the user's stated feeling and willingness; it is not Evidence.

No universal weighting such as 30% background + 70% task is allowed in P0. Evidence strength depends on source quality, task clarity, requirement relevance, ambiguity, and replayability.

## Behavior Event

A Behavior Event is an immutable raw record of a user action or task-state consequence. Common fields:

- `eventId`
- `taskSessionId`
- canonical `jobId`
- semantic `sourceStep`
- `stepType`
- `actionType`
- `selectedOptions`, `rankedOptions`, or `matchedPairs`
- `shortReason`
- `objectId`
- `stateBefore` and `stateAfter`
- action-specific `payload`
- `timeSpentMs`
- `helpUsed`
- `createdAt`

Only relevant fields need values. Behavior Events make no capability claim by themselves. Time, help use, inspection order, and first-attempt failure are context, not automatic penalties.

## sourceStep Contract

Task Events use exactly one of:

- `scenario`
- `first_judgment`
- `evidence_gathering`
- `twist`
- `reconsideration`
- `final_decision`

Background sources use `background_resume` or `background_profile`.

Visible labels such as four user-facing steps or three scene acts do not replace this internal vocabulary. A scene interaction maps to the semantic purpose of the action; scene names may be retained in `payload`.

## Evidence Object

Evidence interprets one or more Events or an explicit background source against one Role Requirement:

- `evidenceId`
- canonical `jobId`
- `requirementId`
- `sourceType`
- `sourceStep`
- `sourceEventIds`
- `observedAction`
- `judgmentRelation`
- `supportLevel`
- `confidence`
- `notObserved`
- `tension`
- `supports`
- `limits`
- `replay`

Every Evidence item answers what happened, where it happened, why it relates, the strongest supported claim, and what it cannot prove.

## Interpretation Vocabulary

`judgmentRelation`:

- `direct` — directly exercises the requirement in this context.
- `indirect` — adjacent and possibly transferable.
- `contradictory` — creates meaningful tension with another item.
- `insufficient` — too ambiguous to support a claim.

`supportLevel`:

- `strong_support`
- `moderate_support`
- `limited_support`
- `weak_signal`
- `not_observed`

`confidence` is `high`, `medium`, or `low` and reflects source reliability and interpretation ambiguity, not the user's ability level.

## Not Observed and Tensions

`not_observed` means the experience did not produce usable evidence. It must state why and must never be translated into inability, weakness, or a low score.

A tension preserves conflicting or nuanced evidence without forcing a verdict. Examples include task evidence and Interest Feedback pointing in different directions, or an initial choice conflicting with later reasoning. Tensions guide reflection and Next Mission design.

## Role Requirement Profile

A Role Requirement Profile belongs to the RoleDefinition and contains requirements, descriptions, observable behaviors, and explicit non-claims. It describes the role, not a user.

## Current Evidence Profile

Current Evidence Profile summarizes currently available, separated evidence. It may show requirement coverage, evidence counts by source, strongest contextual support, unknowns, and tensions.

If visualized as a radar, it must be labeled **Current Evidence Profile**, not ability profile. It excludes `navigationScore`, recommendation rank, rejection data, and Interest Feedback.

## Evidence Replay and Cards

Every task-evidence claim must replay to source Events. Replay includes semantic source step, visible task context, user action, short reason when present, related requirement, interpretation, and limits.

Evidence Card is only a presentation over `sourceStep`, `sourceEventIds`, `observedAction`, `supports`, `limits`, and `replay`. It introduces no Signal Score, evidence type, or ranking.

Background Evidence and Task Evidence remain separate even when shown through the same card pattern.

## Interest Isolation

Interest, energy, engagement, enjoyment, and willingness are explicit self-report. They are stored as Interest Feedback under the report owner.

They must not:

- enter Evidence or Current Evidence Profile;
- affect capability interpretation;
- be merged with task performance;
- prove role suitability;
- become an energy or ability score.

Observed choices may support a bounded statement about work approach in that trial. They still cannot infer stable personality or career fit.

## Evidence Language by Breadth

| Independent evidence base | Maximum language |
| --- | --- |
| 1 trial | Observed Signal / Observed Pattern |
| 2 independent trials | Repeated Signal |
| 3+ independent experiments | Recurring Pattern / Trend |

A single trial cannot support a stable trait, general ability conclusion, or career suitability verdict.

## Isolation from Recommendation

Recommendation inputs, `navigationScore`, Top 3 position, rejection, and recommendation reasons never flow into Behavior interpretation, Evidence, Current Evidence Profile, or capability copy.
