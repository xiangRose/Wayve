# TaskTemplate Schema

Status: Hackathon P0 Frozen
Source of Truth: Feishu
Repository Snapshot Updated: 2026-08-29

`TaskTemplate` is the stable first-level container for a role trial. Shared experience semantics belong to [Career Trial Design](../career-trials.md), and event/evidence semantics belong to [Behavior and Evidence](../behavior-and-evidence.md). This schema only defines their engineering representation.

Concrete authored content inside the container is owned by the Task Content Library: scenario fixtures, evidence cards, messages, cases, choices, configuration values, deterministic consequence fixtures, and replay snapshot content. This schema does not grant content authority over shared Product semantics or Frozen Role Trial mechanics.

## Canonical Container

```json
{
  "type": "TaskTemplate",
  "jobId": "ai_product",
  "scaffoldType": "career_changer",
  "experienceMode": "structured_decision",
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
  "presentation": {},
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
| `experienceMode` | Yes | Presentation grammar: `structured_decision` or `persistent_scene`. It does not change evidence semantics. |
| `title` | Yes | Trial title. |
| `estimatedMinutes` | Yes | P0 target is 3-5 minutes. |
| `scenario` | Yes | Work-like situation and constraints. |
| `flow` | Yes | Frozen task flow stages. |
| `steps` | Yes | Step definitions and interactions. |
| `presentation` | No | Scene, object, canvas, state, and responsive metadata needed by the chosen experience mode. It must not contain evaluation truth. |
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

For `persistent_scene`, a step can represent a scene act and must declare every semantic stage it covers:

```json
{
  "stepId": "test_and_revise",
  "stepNumber": 3,
  "stepType": "scene_act",
  "sourceSteps": ["twist", "reconsideration", "final_decision"],
  "interaction": {
    "type": "stateful_simulation",
    "stateMachineRef": "presentation.stateMachine"
  },
  "eventEmits": [
    "actionType",
    "objectId",
    "stateBefore",
    "stateAfter",
    "payload",
    "timeSpentMs"
  ]
}
```

Visible act names do not replace the frozen semantic flow. Each emitted event still carries exactly one semantic `sourceStep`.

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
  "objectId": null,
  "stateBefore": null,
  "stateAfter": null,
  "payload": {},
  "createdAt": "2026-08-28T00:00:00Z"
}
```

## Semantic Flow Representation

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

This enum mirrors the Career Trial owner's internal stages; it does not define a second user-facing flow. Visible step labels may differ when events retain exactly one semantic `sourceStep`.

The current backend uses JSON task content under `TaskTemplate`, so the first-level storage model can remain stable.

## P0 `ai_product` Promotion Manifest

The first implementation must preserve these reviewed participant event IDs and semantic mappings:

| Event ID | `sourceStep` | Requirement mapping |
| --- | --- | --- |
| `priority_committed`, `option_deferred`, `short_reason_submitted` | `first_judgment` | `constrained_prioritization`, `decision_communication` |
| `evidence_categories_selected`, `evidence_opened`, `evidence_inspection_ordered`, `evidence_compared` | `evidence_gathering` | `evidence_judgment` |
| `consequence_revealed` | `twist` | consequence context; not standalone participant Evidence |
| `reconsideration_recorded`, `priority_revised` | `reconsideration` | `hypothesis_revision` |
| `validation_metric_selected`, `tradeoff_uncertainty_submitted`, `final_decision_submitted` | `final_decision` | `testable_next_step`, `constrained_prioritization` |

Each emitted event carries exactly one `sourceStep`. `simulated_user_step` is a separate consequence trace and cannot enter Evidence Mapping directly. `experienceMode` is `structured_decision` and uses the shared structured-decision grammar.

`experienceMode`, `presentation`, and the generalized event fields document the product contract only. The current backend alignment remains a follow-up item; this documentation update does not claim those fields are implemented.
