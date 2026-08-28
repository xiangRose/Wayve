const FACTS = ['enterprise retention is stronger', 'personal users drop during import', 'users who complete import value summaries'];
const categories = ['funnel_activation', 'segment_retention', 'user_feedback', 'engineering_feasibility'];

export function createTrial(sessionId) {
  return { sessionId, jobId: 'ai_product', stage: 'first_judgment', selectedEvidence: [], events: [], initial: null, consequence: null, final: null };
}
function event(sourceStep, actionType, payload) { return { eventId: crypto.randomUUID(), sourceStep, actionType, payload, createdAt: new Date().toISOString() }; }
export function submitFirstJudgment(trial, answer) {
  if (!answer?.selectedSlice || !Array.isArray(answer.deferred) || answer.deferred.length !== 2) throw new Error('a slice and two deferred options are required');
  return { ...trial, stage: 'evidence_gathering', initial: answer, events: [...trial.events, event('first_judgment', 'priority_committed', answer), event('first_judgment', 'option_deferred', { deferred: answer.deferred })] };
}
export function selectEvidenceCategory(trial, ...ids) {
  const next = [...trial.selectedEvidence, ...ids].filter((id, i, all) => all.indexOf(id) === i);
  if (next.some((id) => !categories.includes(id))) throw new Error('unknown evidence category');
  if (next.length > 2) throw new Error('exactly two evidence categories may be selected');
  return { ...trial, selectedEvidence: next, stage: next.length === 2 ? 'twist' : 'evidence_gathering', events: ids.length ? [...trial.events, event('evidence_gathering', 'evidence_categories_selected', { categories: ids })] : trial.events };
}
export function revealConsequence(trial) {
  if (trial.selectedEvidence.length !== 2) throw new Error('select exactly two categories first');
  return { ...trial, stage: 'reconsideration', consequence: { facts: FACTS, implication: `Review how ${trial.initial.selectedSlice} addresses the first-value break.` }, events: [...trial.events, event('twist', 'consequence_revealed', { facts: FACTS })] };
}
export function finalizeDecision(trial, decision) {
  if (!['retain', 'revise'].includes(decision?.mode)) throw new Error('mode must be retain or revise');
  if (!decision.selectedSlice || !decision.reason || !decision.tradeoff || !decision.metric || !decision.uncertainty) throw new Error('final decision requires reason, tradeoff, metric, and uncertainty');
  const step = decision.mode === 'retain' ? 'reconsideration' : 'reconsideration';
  const events = [...trial.events, event(step, decision.mode === 'revise' ? 'priority_revised' : 'reconsideration_recorded', decision), event('final_decision', 'final_decision_submitted', decision)];
  return { ...trial, stage: 'completed', final: decision, events };
}
export function buildReport(trial, interestFeedback) {
  const claims = [
    { requirementId: 'constrained_prioritization', sourceEventIds: trial.events.filter((e) => e.actionType === 'priority_committed').map((e) => e.eventId), sourceStep: 'first_judgment', supports: 'May support a bounded product priority under this delivery constraint.', limits: 'Does not prove general product competence or fit.' },
    { requirementId: 'evidence_judgment', sourceEventIds: trial.events.filter((e) => e.sourceStep === 'evidence_gathering').map((e) => e.eventId), sourceStep: 'evidence_gathering', supports: 'May support selecting relevant supplied signals for this decision.', limits: 'Does not prove broad analytical ability.' },
    { requirementId: 'hypothesis_revision', sourceEventIds: trial.events.filter((e) => e.actionType.includes('reconsideration') || e.actionType === 'priority_revised').map((e) => e.eventId), sourceStep: 'reconsideration', supports: 'May support retaining or revising a judgment after new information.', limits: 'Does not prove stable adaptability.' },
    { requirementId: 'testable_next_step', sourceEventIds: trial.events.filter((e) => e.actionType === 'final_decision_submitted').map((e) => e.eventId), sourceStep: 'final_decision', supports: 'May support naming a bounded validation test and uncertainty.', limits: 'Does not prove outcome prediction.' },
  ];
  return { sections: [{ id: 'summary' }, { id: 'requirements' }, { id: 'profile' }, { id: 'replay' }, { id: 'interest' }, { id: 'unknowns' }, { id: 'nextMission' }], taskEvidence: claims, interestFeedback, evidenceReplay: trial.events, nextMission: { status: 'Pending', unknown: 'Impact on enterprise retention remains untested.' } };
}
