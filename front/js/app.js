(function () {
  'use strict';

  const API_BASE =
    window.location.port === '3001' ? window.location.origin + '/api/v1' : 'http://localhost:3001/api/v1';

  const REC_LABELS = ['优先推荐', '值得体验', '探索方向'];
  const HARD_SKILL_DIMENSIONS = [
    { id: 'user_insight', label: '用户洞察' },
    { id: 'problem_definition', label: '问题定义' },
    { id: 'product_judgment', label: '产品判断' },
    { id: 'ai_feasibility_understanding', label: 'AI 可行性理解' },
    { id: 'prioritization_tradeoffs', label: '优先级与取舍' },
    { id: 'cross_team_push', label: '跨团队推动' },
  ];

  const state = {
    sessionId: null,
    useMock: false,
    jobs: [],
    recommendations: [],
    currentJobId: 'ai_product',
    taskSessionId: null,
    completedJobs: [],
    questionIndex: 0,
    questionAnswers: {},
    persistedQuestions: new Set(),
    quizCompleted: false,
    hasRecommendations: false,
    rolesEntrySource: 'direct',
  };

  const screens = document.querySelectorAll('.screen');
  const analysisProgress = {};
  const QUESTION_SCREENS = ['choice', 'ranking', 'category', 'evidence', 'open'];
  const QUESTION_IDS = QUESTION_SCREENS;

  const analysisConfig = {
    profile: {
      barId: 'analyze1Bar', pctId: 'analyze1Pct', stepId: 'analyze1Step3', buttonId: 'viewRecommendBtn',
      pendingLabel: '正在生成结果…', doneLabel: '查看分析结果', doneStep: '整理优先体验方向',
    },
    task: {
      barId: 'analyze2Bar', pctId: 'analyze2Pct', stepId: 'analyze2Step3', buttonId: 'viewReportBtn',
      pendingLabel: '正在生成报告…', doneLabel: '查看体验报告', doneStep: '生成个性化反馈报告',
    },
  };

  function setAnalysisProgress(key, value) {
    const config = analysisConfig[key];
    const bar = document.getElementById(config.barId);
    const pct = document.getElementById(config.pctId);
    if (bar) bar.style.width = value + '%';
    if (pct) pct.textContent = value + '%';
    analysisProgress[key] = { ...(analysisProgress[key] || {}), value };
  }

  function startAnalysisProgress(key) {
    const config = analysisConfig[key];
    const previous = analysisProgress[key];
    if (previous?.timer) window.clearInterval(previous.timer);
    const button = document.getElementById(config.buttonId);
    const step = document.getElementById(config.stepId);
    if (button) { button.disabled = true; button.textContent = config.pendingLabel; }
    if (step) { step.classList.remove('done'); step.textContent = '○ ' + config.doneStep; }
    setAnalysisProgress(key, 0);
    const timer = window.setInterval(() => {
      const current = analysisProgress[key]?.value || 0;
      if (current >= 92) return;
      const next = Math.min(92, current + (current < 60 ? 4 : 2));
      setAnalysisProgress(key, next);
    }, 110);
    analysisProgress[key] = { value: 0, timer };
  }

  function finishAnalysisProgress(key) {
    const config = analysisConfig[key];
    const currentState = analysisProgress[key];
    if (!currentState) return;
    if (currentState.timer) window.clearInterval(currentState.timer);
    const timer = window.setInterval(() => {
      const current = analysisProgress[key]?.value || 0;
      const next = Math.min(100, current + Math.max(1, Math.ceil((100 - current) / 5)));
      setAnalysisProgress(key, next);
      if (next < 100) return;
      window.clearInterval(timer);
      const step = document.getElementById(config.stepId);
      const button = document.getElementById(config.buttonId);
      if (step) { step.classList.add('done'); step.textContent = config.doneStep; }
      if (button) { button.disabled = false; button.textContent = config.doneLabel; }
      analysisProgress[key] = { value: 100, timer: null };
    }, 70);
    analysisProgress[key] = { ...(analysisProgress[key] || {}), timer };
  }

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
    const navSection = {
      home: 'home',
      roles: 'roles', recommend: 'roles', previewNotice: 'roles', profile1: 'roles', profile2: 'roles', profile3: 'roles', analyze1: 'roles', choice: 'roles', ranking: 'roles', category: 'roles', evidence: 'roles', open: 'roles', analyze2: 'roles',
      growth: 'growth', report: 'growth',
    }[id];
    document.querySelectorAll('[data-nav]').forEach((n) => {
      n.classList.toggle('active', n.dataset.nav === navSection);
    });
    const active = document.getElementById(id);
    if (active) active.scrollTop = 0;
    window.scrollTo(0, 0);
    if (id === 'growth') renderGrowth();
    if (id === 'roles') updateRecommendationBackControl();
    if (id === 'analyze1') startAnalysisProgress('profile');
    if (id === 'analyze2') startAnalysisProgress('task');
    const questionIndex = QUESTION_SCREENS.indexOf(id);
    if (questionIndex >= 0) {
      state.questionIndex = questionIndex;
      restoreQuestionAnswer(questionIndex);
      updateQuestionNav();
    }
  }

  function captureQuestionAnswer(index = state.questionIndex) {
    const id = QUESTION_IDS[index];
    if (id === 'choice') state.questionAnswers[id] = [...document.querySelectorAll('#choice .option.selected')].map((x) => x.textContent.trim())[0] || '';
    if (id === 'ranking') state.questionAnswers[id] = [...document.querySelectorAll('#rankingList .sort-item')].map((x) => x.textContent.trim());
    if (id === 'category') state.questionAnswers[id] = [...document.querySelectorAll('#category .match-item.source.connected')].reduce((result, source) => { result[source.dataset.source] = source.dataset.matchTarget || ''; return result; }, {});
    if (id === 'evidence') state.questionAnswers[id] = [...document.querySelectorAll('#evidence input[type="checkbox"]:checked')].map((x) => x.parentElement.textContent.trim());
    if (id === 'open') state.questionAnswers[id] = document.getElementById('openAnswer')?.value || '';
  }

  function restoreQuestionAnswer(index) {
    const id = QUESTION_IDS[index]; const value = state.questionAnswers[id];
    if (id === 'choice') document.querySelectorAll('#choice .option').forEach((x) => x.classList.toggle('selected', x.textContent.trim() === value));
    if (id === 'ranking' && Array.isArray(value)) { const list = document.getElementById('rankingList'); value.forEach((text) => { const item = [...list.children].find((x) => x.textContent.trim() === text); if (item) list.appendChild(item); }); }
    if (id === 'category' && value && typeof value === 'object') document.querySelectorAll('#category .match-item.source').forEach((source) => { const target = value[source.dataset.source]; source.classList.toggle('connected', Boolean(target)); source.dataset.matchTarget = target || ''; });
    if (id === 'evidence' && Array.isArray(value)) document.querySelectorAll('#evidence input[type="checkbox"]').forEach((x) => { x.checked = value.includes(x.parentElement.textContent.trim()); });
    if (id === 'open' && document.getElementById('openAnswer')) document.getElementById('openAnswer').value = value || '';
  }

  function questionIsValid(index = state.questionIndex) {
    const id = QUESTION_IDS[index]; const value = state.questionAnswers[id];
    if (id === 'choice') return Boolean(value);
    if (id === 'ranking') return Array.isArray(value) && value.length > 0;
    if (id === 'category') return value && typeof value === 'object' && Object.keys(value).length > 0;
    if (id === 'evidence') return Array.isArray(value) && value.length > 0;
    if (id === 'open') return typeof value === 'string' && value.trim().length > 0;
    return false;
  }

  function updateQuestionNav(message = '') {
    const screen = document.getElementById(QUESTION_IDS[state.questionIndex]); if (!screen) return;
    const progress = screen.querySelector('.task-progress');
    if (progress) { progress.firstChild.nodeValue = Math.round(((state.questionIndex + 1) / QUESTION_IDS.length) * 100) + '%'; const bar = progress.querySelector('.bar span'); if (bar) bar.style.width = Math.round(((state.questionIndex + 1) / QUESTION_IDS.length) * 100) + '%'; }
    const prev = screen.querySelector('[data-qnav="prev"]'); const next = screen.querySelector('[data-qnav="next"]');
    if (prev) prev.disabled = state.questionIndex === 0;
    if (next) next.disabled = !questionIsValid();
    const note = screen.querySelector('.question-validation'); if (note) note.textContent = message;
  }

  async function persistQuestion(index) {
    captureQuestionAnswer(index);
    const id = QUESTION_IDS[index];
    if (state.persistedQuestions.has(id) || state.useMock || !state.taskSessionId) return;
    state.persistedQuestions.add(id);
    try { await api('/tasks/' + state.taskSessionId + '/step', { method: 'POST', headers: apiHeaders(true), body: JSON.stringify({ answer: { questionId: id, value: state.questionAnswers[id], demo: true }, events: [] }) }); }
    catch (err) { state.persistedQuestions.delete(id); console.warn('题目保存失败', err); }
  }

  async function navigateQuestion(direction) {
    captureQuestionAnswer();
    if (direction < 0) { go(QUESTION_IDS[state.questionIndex - 1]); return; }
    if (!questionIsValid()) { updateQuestionNav('请先完成这一题，再继续。'); return; }
    await persistQuestion(state.questionIndex);
    if (state.questionIndex === QUESTION_IDS.length - 1) { if (!state.quizCompleted) { state.quizCompleted = true; await submitTaskAndFinish(); } return; }
    go(QUESTION_IDS[state.questionIndex + 1]);
  }

  function updateRecommendationBackControl() {
    const back = document.getElementById('recommendationBack');
    if (back) back.hidden = !(state.hasRecommendations && state.rolesEntrySource === 'recommendation');
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
    taskStatus: 'interactive',
    }));
  }

  function fallbackJobs() {
    return [
      { jobId: 'ai_product', name: 'AI产品', desc: '定义 AI 产品问题与优先级。', highlights: ['产品规划', '用户研究', '数据分析'], taskStatus: 'interactive' },
      { jobId: 'ai_ops', name: 'AI运营', desc: '围绕增长与留存设计运营实验。', highlights: ['数据分析', '用户洞察', '运营策略'], taskStatus: 'interactive' },
      { jobId: 'ai_data_eval', name: 'AI数据与评测', desc: '建立评测体系与数据标准。', highlights: ['评测设计', '数据分析', '标准制定'], taskStatus: 'interactive' },
      { jobId: 'ai_app_dev', name: 'AI应用开发', desc: '将大模型能力集成进产品。', highlights: ['编程能力', '系统设计', '性能优化'], taskStatus: 'interactive' },
      { jobId: 'ai_ui_design', name: 'AIUI设计', desc: '把复杂 AI 能力做成可理解的界面体验。', highlights: ['交互设计', '信息架构', '用户研究'], taskStatus: 'interactive' },
    ];
  }

  function renderRoles() {
    const list = document.getElementById('rolesList');
    if (!list) return;
    list.innerHTML = state.jobs
      .map((job, i) => {
        return (
          '<article class="fan-card">' +
          '<span class="fan-number">' + String(i + 1).padStart(2, '0') + '</span>' +
          '<h2>' + esc(job.name) + '</h2>' +
          '<p>' + esc(job.desc) + '</p>' +
          '<ul class="fan-list">' + job.highlights.map((t) => '<li>' + esc(t) + '</li>').join('') + '</ul>' +
          '<button class="btn" type="button" data-action="start-job" data-job-id="' + esc(job.jobId) + '">' +
          '开始体验 →</button></article>'
        );
      })
      .join('');
  }

  function renderRecommendations() {
    const list = document.getElementById('recommendList');
    if (!list) return;
    const recs = state.recommendations.length ? state.recommendations : state.jobs.slice(0, 3).map((j, i) => ({
      jobId: j.jobId,
      label: REC_LABELS[i] || '推荐',
      reason: '该岗位值得优先体验：' + j.desc,
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

  const GROWTH_RECORDS = [
    {
      jobId: 'ai_product',
      displayName: 'AI产品经理',
      theme: 'purple',
      desc: '擅长拆解问题，喜欢把想法变成可用产品。',
    },
    {
      jobId: 'ai_ops',
      displayName: 'AI产品运营',
      theme: 'green',
      desc: '对内容与用户增长保持敏锐，持续积累方法。',
    },
    {
      jobId: 'ai_app_dev',
      displayName: 'AI解决方案顾问',
      theme: 'coral',
      desc: '期待连接业务与技术，找到更大的影响力。',
    },
  ];

  function growthRecordStatus(done, slot) {
    if (done) return '已完成体验';
    if (slot === 'exploring') return '探索中';
    return '下一站';
  }

  function renderGrowth() {
    const list = document.getElementById('growthList');
    if (!list) return;
    const firstIncomplete = GROWTH_RECORDS.find((r) => !state.completedJobs.includes(r.jobId));
    list.innerHTML = GROWTH_RECORDS
      .map((record) => {
        const job = getJob(record.jobId);
        const jobId = job ? record.jobId : state.jobs[0]?.jobId || record.jobId;
        const done = state.completedJobs.includes(jobId);
        const slot = done ? 'done' : firstIncomplete?.jobId === record.jobId ? 'exploring' : 'next';
        const status = growthRecordStatus(done, slot);
        const action = done ? 'view-report' : 'start-job';
        const actionLabel = done ? '查看报告' : '开始体验';
        return (
          '<article class="growth-record-card growth-record-card--' + record.theme + '">' +
          '<span class="growth-record-accent" aria-hidden="true"></span>' +
          '<h3>' + esc(record.displayName) + '</h3>' +
          '<span class="growth-record-status">' + esc(status) + '</span>' +
          '<p class="growth-record-desc">' + esc(record.desc) + '</p>' +
          '<button class="growth-record-action" type="button" data-action="' + action + '" data-job-id="' + esc(jobId) + '">' +
          esc(actionLabel) + '</button></article>'
        );
      })
      .join('');
  }

  function updateTaskTitles(jobId) {
    const job = getJob(jobId);
    if (!job) return;
    const label = job.name + ' · 微任务体验';
    document.querySelectorAll('.task-job-title').forEach((el) => { el.textContent = label; });
    const rt = document.getElementById('reportJobTitle');
    if (rt) rt.textContent = job.name + ' · 职业体验报告';
    const figmaJobName = document.getElementById('figmaJobName');
    if (figmaJobName) figmaJobName.textContent = job.name === 'AI产品' ? 'AI产品经理' : job.name;
  }

  function patchReport(report) {
    if (!report) return;
    const summary = document.getElementById('reportSummary');
    if (summary && report.comparisonSummary) summary.textContent = report.comparisonSummary;
    const figmaSummary = document.getElementById('figmaReportSummary');
    if (figmaSummary && report.comparisonSummary) figmaSummary.textContent = report.comparisonSummary;
    const next = document.getElementById('reportNextStep');
    if (next && report.boundaryNotice) {
      next.innerHTML = '<b>边界说明：</b>' + esc(report.boundaryNotice);
    }
    const figmaNotice = document.getElementById('figmaReportNotice');
    if (figmaNotice && report.boundaryNotice) figmaNotice.textContent = report.boundaryNotice;
  }

  function buildFigmaReport() {
    const host = document.getElementById('report');
    if (!host || document.getElementById('figmaReport')) return;
    host.insertAdjacentHTML('beforeend', `
      <article class="report figma-report" id="figmaReport" aria-label="职业体验报告">
        <header class="figma-report-nav">
          <div class="figma-brand"><img src="assets/report/brand.png" alt="试途" /><span>首页</span><span class="active">探索</span><span>轨迹</span></div>
          <div class="figma-user"><img src="assets/profile-avatar.jpg" alt="小途头像" /><b>小途</b></div>
        </header>
        <div class="figma-report-body">
          <section class="figma-hero">
            <div class="figma-copy">
              <div class="figma-report-return"><button class="figma-back" data-go="growth" type="button" aria-label="返回">‹</button><span>▮ 你的职业体验报告</span></div>
              <p class="figma-eyebrow">你的体验职业是：</p>
              <h1 id="figmaJobName">AI产品经理</h1>
              <span class="figma-potential">本轮观察摘要</span>
              <p id="figmaReportSummary">本次体验中，你在用户理解和问题拆解相关判断上表现较突出；优先级与取舍仍值得通过更多情境继续验证。</p>
            </div>
            <img class="figma-hero-art" src="assets/report/hero-workspace.png" alt="AI 产品经理工作场景插画" />
          </section>
          <section class="figma-evidence-panel">
            <h2>🌳 你的岗位能力对照：</h2>
            <div class="figma-evidence-copy"><h3>AI判断依据</h3><ul id="figmaReportBasis"><li>你在排序题中优先选择了解决高频核心问题。</li><li>你能从访谈记录中提取关键用户痛点。</li><li>你的开放回答体现出一定的产品思路，但缺少更明确的优先级标准。</li></ul><p id="figmaReportNotice" class="figma-notice"></p></div>
          </section>
          <section class="figma-advice">
            <h2>💡 给你的「学习建议」💡</h2>
            <div class="figma-advice-grid">
              <article class="figma-advice-card coral"><span>♥　你的优势</span><h3>用户理解较强</h3><p>你能从任务中准确识别出用户真正遇到的问题。</p><img src="assets/report/strength-user.png" alt="用户理解插画" /></article>
              <article class="figma-advice-card yellow"><span>◢　你的优势</span><h3>沟通表达清晰</h3><p>你的回答结构较明确，能够解释判断依据。</p><img src="assets/report/strength-communication.png" alt="沟通表达插画" /></article>
              <article class="figma-advice-card blue"><span>●　值得加强</span><h3>优先级决策</h3><p>在资源有限时的取舍逻辑仍可加强。</p><img src="assets/report/improve-priority.png" alt="优先级决策插画" /></article>
              <article class="figma-advice-card green"><span>◆　值得加强</span><h3>证据判断</h3><p>建议练习基于信息证据做决策。</p><img src="assets/report/improve-evidence.png" alt="证据判断插画" /></article>
            </div>
          </section>
          <div class="figma-report-actions"><button class="figma-secondary" data-go="roles" type="button">体验其他岗位</button><button class="figma-primary" data-go="growth" type="button">查看成长轨迹</button></div>
        </div>
      </article>`);
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
      state.hasRecommendations = state.recommendations.length > 0;
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
    if (!state.completedJobs.includes(state.currentJobId)) {
      state.completedJobs.push(state.currentJobId);
      renderGrowth();
    }
    go('analyze2');
    window.setTimeout(() => finishAnalysisProgress('task'), 1500);
  }

  async function loadReport() {
    updateTaskTitles(state.currentJobId);
    // The report screen should always open immediately. Remote report data is
    // optional enhancement and must never block the local experience flow.
    go('report');
    if (!state.useMock && state.sessionId) {
      try {
        const report = await api('/reports/generate', { method: 'POST', headers: apiHeaders() });
        patchReport(report);
      } catch (err) {
        console.warn('报告接口失败', err);
      }
    }
  }

  function bindUi() {
    document.addEventListener('click', (e) => {
      const nav = e.target.closest('[data-go]');
      if (nav && !nav.closest('#drawer')) {
        if (nav.dataset.go === 'roles') state.rolesEntrySource = 'direct';
        go(nav.dataset.go);
      }

      const viewAllRoles = e.target.closest('[data-action="view-all-roles"]');
      if (viewAllRoles) { state.rolesEntrySource = 'recommendation'; go('roles'); }
      const backToRecommend = e.target.closest('[data-action="back-to-recommend"]');
      if (backToRecommend && state.hasRecommendations) { state.rolesEntrySource = 'recommendation'; renderRecommendations(); go('recommend'); }

      const startBtn = e.target.closest('[data-action="start-job"]');
      if (startBtn) startJob(startBtn.dataset.jobId);

      const reportBtn = e.target.closest('[data-action="view-report"]');
      if (reportBtn) {
        state.currentJobId = reportBtn.dataset.jobId;
        loadReport();
      }

      const drawerGo = e.target.closest('#drawer [data-go]');
      if (drawerGo) {
        document.getElementById('drawer').classList.remove('show');
        document.getElementById('drawerShade').classList.remove('show');
        go(drawerGo.dataset.go);
      }
      if (e.target.closest('.question .option, .question .sort-item, .question .match-item, .question input[type="checkbox"]')) {
        window.setTimeout(() => { captureQuestionAnswer(); updateQuestionNav(); }, 0);
      }
      const qnav = e.target.closest('[data-qnav]');
      if (qnav) navigateQuestion(qnav.dataset.qnav === 'prev' ? -1 : 1);
    });

    document.querySelectorAll('[data-nav]').forEach((b) => {
      b.addEventListener('click', () => go(b.dataset.nav));
    });

    document.querySelectorAll('.selectable .chip').forEach((c) => {
      c.addEventListener('click', () => c.classList.toggle('selected'));
    });

    document.querySelectorAll('.single-select .option').forEach((o) => {
      o.addEventListener('click', () => {
        o.parentElement.querySelectorAll('.option').forEach((x) => x.classList.remove('selected'));
        o.classList.add('selected');
      });
    });

    const rankingList = document.getElementById('rankingList');
    let draggedItem = null;
    rankingList.querySelectorAll('.sort-item').forEach((item) => {
      item.addEventListener('dragstart', () => {
        draggedItem = item;
        requestAnimationFrame(() => item.classList.add('dragging'));
      });
      item.addEventListener('dragend', () => {
        item.classList.remove('dragging');
        rankingList.querySelectorAll('.sort-item').forEach((x) => x.classList.remove('drag-over'));
        draggedItem = null;
      });
      item.addEventListener('dragover', (e) => {
        e.preventDefault();
        if (item !== draggedItem) item.classList.add('drag-over');
      });
      item.addEventListener('dragleave', () => item.classList.remove('drag-over'));
      item.addEventListener('drop', (e) => {
        e.preventDefault();
        if (!draggedItem || item === draggedItem) return;
        const before = e.clientY < item.getBoundingClientRect().top + item.offsetHeight / 2;
        rankingList.insertBefore(draggedItem, before ? item : item.nextSibling);
        item.classList.remove('drag-over');
      });
    });

    const matchingBoard = document.getElementById('matchingBoard');
    const matchLines = document.getElementById('matchLines');
    let selectedSource = null;
    const connections = new Map();
    const drawConnections = () => {
      const boardRect = matchingBoard.getBoundingClientRect();
      matchLines.replaceChildren();
      connections.clear();
      document.querySelectorAll('.match-item.source.connected[data-match-target]').forEach((source) => {
        const target = document.querySelector(`.match-item.target[data-target="${CSS.escape(source.dataset.matchTarget)}"]`);
        if (target) connections.set(source, target);
      });
      connections.forEach((target, source) => {
        const a = source.getBoundingClientRect();
        const b = target.getBoundingClientRect();
        const x1 = a.right - boardRect.left;
        const y1 = a.top + a.height / 2 - boardRect.top - 36;
        const x2 = b.left - boardRect.left;
        const y2 = b.top + b.height / 2 - boardRect.top - 36;
        const ns = 'http://www.w3.org/2000/svg';
        const line = document.createElementNS(ns, 'line');
        const c1 = document.createElementNS(ns, 'circle');
        const c2 = document.createElementNS(ns, 'circle');
        line.setAttribute('x1', x1);
        line.setAttribute('y1', y1);
        line.setAttribute('x2', x2);
        line.setAttribute('y2', y2);
        c1.setAttribute('cx', x1);
        c1.setAttribute('cy', y1);
        c1.setAttribute('r', 4);
        c2.setAttribute('cx', x2);
        c2.setAttribute('cy', y2);
        c2.setAttribute('r', 4);
        matchLines.append(line, c1, c2);
      });
    };
    document.querySelectorAll('.match-item.source').forEach((source) => {
      source.addEventListener('click', () => {
        document.querySelectorAll('.match-item.source').forEach((x) => x.classList.remove('selected'));
        selectedSource = source;
        source.classList.add('selected');
      });
    });
    document.querySelectorAll('.match-item.target').forEach((target) => {
      target.addEventListener('click', () => {
        if (!selectedSource) return;
        connections.set(selectedSource, target);
        selectedSource.classList.remove('selected');
        selectedSource.classList.add('connected');
        selectedSource.dataset.matchTarget = target.dataset.target;
        selectedSource = null;
        drawConnections();
      });
    });
    window.addEventListener('resize', drawConnections);

    const drawer = document.getElementById('drawer');
    const shade = document.getElementById('drawerShade');
    const closeDrawer = () => {
      drawer.classList.remove('show');
      shade.classList.remove('show');
    };
    document.getElementById('profileBtn').onclick = () => {
      drawer.classList.add('show');
      shade.classList.add('show');
    };
    document.getElementById('drawerClose').onclick = closeDrawer;
    shade.onclick = closeDrawer;

    const homeScrollCue = document.getElementById('homeScrollCue');
    if (homeScrollCue) {
      homeScrollCue.addEventListener('click', () => {
        const homeEntry = document.getElementById('homeEntry');
        if (homeEntry) homeEntry.scrollIntoView({ behavior: 'smooth', block: 'start' });
      });
    }

    const intro = document.getElementById('intro');
    let introDismissed = false;
    const dismissIntro = (withRoute) => {
      if (introDismissed) return;
      introDismissed = true;
      if (withRoute) {
        intro.classList.add('leaving');
        window.setTimeout(() => intro.classList.add('hide'), 380);
      } else {
        intro.classList.add('hide');
      }
    };
    document.getElementById('introEnterBtn').addEventListener('click', (e) => {
      e.stopPropagation();
      dismissIntro(true);
    });
    intro.addEventListener('click', (e) => {
      if (!e.target.closest('button')) dismissIntro(true);
    });
    document.addEventListener('keydown', (e) => {
      if (!introDismissed && (e.key === 'Enter' || e.key === ' ')) dismissIntro(true);
    });

    document.querySelectorAll('.question-dropdown__head').forEach((head) => {
      head.addEventListener('click', () => {
        head.closest('.question-dropdown')?.classList.toggle('is-collapsed');
      });
    });

    document.getElementById('submitProfileBtn').addEventListener('click', async () => {
      go('analyze1');
      try {
        await saveProfile();
        await loadRecommendations();
      } catch (err) {
        console.warn(err);
      } finally {
        window.setTimeout(() => finishAnalysisProgress('profile'), 500);
      }
    });

    document.getElementById('viewRecommendBtn').addEventListener('click', () => {
      renderRecommendations();
      state.hasRecommendations = true;
      state.rolesEntrySource = 'recommendation';
      go('recommend');
    });

    document.getElementById('openAnswer')?.addEventListener('input', () => { captureQuestionAnswer(4); updateQuestionNav(); });
    document.getElementById('viewReportBtn').addEventListener('click', loadReport);
  }

  function establishFigmaQuestionPanels() {
    document.querySelectorAll(':is(#choice,#ranking,#category,#evidence,#open) > .question').forEach((question) => {
      const variant = question.closest('.screen')?.id;
      if (variant) question.classList.add(`question--${variant === 'ranking' ? 'sort' : variant}`);
      const panel = document.createElement('div');
      panel.className = 'figma-question-panel';
      question.parentNode.insertBefore(panel, question);
      panel.appendChild(question);
    });
  }

  document.addEventListener('DOMContentLoaded', async () => {
    buildFigmaReport();
    state.jobs = fallbackJobs();
    renderRoles();
    renderRecommendations();
    renderGrowth();
    establishFigmaQuestionPanels();
    bindUi();
    await initApi();
    go('home');
    window.scrollTo(0, 0);
    const active = document.querySelector('.screen.active');
    if (active) active.scrollTop = 0;
  });
})();
