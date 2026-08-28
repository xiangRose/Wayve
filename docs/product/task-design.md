# Task Design

Status: Hackathon P0 Frozen
Source of Truth: Feishu
Repository Snapshot Updated: 2026-08-28

Career trials should feel like a short slice of real work. They are not exams, personality tests, or self-assessment forms.

## P0 Task Container

All role trials share one container:

Scenario -> First Judgment -> Evidence Gathering / Information Choice -> Twist -> Reconsideration -> Final Decision

The container records user choices, order changes, matches, and short reasons as Behavior Events. These events later become Evidence only when they can be tied to a role requirement and a specific source step.

## Experience Constraints

- Target completion time: 3-5 minutes.
- Interaction should be lightweight and observable.
- Each step should create evidence or context for evidence.
- The user should feel they are solving a work situation, not answering a quiz.
- The task should avoid requiring specialized domain knowledge outside the scenario.

Preferred interactions:

- Single choice with short reason
- Multi-select with short reason
- Sorting / prioritization
- Matching / connecting
- Selecting evidence to inspect
- Short structured summary

Avoid:

- Long text Q&A
- Knowledge exams
- Personality tests
- Self-rated ability questionnaires
- Generic "what are your strengths" prompts

## Hero Role Case: `ai_product`

The Hero Role trial is based on AI product prioritization under incomplete evidence and delivery constraints.

### Scenario

The user joins an AI meeting assistant team. Registrations are rising, first successful summary generation is lower than expected, and seven-day retention is declining. Users say the summary is useful after setup, but the first-use process feels difficult.

The user's task is to decide the most valuable near-term product improvement with limited engineering capacity.

### First Judgment

Ask the user to choose the initial priority:

- First-use completion path
- Summary quality
- Feature discovery / long-term value

The user gives a short reason. Evidence potential:

- Whether the user distinguishes activation from retention.
- Whether the user identifies the product bottleneck before over-optimizing quality.
- Whether the user states uncertainty and asks for more evidence.

### Evidence Gathering / Information Choice

Offer limited information choices:

- Registration -> import -> first generation -> second use funnel
- User segment retention
- User feedback quotes
- Engineering effort estimates

The user chooses one or two. Evidence potential:

- Whether the user seeks data that can validate or challenge the initial hypothesis.
- Whether the user considers segmentation instead of relying only on aggregate metrics.
- Whether the user balances user impact and delivery feasibility.

### Twist

Reveal new information:

- Enterprise users retain better than personal users.
- Most personal users drop at import / connection.
- Users who complete import often find summaries useful.

Evidence potential:

- Whether the user updates judgment after new evidence.
- Whether the user avoids treating summary quality as the only problem.
- Whether the user distinguishes the strongest observed signal from remaining unknowns.

### Reconsideration

Ask the user whether and how their priority changes. The answer should be a short explanation or structured choice plus reason.

Evidence potential:

- Hypothesis revision.
- Evidence weighting.
- Boundary awareness around what the new facts do and do not prove.

### Final Decision

Ask the user to pick one near-term improvement:

- Simplify import flow
- Add summary templates
- Improve summary quality

The user must state trade-off, validation metrics, and remaining uncertainty.

Evidence potential:

- Product prioritization.
- Trade-off reasoning.
- Metric selection.
- Communication clarity.

## Evidence Instrumentation

Each step should emit Behavior Events with:

- `stepId`
- `stepType`
- `actionType`
- `selectedOptions`
- `rankedOptions`
- `matchedPairs`
- `shortReason`
- `timeSpentMs`
- `helpUsed`
- `changedFromPrevious`

Free text should be short and used to explain a choice, not as the primary task format.

## Reuse for Other Roles

The same task container supports all roles by changing scenario content, role requirements, and option sets.

| jobId | Scenario Focus | Container Reuse |
| --- | --- | --- |
| `ai_ops` | Activation or retention drop in an AI product. | First judgment chooses operational hypothesis; evidence step chooses funnel, segment, channel, or feedback data; final decision selects an experiment. |
| `ai_data_eval` | AI output quality varies across cases. | First judgment chooses likely error pattern; evidence step selects examples or rubric criteria; twist reveals contradiction; final decision updates evaluation rubric. |
| `ai_app_dev` | A prototype must connect AI capability to user workflow under constraints. | First judgment chooses architecture or flow priority; evidence step selects API, model behavior, or edge-case info; final decision picks implementation plan and risk. |
| `ai_ui_design` | Users struggle to understand or control AI output. | First judgment identifies interaction barrier; evidence step selects user goals, click behavior, or state feedback; final decision chooses information architecture and feedback states. |

## Copy Boundary

Task copy may say:

- "This task can produce evidence about..."
- "This behavior may support..."
- "This was not observed in this task..."

Task copy must not say:

- "You are good at..."
- "You are bad at..."
- "This role is the best fit..."
- "You are naturally suited..."
