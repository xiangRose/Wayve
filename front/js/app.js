/**
 * app.js — 数据层 + 后端 API（请勿修改，交互逻辑请写在 ui.js）
 */
(function () {
  'use strict';

  const API_BASE =
    window.location.port === '3000' ? window.location.origin + '/api/v1' : 'http://localhost:3000/api/v1';

  const REC_LABELS = ['优先推荐', '值得体验', '探索方向'];

  const REPORT_CARD_THEMES = [
    { theme: 'pink', tag: '你的优势', icon: 'hearts.svg' },
    { theme: 'yellow', tag: '你的优势', icon: 'stairs.svg' },
    { theme: 'blue', tag: '值得加强', icon: 'seedling.svg' },
    { theme: 'green', tag: '值得加强', icon: 'diamonds.svg' },
  ];

  const REPORT_EMPTY = {
    fitLevel: '待补充',
    headline: '完成微任务后将生成岗位体验结论。',
    summary: '暂无报告摘要，请先完成该岗位的微任务体验。',
    basis: ['暂无判断依据，完成体验后由后端生成。'],
    boundary: '本报告依据你主动提供的经历和本轮任务中的可观察行为生成，用于职业探索，不是招聘结论或永久能力评价。',
    cards: [
      { title: '暂无优势项', desc: '完成体验后展示。' },
      { title: '暂无优势项', desc: '完成体验后展示。' },
      { title: '暂无待加强项', desc: '完成体验后展示。' },
      { title: '暂无待加强项', desc: '完成体验后展示。' },
    ],
  };

  const state = {
    sessionId: null,
    useMock: false,
    jobs: [],
    recommendations: [],
    currentJobId: 'ai_product',
    taskSessionId: null,
    completedJobs: [],
    lastReport: null,
  };

  const screens = document.querySelectorAll('.screen');

  function esc(s) {
    return String(s ?? '')
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;')
      .replace(/"/g, '&quot;');
  }

  function getJob(jobId) {
    return state.jobs.find((j) => j.jobId === jobId);
  }

  function apiHeaders(json) {
    const h = { Accept: 'application/json' };
    if (json) h['Content-Type'] = 'application/json';
    if (state.sessionId) h['X-Session-Id'] = state.sessionId;
    h['X-Demo-Mode'] = 'true';
    return h;
  }

  async function api(path, options) {
    const res = await fetch(API_BASE + path, options);
    if (!res.ok) throw new Error(await res.text() || res.statusText);
    return res.status === 204 ? null : res.json();
  }

  function go(id) {
    screens.forEach((s) => s.classList.toggle('active', s.id === id));
    document.querySelectorAll('[data-nav]').forEach((n) => {
      n.classList.toggle('active', n.dataset.nav === id || (id === 'recommend' && n.dataset.nav === 'roles'));
    });
    const active = document.getElementById(id);
    if (active) active.scrollTop = 0;
    window.scrollTo(0, 0);
  }

  function mapCurrentStatus(label) {
    if (!label) return 'other';
    if (label.includes('在校')) return 'student';
    if (label.includes('应届')) return 'graduate';
    if (label.includes('工作')) return 'employed';
    if (label.includes('转行')) return 'unemployed';
    return 'other';
  }

  function selectedChips(id) {
    const el = document.getElementById(id);
    if (!el) return [];
    return [...el.querySelectorAll('.chip.selected')].map((c) => c.textContent.trim());
  }

  function normalizeJobs(raw) {
    return raw.map((j) => ({
      jobId: j.jobId,
      name: j.name,
      desc: j.definition || '',
      highlights: (j.specificCompetencies || []).slice(0, 3),
      taskStatus: j.taskStatus || 'interactive',
    }));
  }

  function fallbackJobs() {
    return [
      { jobId: 'ai_product', name: 'AI产品', desc: '定义 AI 产品问题与优先级。', highlights: ['产品规划', '用户研究', '数据分析'], taskStatus: 'interactive' },
      { jobId: 'ai_ops', name: 'AI运营', desc: '围绕增长与留存设计运营实验。', highlights: ['数据分析', '用户洞察', '运营策略'], taskStatus: 'interactive' },
      { jobId: 'ai_data_eval', name: 'AI数据与评测', desc: '建立评测体系与数据标准。', highlights: ['评测设计', '数据分析', '标准制定'], taskStatus: 'interactive' },
      { jobId: 'ai_app_dev', name: 'AI应用开发', desc: '将大模型能力集成进产品。', highlights: ['编程能力', '系统设计', '性能优化'], taskStatus: 'interactive' },
      { jobId: 'ai_ui_design', name: 'UI设计', desc: '把复杂 AI 能力做成可理解的界面体验。', highlights: ['交互设计', '信息架构', '用户研究'], taskStatus: 'interactive' },
    ];
  }

  function renderRoles() {
    const list = document.getElementById('rolesList');
    if (!list) return;
    list.innerHTML = state.jobs
      .map((job, i) => (
          '<article class="fan-card">' +
          '<span class="fan-number">' + String(i + 1).padStart(2, '0') + '</span>' +
          '<h2>' + esc(job.name) + '</h2>' +
          '<p>' + esc(job.desc) + '</p>' +
          '<ul class="fan-list">' + job.highlights.map((t) => '<li>' + esc(t) + '</li>').join('') + '</ul>' +
          '<button class="btn" type="button" data-action="start-job" data-job-id="' + esc(job.jobId) + '">' +
          '开始体验 →</button></article>'
        ))
      .join('');
  }

  function renderRecommendations() {
    const list = document.getElementById('recommendList');
    if (!list) return;
    const recs = state.recommendations.length ? state.recommendations : state.jobs.slice(0, 3).map((j, i) => ({
      jobId: j.jobId,
      label: REC_LABELS[i] || '推荐',
      reason: '该岗位适合体验' + j.desc,
      tags: j.highlights,
    }));
    list.innerHTML = recs
      .map((rec, i) => {
        const job = getJob(rec.jobId) || {};
        const tags = rec.tags || job.highlights || [];
        return (
          '<article class="recommend-card">' +
          '<div class="recommend-meta"><span class="recommend-number">' + String(i + 1).padStart(2, '0') + '</span>' +
          '<span class="recommend-label">' + esc(rec.label || REC_LABELS[i] || '推荐') + '</span></div>' +
          '<h2>' + esc(job.name || rec.jobId) + '</h2>' +
          '<p>' + esc(rec.reason || '') + '</p>' +
          '<div class="tags">' + tags.map((t) => '<span class="tag">' + esc(t) + '</span>').join('') + '</div>' +
          '<button class="btn" type="button" data-action="start-job" data-job-id="' + esc(rec.jobId) + '">体验岗位 →</button></article>'
        );
      })
      .join('');
  }

  function renderGrowth() {
    const list = document.getElementById('growthList');
    if (!list) return;
    list.innerHTML = state.jobs
      .map((job) => {
        const done = state.completedJobs.includes(job.jobId);
        return (
          '<article class="track-item"><div><b>' + esc(job.name) + '</b><br>' +
          '<span class="status' + (done ? ' completed' : '') + '">' + (done ? '已完成' : '未体验') + '</span></div>' +
          '<div>' + (done ? '本轮已完成微任务体验' : '尚未开始') + '</div>' +
          '<div>' + (done ? '表现良好' : '等待体验') + '</div>' +
          '<button class="btn small" type="button" data-action="' + (done ? 'view-report' : 'start-job') + '" data-job-id="' + esc(job.jobId) + '">' +
          (done ? '查看报告' : '开始体验') + '</button></article>'
        );
      })
      .join('');
  }

  function updateTaskTitles(jobId) {
    const job = getJob(jobId);
    if (!job) return;
    const label = job.name + ' · 微任务体验';
    document.querySelectorAll('.task-job-title').forEach((el) => { el.textContent = label; });
  }

  function splitInsight(text) {
    const idx = text.indexOf('：');
    if (idx === -1) return { title: text.slice(0, 12), desc: text };
    return { title: text.slice(0, idx), desc: text.slice(idx + 1) };
  }

  function getJobReport(report, jobId) {
    const jobEvidence = report?.taskEvidenceByJob?.[jobId] || {};
    const globalGap = report?.gapAnalysis || {};
    const isTargetJob = globalGap.targetJobId === jobId;
    return {
      jobName: jobEvidence.jobName,
      headline: jobEvidence.headline,
      comparisonSummary: jobEvidence.comparisonSummary || (isTargetJob ? report?.comparisonSummary : null),
      fitLevel: jobEvidence.fitLevel,
      behaviors: jobEvidence.observedBehaviors || [],
      transferableCapabilities: jobEvidence.transferableCapabilities
        || (isTargetJob ? globalGap.transferableCapabilities : null)
        || [],
      gapsToBuild: jobEvidence.gapsToBuild
        || (isTargetJob ? globalGap.gapsToBuild : null)
        || [],
      boundary: report?.boundaryNotice,
    };
  }

  function deriveFitLevel(behaviors, explicit) {
    if (explicit) return explicit;
    const strong = behaviors.filter((b) => b.judgment === '充分支持').length;
    if (strong >= 2) return '高';
    if (strong >= 1 || behaviors.length >= 2) return '中';
    return behaviors.length ? '中' : '待补充';
  }

  function deriveHeadline(jobReport) {
    if (jobReport.headline) return jobReport.headline;
    const behaviors = jobReport.behaviors;
    if (!behaviors.length) return REPORT_EMPTY.headline;
    const comps = [...new Set(behaviors.map((b) => b.competency).filter(Boolean))].slice(0, 2);
    return comps.length ? '你在' + comps.join('和') + '方面表现较好。' : REPORT_EMPTY.headline;
  }

  function buildInsightCards(jobReport) {
    const strengths = [];
    const gaps = [];
    const behaviors = jobReport.behaviors || [];

    behaviors.forEach((b) => {
      const item = { title: b.competency || '能力表现', desc: b.behavior || b.note || '' };
      if (b.judgment === '充分支持') strengths.push(item);
      else gaps.push(item);
    });

    (jobReport.transferableCapabilities || []).forEach((t) => strengths.push(splitInsight(t)));
    (jobReport.gapsToBuild || []).forEach((t) => gaps.push(splitInsight(t)));

    const cards = [];
    for (let i = 0; i < 2; i += 1) {
      cards.push(strengths[i] || REPORT_EMPTY.cards[i]);
    }
    for (let i = 0; i < 2; i += 1) {
      cards.push(gaps[i] || REPORT_EMPTY.cards[i + 2]);
    }
    return cards;
  }

  function renderReport(report) {
    const jobId = state.currentJobId;
    const job = getJob(jobId);
    const jobReport = report ? getJobReport(report, jobId) : null;
    const jobName = job?.name || jobReport?.jobName || '未知岗位';
    const behaviors = jobReport?.behaviors || [];
    const basis = behaviors.length
      ? behaviors.map((b) => b.behavior).filter(Boolean)
      : REPORT_EMPTY.basis;
    const cards = jobReport ? buildInsightCards(jobReport) : REPORT_EMPTY.cards;

    const setText = (id, text) => {
      const el = document.getElementById(id);
      if (el) el.textContent = text || '';
    };

    setText('reportJobName', jobName);
    setText('reportFitLevel', jobReport ? deriveFitLevel(behaviors, jobReport.fitLevel) : REPORT_EMPTY.fitLevel);
    setText('reportHeadline', jobReport ? deriveHeadline(jobReport) : REPORT_EMPTY.headline);
    setText('reportSummary', jobReport?.comparisonSummary || REPORT_EMPTY.summary);
    setText('reportBoundary', jobReport?.boundary || REPORT_EMPTY.boundary);

    const heroPlaceholder = document.getElementById('reportHeroPlaceholder');
    if (heroPlaceholder) heroPlaceholder.textContent = jobName + ' · 报告插图占位';

    const basisEl = document.getElementById('reportBasis');
    if (basisEl) {
      basisEl.innerHTML = basis.map((item) => '<li>' + esc(item) + '</li>').join('');
    }

    const cardsEl = document.getElementById('reportCards');
    if (!cardsEl) return;
    cardsEl.innerHTML = cards
      .map((card, i) => {
        const theme = REPORT_CARD_THEMES[i];
        const tag = i < 2 ? '你的优势' : '值得加强';
        return (
          '<article class="report-v2-card report-v2-card--' + theme.theme + '">' +
          '<span class="report-v2-card-tag"><img src="assets/report/' + theme.icon + '" width="22" height="22" alt="" />' +
          esc(tag) + '</span>' +
          '<h3>' + esc(card.title) + '</h3>' +
          '<p>' + esc(card.desc) + '</p>' +
          '<div class="report-v2-card-illus report-v2-card-illus--placeholder" aria-hidden="true">配图占位</div>' +
          '</article>'
        );
      })
      .join('');
  }

  function patchReport(report) {
    if (!report) return;
    state.lastReport = report;
    renderReport(report);
  }

  async function initApi() {
    try {
      const session = await api('/sessions', { method: 'POST', headers: apiHeaders() });
      state.sessionId = session.sessionId;
      document.getElementById('drawerSessionId').textContent = '会话 ID：' + session.sessionId.slice(0, 8) + '…';
      const jobsRes = await api('/jobs', { headers: apiHeaders() });
      state.jobs = normalizeJobs(jobsRes.jobs || []);
      state.useMock = false;
    } catch (err) {
      console.warn('API 不可用，使用本地数据', err);
      state.jobs = fallbackJobs();
      state.useMock = true;
      document.getElementById('drawerSessionId').textContent = '离线演示（未连接后端）';
    }
    renderRoles();
    renderRecommendations();
    renderGrowth();
  }

  async function saveProfile() {
    if (state.useMock || !state.sessionId) return;
    const statusLabel = document.getElementById('profileStatus').value;
    const background = [
      document.getElementById('profileBackground').value,
      document.getElementById('profileStory').value,
      document.getElementById('profileGoal').value,
    ].filter(Boolean).join('\n');
    await api('/sessions/' + state.sessionId + '/profile', {
      method: 'PUT',
      headers: apiHeaders(true),
      body: JSON.stringify({
        userStage: statusLabel.includes('转行') ? 'career_changer' : 'beginner',
        clarityLevel: 'unknown',
        currentStatus: mapCurrentStatus(statusLabel),
        education: '',
        backgroundText: background,
        teamRoleDescription: selectedChips('experienceChips').join('、'),
        workPreference: [...selectedChips('interestChips'), ...selectedChips('workStyleChips')].join('、'),
        resumeText: document.getElementById('profileSkills').value || '',
      }),
    });
  }

  async function loadRecommendations() {
    if (state.useMock) return;
    try {
      const res = await api('/jobs/recommend', {
        method: 'POST',
        headers: apiHeaders(true),
        body: JSON.stringify({ rejectedJobIds: [] }),
      });
      state.recommendations = (res.recommendations || []).map((r, i) => {
        const job = getJob(r.jobId);
        return {
          jobId: r.jobId,
          label: REC_LABELS[i] || '推荐',
          reason: r.reason || '',
          tags: job ? job.highlights : [],
        };
      });
      renderRecommendations();
    } catch (err) {
      console.warn('推荐接口失败', err);
    }
  }

  async function startJob(jobId) {
    const job = getJob(jobId);
    if (!job) return;
    state.currentJobId = jobId;
    updateTaskTitles(jobId);

    if (jobId !== 'ai_product') {
      const name = document.getElementById('previewJobName');
      const desc = document.getElementById('previewJobDesc');
      if (name) name.textContent = job.name + ' · 预览';
      if (desc) desc.textContent = job.desc;
      go('previewNotice');
      return;
    }
    if (window.MvuUI) {
      const name = document.getElementById('previewJobName');
      const desc = document.getElementById('previewJobDesc');
      const copy = document.getElementById('previewJobCopy');
      const launch = document.getElementById('launchMvuBtn');
      if (name) name.textContent = 'AI Product · 体验预览';
      if (desc) desc.textContent = '用 3–5 分钟，在真实的激活与留存问题中做一次产品优先级判断。';
      if (copy) copy.textContent = '你将先做一个临时决策，再选择两类证据，观察新的事实如何改变取舍，最后保留或修订并提交验证计划。';
      if (launch) { launch.hidden = false; launch.onclick = () => window.MvuUI.start(state.sessionId); }
      go('previewNotice');
      return;
    }

    if (!state.useMock && state.sessionId) {
      try {
        const task = await api('/tasks', {
          method: 'POST',
          headers: apiHeaders(true),
          body: JSON.stringify({ jobId, scaffoldType: 'career_changer' }),
        });
        state.taskSessionId = task.taskSessionId;
      } catch (err) {
        console.warn('创建任务会话失败，继续演示流程', err);
      }
    }
    go('choice');
  }

  async function submitTaskAndFinish() {
    if (!state.useMock && state.taskSessionId) {
      try {
        const answer = { text: document.getElementById('openAnswer').value.trim(), demo: true };
        await api('/tasks/' + state.taskSessionId + '/step', {
          method: 'POST',
          headers: apiHeaders(true),
          body: JSON.stringify({ answer, events: [] }),
        });
      } catch (err) {
        console.warn('任务提交失败', err);
      }
    }
    if (!state.completedJobs.includes(state.currentJobId)) {
      state.completedJobs.push(state.currentJobId);
      renderGrowth();
    }
    go('analyze2');
  }

  async function loadReport() {
    updateTaskTitles(state.currentJobId);
    if (!state.useMock && state.sessionId) {
      try {
        const report = await api('/reports/generate', { method: 'POST', headers: apiHeaders() });
        patchReport(report);
      } catch (err) {
        console.warn('报告接口失败', err);
        renderReport(state.lastReport);
      }
    } else {
      renderReport(state.lastReport);
    }
    go('report');
  }

  function bindApiEvents() {
    document.addEventListener('click', (e) => {
      const startBtn = e.target.closest('[data-action="start-job"]');
      if (startBtn) startJob(startBtn.dataset.jobId);

      const reportBtn = e.target.closest('[data-action="view-report"]');
      if (reportBtn) {
        state.currentJobId = reportBtn.dataset.jobId;
        loadReport();
      }
    });

    document.getElementById('submitProfileBtn').addEventListener('click', async () => {
      go('analyze1');
      try {
        await saveProfile();
        await loadRecommendations();
        document.getElementById('analyze1Step3').textContent = '✓ 匹配适合的岗位方向';
        document.getElementById('analyze1Bar').style.width = '100%';
        document.getElementById('analyze1Pct').textContent = '100%';
      } catch (err) {
        console.warn(err);
      }
    });

    document.getElementById('viewRecommendBtn').addEventListener('click', () => {
      renderRecommendations();
      go('recommend');
    });

    document.getElementById('submitTaskBtn').addEventListener('click', submitTaskAndFinish);
    document.getElementById('viewReportBtn').addEventListener('click', loadReport);
  }

  window.CareerApp = {
    go,
    startJob,
    loadReport,
    getJobs: () => state.jobs.slice(),
    getCurrentJobId: () => state.currentJobId,
    isOffline: () => state.useMock,
  };

  document.addEventListener('DOMContentLoaded', async () => {
    state.jobs = fallbackJobs();
    renderRoles();
    renderRecommendations();
    renderGrowth();
    bindApiEvents();
    await initApi();
    go('home');
    window.scrollTo(0, 0);
    const active = document.querySelector('.screen.active');
    if (active) active.scrollTop = 0;
  });
})();
