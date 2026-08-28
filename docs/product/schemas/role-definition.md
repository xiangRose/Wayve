# RoleDefinition Schema

Status: Hackathon P0 Frozen
Source of Truth: Feishu
Repository Snapshot Updated: 2026-08-28

`RoleDefinition` is the stable first-level container for canonical role metadata and requirement profiles. Product definitions, recommendation behavior, and migration policy belong to [Jobs and Recommendations](../jobs-and-recommendations.md).

Container name and canonical `jobId` values should remain stable. Fields may be extended as product learning continues.

## Canonical Container

```json
{
  "type": "RoleDefinition",
  "jobId": "ai_product",
  "displayName": "AI Product",
  "definition": "...",
  "coreWorkObject": "...",
  "workActivities": [],
  "trialSummary": "...",
  "requirementProfile": {
    "requirements": []
  },
  "recommendationFeatures": {
    "workPreferenceTags": [],
    "aiUsageStyleTags": [],
    "experienceTags": [],
    "skillTags": [],
    "careerIntentTags": []
  },
  "boundaries": {
    "canClaim": [],
    "cannotClaim": []
  },
  "status": "active"
}
```
## Stable First-Level Fields

| Field | Required | Meaning |
| --- | --- | --- |
| `type` | Yes | Must be `RoleDefinition`. |
| `jobId` | Yes | One of the five canonical job IDs. |
| `displayName` | Yes | User-facing role name. |
| `definition` | Yes | Short role definition. |
| `coreWorkObject` | Yes | Main object the role works on. |
| `workActivities` | Yes | Common work activities for trial and matching copy. |
| `trialSummary` | Yes | What the user experiences in a short trial. |
| `requirementProfile` | Yes | Role requirements used by evidence evaluation. |
| `recommendationFeatures` | Yes | Allowed matching tags. |
| `boundaries` | Yes | What evidence can and cannot claim. |
| `status` | Yes | `active`, `draft`, or `retired`. |

## Canonical jobId Enum

```json
[
  "ai_product",
  "ai_ops",
  "ai_data_eval",
  "ai_app_dev",
  "ai_ui_design"
]
```

## Requirement Profile Shape

```json
{
  "requirements": [
    {
      "requirementId": "evidence_judgment",
      "label": "Evidence Judgment",
      "description": "Uses relevant evidence to update product judgment.",
      "observableBehaviors": [
        "Selects funnel or segment data to validate a hypothesis",
        "Changes decision when new evidence contradicts the initial judgment"
      ],
      "nonClaims": [
        "Does not prove long-term product leadership ability",
        "Does not prove domain expertise outside the task scenario"
      ]
    }
  ]
}
```
