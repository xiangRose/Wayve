# TaskTemplate Schema

Status: Hackathon P0 Frozen
Source of Truth: Feishu
Repository Snapshot Updated: 2026-08-28

`TaskTemplate` is the stable first-level container for a role trial. It defines the work scenario, steps, interactions, observable events, and evidence mapping hints.

## Canonical Container

```json
{
  "type": "TaskTemplate",
  "jobId": "ai_product",
  "scaffoldType": "career_changer",
  "title": "...",
  "estimatedMinutes": 4,
  "scenario": {
    "summary": "...",
    "userGoal": "...",
    "constraints": []
  },
  "flow": [
    "scenario",
    "first_judgment",
    "evidence_gathering",
    "twist",
    "reconsideration",
    "final_decision"
  ],
  "steps": [],
  "eventSchema": {},
  "evidenceMapping": [],
  "copyBoundaries": {}
}
```

## Stable First-Level Fields

| Field | Required | Meaning |
| --- | --- | --- |
| `type` | Yes | Must be `TaskTemplate`. |
| `jobId` | Yes | Canonical role ID. |
| `scaffoldType` | Yes | User-stage or support variant. |
| `title` | Yes | Trial title. |
| `estimatedMinutes` | Yes | P0 target is 3-5 minutes. |
| `scenario` | Yes | Work-like situation and constraints. |
| `flow` | Yes | Frozen task flow stages. |
| `steps` | Yes | Step definitions and interactions. |
| `eventSchema` | Yes | Behavior Event fields emitted by this task. |
| `evidenceMapping` | Yes | Mapping hints from step actions to role requirements. |
| `copyBoundaries` | Yes | Phrases and claims allowed or forbidden in this task. |

## Step Shape

```json
{
  "stepId": "first_judgment",
  "stepNumber": 2,
  "stepType": "first_judgment",
  "prompt": "...",
  "context": {},
  "interaction": {
    "type": "single_choice_with_short_reason",
    "options": []
  },
  "eventEmits": [
    "selectedOptions",
    "shortReason",
    "timeSpentMs",
    "helpUsed"
  ],
  "evidenceTargets": [
    {
      "requirementId": "evidence_judgment",
      "judgmentRelation": "direct",
      "limits": "Only supports evidence use inside this scenario."
    }
  ]
}
```

## Event Schema

```json
{
  "eventId": "uuid",
  "taskSessionId": "uuid",
  "jobId": "ai_product",
  "sourceStep": "evidence_gathering",
  "stepType": "evidence_gathering",
  "actionType": "multi_select_with_short_reason",
  "selectedOptions": [],
  "rankedOptions": [],
  "matchedPairs": [],
  "shortReason": "...",
  "timeSpentMs": 45000,
  "helpUsed": false,
  "createdAt": "2026-08-28T00:00:00Z"
}
```

## P0 Flow Enum

```json
[
  "scenario",
  "first_judgment",
  "evidence_gathering",
  "twist",
  "reconsideration",
  "final_decision"
]
```

The current backend uses JSON task content under `TaskTemplate`, so this schema can be adopted without changing the first-level storage model.
