# Evidence Evaluation

Status: Hackathon P0 Frozen
Source of Truth: Feishu
Repository Snapshot Updated: 2026-08-28

Evidence evaluation turns observable user behavior into bounded product claims. It must preserve traceability, uncertainty, and separation between different evidence sources.

Do not freeze a universal weighting formula such as 30/70 or 40/60 in P0. Evidence strength depends on source quality, source step, task design, and requirement relevance.

## Behavior Event

A Behavior Event is a raw record of what the user did.

Typical fields:

- `eventId`
- `taskSessionId`
- `jobId`
- `sourceStep`
- `stepType`
- `actionType`
- `selectedOptions`
- `rankedOptions`
- `matchedPairs`
- `shortReason`
- `timeSpentMs`
- `helpUsed`
- `createdAt`

Behavior Events do not make capability claims by themselves.

## Evidence

Evidence is an interpretation of one or more Behavior Events or background sources against a Role Requirement Profile.

Required fields:

- `evidenceId`
- `jobId`
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

Every Evidence object must answer:

- What did the user do?
- Where did it happen?
- Why is it related to the requirement?
- What is the strongest claim it can support?
- What can it not claim?

## sourceStep

`sourceStep` identifies where the relevant behavior happened in the trial or background review.

Examples:

- `scenario`
- `first_judgment`
- `evidence_gathering`
- `twist`
- `reconsideration`
- `final_decision`
- `background_resume`
- `background_profile`

For task evidence, `sourceStep` must allow replay back to the user's actual action.

## judgmentRelation

`judgmentRelation` describes how the observed action relates to a role requirement:

- `direct`: The action directly exercises the requirement.
- `indirect`: The action is adjacent and may transfer, but does not directly verify the requirement.
- `contradictory`: The action creates tension against another evidence point or expectation.
- `insufficient`: The action is too weak or ambiguous to support a claim.

## supportLevel

`supportLevel` describes how much support the evidence provides in this specific context:

- `strong_support`
- `moderate_support`
- `limited_support`
- `weak_signal`
- `not_observed`

`not_observed` means the task did not observe evidence. It does not mean the user lacks the capability.

## confidence

`confidence` describes reliability of the interpretation:

- `high`
- `medium`
- `low`

Confidence is affected by source quality, task clarity, whether the user used hints, ambiguity of the answer, and consistency with other evidence.

## notObserved

`notObserved` records requirements that were not tested or could not be inferred.

Rules:

- It must not be converted into negative ability language.
- It should explain why the evidence was unavailable.
- It may feed Next Mission suggestions.

Example:

> Visual execution quality was not observed because the P0 `ai_ui_design` task uses text and ordering interactions, not high-fidelity design output.

## tension

`tension` records meaningful conflicts or unresolved signals.

Examples:

- User shows stronger task evidence for `ai_product` but stronger interest in `ai_ui_design`.
- Background evidence suggests project coordination, but task evidence did not observe prioritization.
- User selected a quick solution but did not state validation metrics.

Tension should guide reflection and Next Mission design, not become a verdict.

## Role Requirement Profile

Role Requirement Profile defines what a role needs and which trial behaviors may produce evidence.

It includes:

- `jobId`
- `requirements[]`
- `requirementId`
- `label`
- `description`
- `observableBehaviors`
- `nonClaims`

Role Requirement Profile belongs to role definition, not user evaluation.

## Current Evidence Profile

Current Evidence Profile is the user's current evidence map based on available sources.

If the product displays a radar, the radar must represent Current Evidence Profile only. It must not be named or interpreted as a permanent ability profile.

Current Evidence Profile may include:

- Requirement coverage
- Evidence count by source type
- Highest support level per requirement
- Unknowns
- Tensions

It must not include role recommendation `navigationScore`.

## Evidence Replay

Evidence Replay lets the user and team inspect why the report says what it says.

Each replay item should include:

- Source step
- User action
- User short reason, if provided
- Related requirement
- Interpretation
- Limits

Replay can be summarized in UI, but the underlying data should remain inspectable for debugging and trust.

## Interest Feedback

Interest Feedback captures how the user felt about the role trial and whether they want to continue exploring it.

It may include:

- `likeLevel`
- `longTermWillingness`
- `feelingSource`
- `freeText`

Interest is important for exploration, but it is not ability evidence. It may influence Next Mission priority only as user preference, not as proof of role capability.

## Unknowns

Unknowns are capabilities, contexts, or requirements that the current experience did not observe.

Unknowns should be explicit so the report can avoid overclaiming. They are also the main input for Next Mission.

## Next Mission

Next Mission proposes a bounded follow-up task to gather missing evidence.

It should include:

- Target role
- Evidence gap or unknown
- Mission prompt
- Suggested steps
- Estimated time
- Deliverable
- How completion can update the Current Evidence Profile

## Recommendation Isolation

Recommendation data must not flow into Evidence, Current Evidence Profile, radar, or report capability statements.

Allowed recommendation output:

- Role navigation order
- Recommendation reasons
- Rejected role handling

Forbidden evaluation use:

- Treating `navigationScore` as ability evidence
- Using recommendation rank as a report claim
- Combining recommendation score with task evidence
