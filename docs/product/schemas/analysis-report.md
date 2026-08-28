# AnalysisReport Schema

Status: Hackathon P0 Frozen
Source of Truth: Feishu
Repository Snapshot Updated: 2026-08-28

`AnalysisReport` is the stable first-level container for the post-trial report. It separates background evidence, task evidence, interest feedback, unknowns, and next mission.

It must not contain a unified Job Fit Score.

Tidal-inspired report organization can be used as presentation logic, but the existing `AnalysisReport` top-level container is sufficient for P0. Do not add a new top-level schema only for visual report sections.

## Canonical Container

```json
{
  "type": "AnalysisReport",
  "reportId": "uuid",
  "sessionId": "uuid",
  "generatedAt": "2026-08-28T00:00:00Z",
  "targetJobId": "ai_product",
  "roleRequirementProfiles": {},
  "currentEvidenceProfile": {},
  "backgroundEvidence": [],
  "taskEvidence": [],
  "interestFeedback": [],
  "evidenceReplay": [],
  "unknowns": [],
  "tensions": [],
  "comparisonSummary": "...",
  "nextMission": {},
  "boundaryNotice": "..."
}
```

## Stable First-Level Fields

| Field | Required | Meaning |
| --- | --- | --- |
| `type` | Yes | Must be `AnalysisReport`. |
| `reportId` | Yes | Report ID. |
| `sessionId` | Yes | Session ID. |
| `generatedAt` | Yes | Generation timestamp. |
| `targetJobId` | No | User-selected target direction, if selected. |
| `roleRequirementProfiles` | Yes | Role requirements used for interpretation. |
| `currentEvidenceProfile` | Yes | Current evidence map. This is not a permanent ability profile. |
| `backgroundEvidence` | Yes | Resume / Background Evidence. |
| `taskEvidence` | Yes | Evidence generated from trial behavior. |
| `interestFeedback` | Yes | User interest and willingness, separate from evidence. |
| `evidenceReplay` | Yes | Replayable links from claims to source actions. |
| `unknowns` | Yes | Not observed or unresolved areas. |
| `tensions` | Yes | Conflicting or nuanced signals. |
| `comparisonSummary` | No | Bounded summary of observed evidence across tried roles. |
| `nextMission` | Yes | Follow-up mission for gathering missing evidence. |
| `boundaryNotice` | Yes | Product boundary and non-verdict notice. |

## Recommended Reading Order

The P0 report should be presented in this order without changing the stable top-level container:

1. Exploration Summary
2. Role Requirement Profile
3. Current Evidence Profile
4. Evidence Replay
5. Interest Feedback
6. Unknowns & Tensions
7. Next Mission

## Report Language Mapping

| Reusable Report Pattern | WAYVE Field / Section | Boundary |
| --- | --- | --- |
| Summary | `comparisonSummary` or Exploration Summary presentation | Must remain bounded to observed evidence. |
| Recurring theme | `currentEvidenceProfile`, `taskEvidence`, `tensions` | Use Recurring Pattern only after 3+ independent experiments. |
| Trend | Evidence Change presentation across cycles | P0 should not claim trend from a single trial. |
| Reflection | `interestFeedback` | Interest is not ability evidence. |
| Direction change | User-confirmed direction update in long-term Growth Track | AI must not announce career switching. |

## Evidence Shape

```json
{
  "evidenceId": "uuid",
  "jobId": "ai_product",
  "requirementId": "evidence_judgment",
  "sourceType": "task",
  "sourceStep": "evidence_gathering",
  "sourceEventIds": ["uuid"],
  "observedAction": "The user selected funnel and segment data before deciding.",
  "judgmentRelation": "direct",
  "supportLevel": "moderate_support",
  "confidence": "medium",
  "notObserved": false,
  "tension": null,
  "supports": "May support evidence-seeking behavior in a constrained product scenario.",
  "limits": "Does not prove broader product strategy ability.",
  "replay": {
    "stepTitle": "Evidence Gathering",
    "userAction": "...",
    "userReason": "..."
  }
}
```

## Interest Feedback Shape

```json
{
  "jobId": "ai_product",
  "likeLevel": "high",
  "longTermWillingness": "maybe",
  "feelingSource": "task_process",
  "freeText": "..."
}
```

Interest Feedback must remain separate from Background Evidence and Task Evidence.

Interest Feedback may express willingness to continue similar work. It must not enter `currentEvidenceProfile` and must not affect capability judgment.

## Next Mission Shape

```json
{
  "targetJobId": "ai_product",
  "evidenceGap": "Metric validation was only partially observed.",
  "missionPrompt": "...",
  "steps": [],
  "estimatedTime": "45-60 minutes",
  "deliverable": "...",
  "futureEvidenceUse": "Can update Current Evidence Profile after review."
}
```

P0 may display the Next Mission as pending future evidence after report generation. Do not mark new evidence or direction update as completed until the user has actually completed a later mission.

## Evidence Card Presentation

Evidence Card is a UI/content presentation pattern over existing evidence fields. It should use:

- `sourceStep`
- `sourceEventIds`
- `observedAction`
- `supports`
- `limits`
- `replay`

No `signalScore` or equivalent field should be introduced for Evidence Cards.

## Boundary Rules

- Do not include `fitScore`.
- Do not include a unified Job Fit Score under another name.
- Do not treat `notObserved` as inability.
- Do not let recommendation `navigationScore` flow into this container.
- If radar UI is retained, bind it to `currentEvidenceProfile` only.
- Do not use HealthKit, heart rate, sleep, steps, or energy values for career judgment.
- Do not add Tide Score, Signal Score, or an ability-growth score history.
- Do not claim Recurring Pattern or Trend from a single trial.
