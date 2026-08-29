import assert from 'node:assert/strict';
import {
  createTrial,
  selectEvidenceCategory,
  submitFirstJudgment,
  revealConsequence,
  finalizeDecision,
  buildReport,
} from './mvu-core.mjs';

let trial = createTrial('test-session');
assert.equal(trial.jobId, 'ai_product');
assert.equal(trial.stage, 'first_judgment');

trial = submitFirstJudgment(trial, { selectedSlice: 'simplify_import', deferred: ['add_template', 'improve_summary'], reason: 'Import is the first break point.' });
assert.equal(trial.stage, 'evidence_gathering');
assert.throws(() => selectEvidenceCategory(trial, 'funnel_activation', 'segment_retention', 'user_feedback'), /exactly two/);
trial = selectEvidenceCategory(trial, 'funnel_activation');
trial = selectEvidenceCategory(trial, 'segment_retention');
assert.equal(trial.selectedEvidence.length, 2);
assert.equal(trial.stage, 'twist');
trial = revealConsequence(trial);
assert.deepEqual(trial.consequence.facts, ['enterprise retention is stronger', 'personal users drop during import', 'users who complete import value summaries']);
trial = finalizeDecision(trial, { mode: 'revise', selectedSlice: 'simplify_import', deferred: ['add_template', 'improve_summary'], reason: 'Fix the first value break before downstream quality.', tradeoff: 'Less immediate summary polish.', metric: 'first import completion rate', uncertainty: 'Enterprise behavior may differ.' });
assert.equal(trial.stage, 'completed');
assert.ok(trial.events.some((event) => event.sourceStep === 'reconsideration'));
const report = buildReport(trial, { engagement: 'very engaging', willingness: 'yes' });
assert.deepEqual(report.sections.map((section) => section.id), ['summary', 'requirements', 'profile', 'replay', 'interest', 'unknowns', 'nextMission']);
assert.equal(report.taskEvidence.every((item) => item.requirementId), true);
assert.equal(report.interestFeedback.engagement, 'very engaging');
console.log('mvu-core tests passed');
