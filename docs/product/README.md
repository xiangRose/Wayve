# WAYVE Product Documentation

Status: Hackathon P0 Active Snapshot
Source of Truth: Feishu
Repository Snapshot Updated: 2026-08-28

This directory is the repository execution snapshot for WAYVE product decisions. Feishu owns decision history. When Feishu and this snapshot conflict, record the gap before implementation.

## Active Product Docs

Each product responsibility has exactly one canonical owner.

| Canonical owner | Responsibility |
| --- | --- |
| [Product Overview](product-overview.md) | Product definition, P0 goal and scope, information architecture, global boundaries. |
| [Jobs and Recommendations](jobs-and-recommendations.md) | Canonical job definitions, role navigation, recommendation inputs and boundaries. |
| [Career Trials](career-trials.md) | Shared trial contract, the `ai_product` P0 vertical slice, interaction grammars, and trial-specific delegation. |
| [Behavior and Evidence](behavior-and-evidence.md) | Behavior Events, evidence interpretation, replay, uncertainty, and evaluation isolation. |
| [Analysis and Growth](analysis-and-growth.md) | Analysis Report reading logic, Interest Feedback, Next Mission, Growth Track, and Direction Update. |
| [Schemas](schemas/README.md) | Stable engineering-facing containers and field contracts. |
| [Hackathon Execution Plan](hackathon-execution-plan.md) | Repository gaps, migration sequence, P0 acceptance, and follow-up work. |

Specific career trials live under `trials/`. They are normative only for the named trial and must follow the shared owners above:

- [AI UI Design Career Trial](trials/ai-ui-design.md) — candidate `ai_ui_design` experience; not P0 production integration.

## Supporting Artifacts

- [Role Trial Research](role-trial-research.md) synthesizes cross-role real-work evidence into candidate Trial design inputs. It is research, not the shared Trial contract or a role-specific Trial specification.

`experience-design-gameplay/` is non-normative:

- `research/` records inputs, alternatives, and design reasoning.
- `prototype/` contains a disposable standalone interaction prototype.
- `inputs/` contains supplied source material and references.

Supporting artifacts may demonstrate or motivate a decision, but never override Active Product Docs. If they conflict, the active canonical owner wins.

## Change Boundary

This documentation snapshot does not authorize backend, `src`, `AI`, OpenAPI, seed, frontend, or data migration. Implementation work requires a separately approved change and must follow the gaps in the Hackathon Execution Plan.
