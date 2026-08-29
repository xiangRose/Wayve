# Role Trial Research

Status: Supporting Research / Non-normative
Canonical owners: [Career Trials](../../career-trials.md), [Jobs and Recommendations](../../jobs-and-recommendations.md), [Behavior and Evidence](../../behavior-and-evidence.md), and role-specific specs under [Trials](../../trials/)

This file is supporting research for role-trial scenario selection. It does not define active product rules, role definitions, evaluation rules, schema contracts, or P0 scope. If this research conflicts with a canonical product document, the canonical document governs.

## 1. Purpose

This document connects local role research to career-trial scenario selection:

`real-work evidence -> research synthesis -> candidate trial directions`

It is a product research synthesis and design input. It is not raw JD storage, the shared trial contract, a formal role-specific trial specification, an implementation schema, or an executable task template.

Source evidence remains under [`../../../../local_data/role_research/`](../../../../local_data/role_research/). Shared trial structure, timing, interaction rules, and behavior instrumentation are owned by [Career Trials](../../career-trials.md). A selected role trial becomes normative only when documented under [`../../trials/`](../../trials/).

## 2. Source Status

Counts below describe the currently readable CSV data in the repository. They do not reproduce earlier unverified collection totals.

| jobId | Current usable evidence | Status |
| --- | --- | --- |
| `ai_product` | 10 usable CSV rows | Sufficient for bounded research synthesis; source URLs are not available for independent link verification. |
| `ai_ops` | 10 raw CSV rows / 9 unique rows | `NEED MORE EVIDENCE`; one exact duplicate and a broad role boundary. |
| `ai_data_eval` | 0 CSV data rows | `NEED MORE EVIDENCE`; CSV is header-only. |
| `ai_app_dev` | 0 CSV data rows | `NEED MORE EVIDENCE`; CSV is header-only. |
| `ai_ui_design` | 0 CSV data rows | `NEED MORE EVIDENCE` for role-JD claims. Separate design-research inputs and a prototype exist, but they do not make the role CSV complete. |

No aggregated, normalized, or summary role-research dataset was found alongside the current CSV files. `ai_ops` also has screenshot evidence; the other role folders may contain screenshot directories, but the row counts above are based only on actual CSV data.

## 3. Evidence Discipline

Research statements use these labels:

- **FACT** — directly supported by a current local source.
- **INFERENCE** — a synthesis across sources or a bounded product interpretation.
- **BRAINSTORM** — a potentially useful scenario idea that has not been established as a repeated real-work pattern.
- **NEED MORE EVIDENCE** — current local material is insufficient to support the claim or freeze a direction.

Scenario details, fictional metrics, interface copy, and dramatic constraints must not be presented as JD facts merely because the underlying work pattern is plausible. Candidate directions in this document do not authorize a formal Trial specification or implementation.

## 4. `ai_product` Research Synthesis

Primary source: [`../../../../local_data/role_research/ai_product/manual_import.csv`](../../../../local_data/role_research/ai_product/manual_import.csv).

### Work Objects

- **FACT:** User needs, feedback, usage behavior, and product friction recur across the sample.
- **FACT:** Product definitions, features, flows, prototypes, roadmaps, and iteration plans are common work objects.
- **FACT:** Product metrics and analysis appear across all 10 rows, including retention, conversion, payment, instrumentation, dashboards, experiments, and evaluation systems.
- **FACT:** AI capability and delivery boundaries appear explicitly in multiple rows, including technical feasibility, model behavior, Agent workflows, evaluation, abnormal inputs, and human confirmation.

### Repeated Tasks

- **FACT:** Discover and define user or business problems, then convert them into product direction or requirements.
- **FACT:** Plan, design, validate, launch, and iterate AI product capabilities.
- **FACT:** Use data, user research, feedback, competitor research, prototypes, or experiments to revise product judgment.
- **FACT:** Drive cross-functional delivery; all 10 rows contain delivery, iteration, or cross-functional execution language.
- **INFERENCE:** Prioritization under incomplete evidence is a credible Trial focus. Explicit priority/trade-off language is concentrated in a subset of rows, while adjacent planning and iteration work is broader.

### Collaborators

- **FACT:** Engineering, design, algorithms or research teams, operations or growth teams, users, and business stakeholders recur.
- **FACT:** Executive or strategy stakeholders appear in a smaller subset of roles.
- **INFERENCE:** Static stakeholder messages can supply constraints in a Trial, but they cannot by themselves observe real collaboration, communication, or leadership.

### Conflicts / Trade-offs

- **FACT:** The sample explicitly supports trade-offs between capability and simplicity, flexibility and reliability, and depth and breadth.
- **FACT:** Multiple roles require balancing user value with AI or engineering feasibility.
- **FACT:** Experience, conversion or commercialization, delivery speed, and validation quality create recurring tensions.
- **INFERENCE:** Cost, latency, and quality may be useful AI-product constraints, but this exact three-way framing is not a repeated pattern in the current local sample.

### Deliverables

- **FACT:** Deliverables include PRDs, requirement documents, flowcharts, prototypes, product plans, roadmaps, metric systems, instrumentation, dashboards, experiment plans, research reports, MVPs, Agent workflows, and acceptance criteria.

### Mundane Work

- **FACT:** The work includes maintaining schedules and task boards, following reviews and acceptance, recording meeting notes, synchronizing information, collecting metrics, sorting app-store/community/support feedback, tracking competitor changes, documenting states and edge cases, testing, troubleshooting, and following releases.
- **INFERENCE:** These routine actions may offer more authentic short Trial moments than a broad strategy exercise, especially when they still require a consequential judgment.

### Uncertainty

- **FACT:** Roles require defining ambiguous problems, distinguishing stated requests from underlying needs, understanding AI limits, handling missing data and abnormal inputs, validating new directions quickly, and revising judgment from evidence.
- **INFERENCE:** A useful Trial should preserve one bounded uncertainty instead of supplying enough information to make the answer obvious.

## 5. `ai_product` Trial Scenario Directions

These are research-level candidate directions, not final Trial specifications.

### Product Priority Under Incomplete Evidence

- **Real-work pattern:** Choose a near-term product priority using user evidence, product data, technical feasibility, and delivery constraints.
- **Evidence strength:** Strong for the combined work pattern; partial for any specific fictional funnel, metric, product, or forced choice.
- **Possible Trial moment:** Make an initial priority call, select limited evidence to inspect, then reconsider after a conflicting segment or funnel signal.
- **Current confidence:** **HIGH**.

### Bad-case / User-feedback Triage

- **Real-work pattern:** Organize noisy feedback or AI bad cases, identify a likely root problem, and choose what to investigate or fix first.
- **Evidence strength:** Moderate. Feedback synthesis is broad; explicit bad-case work appears in a smaller subset.
- **Possible Trial moment:** Triage a constrained set of user reports and product signals, then update priority when frequency and impact diverge.
- **Current confidence:** **MEDIUM**.

### AI Feature Feasibility Cut

- **Real-work pattern:** Reduce an ambitious AI feature into a testable scope while preserving user value and stating model or delivery limits.
- **Evidence strength:** Moderate across AI feasibility, rapid validation, prototyping, Agent design, abnormal-input handling, and acceptance criteria.
- **Possible Trial moment:** Keep, defer, or constrain feature elements after a reliability or human-confirmation constraint appears.
- **Current confidence:** **MEDIUM**.

## 6. `ai_ops` Research Status

**NEED MORE EVIDENCE**

Primary source: [`../../../../local_data/role_research/ai_ops/ai_ops_manual_import.csv`](../../../../local_data/role_research/ai_ops/ai_ops_manual_import.csv).

The current file contains 10 raw rows and 9 unique rows. The sample spans:

- Agent strategy;
- product operations;
- community and content operations;
- developer ecosystem operations;
- customer delivery and industry-solution operations;
- GEO and content operations.

Preliminary repeated patterns include data monitoring, operational review, strategy iteration, feedback loops, cross-functional delivery, and converting user or customer problems into executable solutions. However, the work objects and outputs vary substantially by subtype. The repository does not yet support freezing activation, retention, Agent operations, content operations, or customer delivery as the canonical `ai_ops` Trial direction.

## 7. Other Roles

### `ai_data_eval`

- **NEED MORE EVIDENCE:** The current CSV has zero data rows.
- **BRAINSTORM:** Evaluation criteria, bad-case prioritization, and labeling-quality trade-offs remain plausible directions, but the current local role CSV does not prove them.

### `ai_app_dev`

- **NEED MORE EVIDENCE:** The current CSV has zero data rows.
- **BRAINSTORM:** Effect, latency, cost, retrieval, streaming, and fallback decisions remain plausible directions, but the current local role CSV does not prove them.

### `ai_ui_design`

- **NEED MORE EVIDENCE:** The role CSV has zero data rows.
- **FACT:** Separate design-research material, a standalone prototype, and a role-specific candidate specification exist under [`../`](../) and [`../../trials/ai-ui-design.md`](../../trials/ai-ui-design.md).
- **BOUNDARY:** Those artifacts support design exploration and prototype validation; they must not be described as a complete local JD evidence base.

## 8. Interaction Research Notes

Across the research material and existing prototypes, the following interactions appear promising for producing observable behavior:

- structured choice;
- ranking or prioritization;
- active information choice;
- information visit order;
- a meaningful Twist;
- before/after revision;
- constrained short reasons.

The value comes from the decision and its context, not from clicking alone. Opening information without using it, time spent, help use, and omitted information are weak or ambiguous signals unless the Trial supplies a defensible interpretation boundary.

The formal interaction and instrumentation contract is owned by [Career Trials](../../career-trials.md), [Behavior and Evidence](../../behavior-and-evidence.md), and the [TaskTemplate schema](../../schemas/task-template.md).

## 9. Open Research Gaps

- The canonical boundary of `ai_ops` has not been narrowed across its current subtypes.
- `ai_data_eval` lacks real CSV samples.
- `ai_app_dev` lacks real CSV samples.
- `ai_ui_design` lacks role CSV data; separate design inputs must remain clearly distinguished.
- The source set behind the earlier claim of 56 JDs has not been found or verified in the current repository.
- The legacy `ai_pm` task template has not been migrated to canonical `ai_product`; this document does not authorize that migration.
- Current CSV source URLs are unavailable, limiting independent source verification.
- No aggregated, normalized, or summary role-research dataset was found alongside the current role CSV files.

## 10. Related Canonical Documents

The current product documentation is organized by responsibility:

- [Product Overview](../../product-overview.md) — product definition, P0 scope, and global boundaries.
- [Jobs and Recommendations](../../jobs-and-recommendations.md) — canonical jobs and role navigation.
- [Career Trials](../../career-trials.md) — shared Trial structure, timing, interactions, and Trial delegation.
- [Behavior and Evidence](../../behavior-and-evidence.md) — behavior events, evidence interpretation, replay, and uncertainty.
- [Analysis and Growth](../../analysis-and-growth.md) — report, Interest Feedback, Next Mission, and growth semantics.
- [Schemas](../../schemas/) — engineering-facing data contracts.
- [Role-specific Trials](../../trials/) — formal specifications for named role Trials.

If this research synthesis conflicts with an active canonical owner or a role-specific Trial specification, the canonical document governs and the research gap should be recorded before implementation.
