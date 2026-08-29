(function () {
  'use strict';

  const API_BASE =
    window.location.port === '3001' ? window.location.origin + '/api/v1' : 'http://localhost:3001/api/v1';

  const REC_LABELS = ['优先推荐', '值得体验', '探索方向'];

  const API_JOB_TO_BACKEND = {
    ai_product: 'ai_pm',
    ai_ui_design: 'ai_ux',
    ai_ops: 'ai_operator',
    ai_data_eval: 'ai_researcher',
    ai_app_dev: 'ai_consultant',
  };
  const API_JOB_TO_FRONT = {
    ai_pm: 'ai_product',
    ai_ux: 'ai_ui_design',
    ai_operator: 'ai_ops',
    ai_researcher: 'ai_data_eval',
    ai_consultant: 'ai_app_dev',
  };
  const MICROTASK_SET_IDS = ['A', 'B', 'C', 'D'];
  const RADAR_SCORE_MAP = { 2: 40, 3: 60, 4: 80, 5: 100 };

  const state = {
    sessionId: null,
    useMock: false,
    jobs: [],
    recommendations: [],
    currentJobId: 'ai_product',
    taskSessionId: null,
    microtaskStep: 1,
    microtaskSetId: 'A',
    microtaskSelectedOption: null,
    microtaskBank: null,
    microtaskScores: null,
    microtaskRadar: null,
    localMicrotaskAnswers: [],
    microtaskStepAnswers: {},
    microtaskMaxSubmittedStep: 0,
    completedJobs: [],
    sceneStep: 0,
    sceneScripts: null,
    scenePanelPhase: 0,
    scenePanelMaxPhase: 0,
    sceneDraft: { selectedOptionId: null, customText: '' },
  };

  const ROLE_DESK_ITEMS = [
    {
      src: 'desk-base.png',
      left: 50,
      top: 25.77,
      width: 77.65,
      height: 108.03,
      transform: 'translateX(-50%) rotate(0.87deg)',
      z: 1,
    },
    {
      src: 'item-03.png',
      left: 51.04,
      top: 33.88,
      width: 25.63,
      height: 40.59,
      transform: 'rotate(180deg) scaleY(-1)',
      z: 2,
    },
    {
      src: 'item-06.png',
      left: 41.18,
      top: 48.29,
      width: 10.63,
      height: 16.83,
      z: 3,
    },
    {
      src: 'item-02.png',
      left: 69.79,
      top: 60.29,
      width: 8.96,
      height: 14.2,
      z: 4,
    },
    {
      src: 'item-04.png',
      left: 46.94,
      top: 58.64,
      width: 14.85,
      height: 21.99,
      transform: 'rotate(164.43deg) scaleY(-1)',
      z: 5,
    },
    {
      src: 'item-07.png',
      left: 30.9,
      top: 64.91,
      width: 19.08,
      height: 27.55,
      transform: 'rotate(6.8deg)',
      z: 6,
    },
    {
      src: 'item-08.png',
      left: 19.79,
      top: 45.82,
      width: 24.07,
      height: 38.13,
      transform: 'rotate(-152.44deg)',
      z: 7,
    },
  ];

  const ROLE_EXPLORE_NODES = [
    {
      jobId: 'ai_ui_design',
      prompt: '你想体验UI设计吗？',
      avatar: 'avatar-ui.png',
      bubble: 'left',
      left: 17.15,
      top: 23.55,
      bubbleClass: 'roles-explore-bubble--ui',
    },
    {
      jobId: 'ai_product',
      prompt: '你想体验产品经理吗？',
      avatar: 'avatar-pm.png',
      bubble: 'left',
      left: 37.99,
      top: 33.88,
      bubbleClass: 'roles-explore-bubble--pm',
    },
    {
      jobId: 'ai_app_dev',
      prompt: '你想体验应用开发吗？',
      avatar: 'avatar-dev.png',
      bubble: 'left',
      left: 59.44,
      top: 22.77,
      bubbleClass: 'roles-explore-bubble--dev',
    },
    {
      jobId: 'ai_data_eval',
      prompt: '你想体验AI数据评测吗？',
      avatar: 'avatar-data.png',
      bubble: 'right',
      left: 79.24,
      top: 37.73,
      bubbleClass: 'roles-explore-bubble--data',
    },
    {
      jobId: 'ai_ops',
      prompt: '你想体验AI产品运营吗？',
      avatar: 'avatar-ops.png',
      bubble: 'right',
      left: 7.85,
      top: 49.61,
      bubbleClass: 'roles-explore-bubble--ops',
    },
  ];

  const SCENE_PANEL_PHASES = [
    { id: 'context', label: '情境' },
    { id: 'question', label: '问题' },
    { id: 'answer', label: '判断' },
  ];
  const SCENE_S1_IDS = {
    ai_product: 'PRODUCT_S1',
    ai_ui_design: 'UI_S1',
    ai_ops: 'OPS_S1',
    ai_data_eval: 'DATA_S1',
    ai_app_dev: 'DEV_S1',
  };
  const SCENE_S2_IDS = {
    ai_product: 'PRODUCT_S2',
    ai_ui_design: 'UI_S2',
    ai_ops: 'OPS_S2',
    ai_data_eval: 'DATA_S2',
    ai_app_dev: 'DEV_S2',
  };
  const SCENE_S3_IDS = {
    ai_product: 'PRODUCT_S3',
    ai_ui_design: 'UI_S3',
    ai_ops: 'OPS_S3',
    ai_data_eval: 'DATA_S3',
    ai_app_dev: 'DEV_S3',
  };

  const SCENE_STEPS = [
    {
      badge: '项目会议室',
      theme: 'green',
      art: 'assets/scenes/scene-meeting.png',
      scripted: true,
      sceneIdForJob: (jobId) => SCENE_S1_IDS[jobId],
    },
    {
      badge: '客户沟通',
      theme: 'purple',
      art: 'assets/scenes/scene-client.png',
      scripted: true,
      sceneIdForJob: (jobId) => SCENE_S2_IDS[jobId],
    },
    {
      badge: '发布现场',
      theme: 'orange',
      art: 'assets/scenes/scene-release.png',
      scripted: true,
      sceneIdForJob: (jobId) => SCENE_S3_IDS[jobId],
    },
  ];

  const screens = document.querySelectorAll('.screen');
  const analysisProgress = {};

  const analysisConfig = {
    profile: {
      barId: 'analyze1Bar', pctId: 'analyze1Pct', stepId: 'analyze1Step3', buttonId: 'viewRecommendBtn',
      pendingLabel: '正在生成结果…', doneLabel: '查看分析结果', doneStep: '匹配适合的岗位方向',
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
    if (state.useMock) h['X-Demo-Mode'] = 'true';
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
      roles: 'roles', recommend: 'roles', previewNotice: 'roles', profile1: 'roles', profile2: 'roles', profile3: 'roles', analyze1: 'roles', choice: 'roles', sceneSim: 'roles', analyze2: 'roles',
      growth: 'growth', report: 'growth',
    }[id];
    document.querySelectorAll('[data-nav]').forEach((n) => {
      n.classList.toggle('active', n.dataset.nav === navSection);
    });
    const active = document.getElementById(id);
    if (active) active.scrollTop = 0;
    window.scrollTo(0, 0);
    if (id === 'growth') renderGrowth();
    if (id === 'roles') renderRoles();
    if (id === 'sceneSim') renderSceneSim();
    if (id === 'analyze1') startAnalysisProgress('profile');
    if (id === 'analyze2') startAnalysisProgress('task');
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
      jobId: API_JOB_TO_FRONT[j.jobId] || j.jobId,
      name: j.name,
      desc: j.definition || '',
      highlights: (j.specificCompetencies || []).slice(0, 3),
      taskStatus: j.taskStatus || 'preview_only',
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

  function renderRolesDeskItem(item) {
    const parts = [
      'left:' + item.left + '%',
      'top:' + item.top + '%',
      'width:' + item.width + '%',
    ];
    if (item.height) parts.push('height:' + item.height + '%');
    if (item.z) parts.push('z-index:' + item.z);
    if (item.transform) parts.push('transform:' + item.transform);
    return (
      '<img class="roles-explore-desk-item" src="assets/roles/' + item.src + '" alt="" style="' +
      parts.join(';') + '" />'
    );
  }

  function renderRoles() {
    const list = document.getElementById('rolesList');
    if (!list) return;

    const nodesHtml = ROLE_EXPLORE_NODES.map((node) => {
      const bubbleSvg = node.bubble === 'right' ? 'bubble-right.svg' : 'bubble-left.svg';
      return (
        '<button class="roles-explore-node" type="button" data-action="start-job" data-job-id="' +
        esc(node.jobId) + '" style="left:' + node.left + '%;top:' + node.top + '%" aria-label="' +
        esc(node.prompt) + '">' +
        '<span class="roles-explore-bubble ' + esc(node.bubbleClass) + '">' +
        '<img class="roles-explore-bubble-bg" src="assets/roles/' + bubbleSvg + '" alt="" />' +
        '<span class="roles-explore-bubble-text">' + esc(node.prompt) + '</span></span>' +
        '<span class="roles-explore-avatar"><img src="assets/roles/' + node.avatar + '" alt="" /></span></button>'
      );
    }).join('');

    const deskHtml = ROLE_DESK_ITEMS.map(renderRolesDeskItem).join('');

    list.innerHTML =
      '<div class="roles-explore-floor-wrap" aria-hidden="true">' +
      '<img class="roles-explore-floor" src="assets/roles/floor.png" alt="" /></div>' +
      '<div class="roles-explore-desk-stage" aria-hidden="true">' + deskHtml + '</div>' +
      nodesHtml;
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

  function pickMicrotaskSetId() {
    return MICROTASK_SET_IDS[Math.floor(Math.random() * MICROTASK_SET_IDS.length)];
  }

  async function loadMicrotaskScores() {
    if (state.microtaskScores) return state.microtaskScores;
    const res = await fetch('data/microtask-scores.json');
    if (!res.ok) throw new Error('微任务评分表加载失败');
    state.microtaskScores = await res.json();
    return state.microtaskScores;
  }

  function radarScore(raw) {
    return RADAR_SCORE_MAP[raw] || 0;
  }

  function buildLocalRadar() {
    const backendId = API_JOB_TO_BACKEND[state.currentJobId] || state.currentJobId;
    const scoresBank = state.microtaskScores;
    if (!scoresBank?.jobs?.[backendId]) return null;
    const set = scoresBank.jobs[backendId].sets?.[state.microtaskSetId] || [];
    const dimensions = state.localMicrotaskAnswers.map((answer, i) => {
      const def = set[i] || {};
      const option = (def.options || []).find((o) => o.id === answer.selectedOptionId);
      const raw = option ? option.score : 0;
      return {
        name: def.dimension || '维度' + (i + 1),
        score: radarScore(raw),
        rawScore: raw,
      };
    });
    return {
      jobId: backendId,
      setId: state.microtaskSetId,
      dimensions,
      labels: dimensions.map((d) => d.name),
      scores: dimensions.map((d) => d.score),
    };
  }

  function renderReportRadar(taskRadar) {
    const svg = document.getElementById('reportRadarSvg');
    if (!svg || !taskRadar) return;
    const labels = taskRadar.labels || [];
    const scores = taskRadar.scores || [];
    if (!labels.length) {
      svg.replaceChildren();
      return;
    }

    const cx = 185;
    const cy = 168;
    const maxR = 118;
    const n = labels.length;
    const angles = labels.map((_, i) => -Math.PI / 2 + (2 * Math.PI * i) / n);
    const pointAt = (score, i) => {
      const r = (Math.max(0, score) / 100) * maxR;
      return { x: cx + r * Math.cos(angles[i]), y: cy + r * Math.sin(angles[i]) };
    };
    const gridLevels = [25, 50, 75, 100];
    const ns = 'http://www.w3.org/2000/svg';

    const mk = (tag, attrs) => {
      const el = document.createElementNS(ns, tag);
      Object.entries(attrs).forEach(([k, v]) => el.setAttribute(k, String(v)));
      return el;
    };

    svg.replaceChildren();

    gridLevels.forEach((level) => {
      const pts = angles
        .map((_, i) => {
          const r = (level / 100) * maxR;
          return cx + r * Math.cos(angles[i]) + ',' + (cy + r * Math.sin(angles[i]));
        })
        .join(' ');
      svg.appendChild(
        mk('polygon', {
          points: pts,
          fill: 'none',
          stroke: '#c5d9b8',
          'stroke-width': 1,
        })
      );
    });

    angles.forEach((a) => {
      svg.appendChild(
        mk('line', {
          x1: cx,
          y1: cy,
          x2: cx + maxR * Math.cos(a),
          y2: cy + maxR * Math.sin(a),
          stroke: '#c5d9b8',
          'stroke-width': 1,
        })
      );
    });

    const polyPts = scores.map((s, i) => {
      const p = pointAt(s, i);
      return p.x + ',' + p.y;
    }).join(' ');
    svg.appendChild(
      mk('polygon', {
        points: polyPts,
        fill: 'rgba(170,195,147,0.35)',
        stroke: '#779d56',
        'stroke-width': 2.5,
      })
    );

    labels.forEach((label, i) => {
      const lr = maxR + 22;
      const x = cx + lr * Math.cos(angles[i]);
      const y = cy + lr * Math.sin(angles[i]);
      const text = mk('text', {
        x,
        y,
        fill: '#39571f',
        'font-size': 11,
        'font-family': 'PingFang SC, Microsoft YaHei, sans-serif',
        'text-anchor': 'middle',
        'dominant-baseline': 'middle',
      });
      text.textContent = label;
      svg.appendChild(text);
    });
  }

  function patchAdviceFromRadar(taskRadar) {
    const dims = (taskRadar?.dimensions || []).slice();
    if (dims.length < 2) return;
    const cards = document.querySelectorAll('.figma-advice-card h3');
    const texts = document.querySelectorAll('.figma-advice-card p');
    const pick = (dim) =>
      '在「' + dim.name + '」相关情境中，可多留意自己通常先确认哪类信息、再采取什么行动。';
    const sorted = dims.slice().sort((a, b) => b.score - a.score);
    const strengths = sorted.slice(0, 2);
    const others = sorted.slice(-2).reverse();
    if (cards[0] && strengths[0]) cards[0].textContent = strengths[0].name + '相关情境';
    if (texts[0] && strengths[0]) texts[0].textContent = pick(strengths[0]);
    if (cards[1] && strengths[1]) {
      cards[1].textContent = strengths[1].name + '相关取舍';
      if (texts[1]) texts[1].textContent = pick(strengths[1]);
    }
    if (cards[2] && others[0]) {
      cards[2].textContent = others[0].name + '可继续观察';
      if (texts[2]) texts[2].textContent = '遇到类似问题时，可尝试换一种信息收集或对齐方式，再对比结果。';
    }
    if (cards[3] && others[1]) {
      cards[3].textContent = others[1].name + '值得多练';
      if (texts[3]) texts[3].textContent = '用一个小案例复盘：当时还缺哪条事实，若补上会如何选择。';
    }
  }

  function patchLearningAdvice(adviceList) {
    if (!adviceList || !adviceList.length) return;
    const cards = document.querySelectorAll('.figma-advice-card');
    adviceList.slice(0, 4).forEach((item, i) => {
      const card = cards[i];
      if (!card) return;
      const title = card.querySelector('h3');
      const text = card.querySelector('p');
      const tag = card.querySelector('span');
      if (title && item.title) title.textContent = item.title;
      if (text && item.description) text.textContent = item.description;
      if (tag && item.type) {
        tag.textContent = item.type === 'strength' ? '♥　你的优势' : '●　值得加强';
      }
    });
  }

  function clipText(text, max) {
    if (!text) return '';
    const t = String(text).trim();
    return t.length <= max ? t : t.slice(0, max - 1) + '…';
  }

  function sanitizeJudgmentLine(line) {
    if (!line) return '';
    let t = String(line).trim();
    t = t.replace(/^【undefined】/, '');
    t = t.replace(/^undefined[｜|]?/, '');
    t = t.replace(/【undefined】/g, '');
    return t.trim();
  }

  function formatLocalJudgmentLine(q, row) {
    const selected = (q.options || []).find((o) => o.id === row.selectedOptionId);
    const label = selected?.label || row.selectedOptionId || '';
    const others = (q.options || []).filter((o) => o.id !== row.selectedOptionId).map((o) => o.label);
    const who = [q.speaker, q.speakerRole, q.time].filter(Boolean).join('｜');
    let contrast = '这一选择体现了你在该题里优先关注的判断线索。';
    if (others.length === 1) {
      contrast = '相比「' + clipText(others[0], 40) + '」等方向，你更先把注意力放在当前选项所代表的路径上。';
    } else if (others.length >= 2) {
      contrast =
        '相比「' + clipText(others[0], 40) + '」「' + clipText(others[1], 40) + '」等备选，你更先把注意力放在当前选项所代表的路径上。';
    }
    const tag = q.dimension ? '【' + q.dimension + '】' : '';
    const whoPrefix = who ? who : '题目情境';
    return (
      tag + whoPrefix +
      '在「' + clipText(q.message, 72) + '」的情境里，面对「' + clipText(q.prompt, 72) + '」，你选择了「' + clipText(label, 56) + '」。' +
      contrast
    );
  }

  async function buildLocalJudgmentBasis() {
    const bank = await loadMicrotaskBank();
    const backendId = API_JOB_TO_BACKEND[state.currentJobId] || state.currentJobId;
    const questions = bank.jobs?.[backendId]?.sets?.[state.microtaskSetId]?.questions || [];
    if (!questions.length || !state.localMicrotaskAnswers.length) return [];
    return state.localMicrotaskAnswers
      .map((row, i) => {
        const q = questions[i] || questions[row.step - 1];
        return q ? formatLocalJudgmentLine(q, row) : null;
      })
      .filter(Boolean);
  }

  async function buildMicrotaskChoiceSignalsForReport() {
    const bank = await loadMicrotaskBank();
    const backendId = API_JOB_TO_BACKEND[state.currentJobId] || state.currentJobId;
    const questions = bank.jobs?.[backendId]?.sets?.[state.microtaskSetId]?.questions || [];
    if (!questions.length || !state.localMicrotaskAnswers.length) return [];
    return state.localMicrotaskAnswers.map((row, i) => {
      const q = questions[i] || questions[row.step - 1];
      if (!q) return null;
      const selected = (q.options || []).find((o) => o.id === row.selectedOptionId);
      const others = (q.options || []).filter((o) => o.id !== row.selectedOptionId).map((o) => o.label);
      return {
        step: row.step || i + 1,
        dimension: q.dimension || (state.microtaskRadar?.labels?.[i] || state.microtaskRadar?.labels?.[row.step - 1]) || '',
        time: q.time,
        speaker: q.speaker,
        speakerRole: q.speakerRole,
        scenario: q.message,
        prompt: q.prompt,
        selectedOption: selected?.label || row.selectedOptionId,
        otherOptions: others,
      };
    }).filter(Boolean);
  }

  function patchJudgmentBasis(items) {
    const basis = document.getElementById('figmaReportBasis');
    if (!basis || !items || !items.length) return;
    basis.innerHTML = items
      .map((line) => sanitizeJudgmentLine(line))
      .filter((line) => line.length > 0)
      .map((line) => '<li>' + esc(line) + '</li>')
      .join('');
  }

  function updateMicrotaskNavButtons(step, total, status) {
    const prevBtn = document.getElementById('microtaskPrevBtn');
    const nextBtn = document.getElementById('microtaskNextBtn');
    if (!prevBtn || !nextBtn) return;
    prevBtn.disabled = step <= 1;
    const hasSelection = Boolean(state.microtaskSelectedOption);
    nextBtn.disabled = !hasSelection && status !== 'completed';
    if (status === 'completed' && step >= total) {
      nextBtn.setAttribute('aria-label', '进入情景模拟');
    } else if (step >= total) {
      nextBtn.setAttribute('aria-label', '完成微任务');
    } else {
      nextBtn.setAttribute('aria-label', '下一题');
    }
  }

  function restoreMicrotaskSelection(step) {
    const saved = state.microtaskStepAnswers[step];
    state.microtaskSelectedOption = saved || null;
    document.querySelectorAll('#microtaskOptions .option').forEach((x) => x.classList.remove('selected'));
    if (saved) {
      const opt = document.querySelector('#microtaskOptions [data-microtask-option="' + saved + '"]');
      if (opt) opt.classList.add('selected');
    }
  }

  async function goMicrotaskPrev() {
    if (state.microtaskStep <= 1) return;
    await renderMicrotaskFromLocal(state.currentJobId, state.microtaskStep - 1);
    restoreMicrotaskSelection(state.microtaskStep);
    const bank = await loadMicrotaskBank();
    const backendId = API_JOB_TO_BACKEND[state.currentJobId] || state.currentJobId;
    const total = bank.jobs?.[backendId]?.sets?.[state.microtaskSetId]?.questions?.length || 6;
    updateMicrotaskNavButtons(state.microtaskStep, total, 'in_progress');
  }

  function formatMicrotaskMeta(stepContent) {
    const parts = [stepContent.speaker];
    if (stepContent.speakerRole) parts.push(stepContent.speakerRole);
    if (stepContent.time) parts.push(stepContent.time);
    return parts.join('｜');
  }

  function renderMicrotaskStep(stepContent, status) {
    const step = stepContent.step || 1;
    const total = stepContent.totalSteps || 6;
    const pct = Math.min(100, Math.round((step / total) * 100));
    state.microtaskStep = step;

    const subtitle = document.getElementById('microtaskSubtitle');
    if (subtitle) subtitle.textContent = '任务 ' + step + ' / ' + total + ' · 情境选择题';

    const progress = document.getElementById('microtaskProgress');
    const progressBar = document.getElementById('microtaskProgressBar');
    if (progress && progress.firstChild) progress.firstChild.textContent = pct + '%';
    if (progressBar) progressBar.style.width = pct + '%';

    const meta = document.getElementById('microtaskMeta');
    if (meta) meta.textContent = formatMicrotaskMeta(stepContent);

    const message = document.getElementById('microtaskMessage');
    if (message) message.textContent = stepContent.message || '';

    const prompt = document.getElementById('microtaskPrompt');
    if (prompt) prompt.textContent = stepContent.prompt || '';

    const optionsHost = document.getElementById('microtaskOptions');
    if (optionsHost) {
      optionsHost.innerHTML = (stepContent.options || [])
        .map(
          (o) =>
            '<button class="option" type="button" data-microtask-option="' +
            esc(o.id) +
            '">' +
            esc(o.label) +
            '</button>'
        )
        .join('');
    }

    state.microtaskSelectedOption = null;
    restoreMicrotaskSelection(step);
    updateMicrotaskNavButtons(step, total, status);
  }

  async function loadMicrotaskBank() {
    if (state.microtaskBank) return state.microtaskBank;
    const res = await fetch('data/microtask-bank.json');
    if (!res.ok) throw new Error('本地微任务题库加载失败');
    state.microtaskBank = await res.json();
    return state.microtaskBank;
  }

  async function renderMicrotaskFromLocal(jobId, stepNum) {
    const bank = await loadMicrotaskBank();
    const backendId = API_JOB_TO_BACKEND[jobId] || jobId;
    const questions = bank.jobs?.[backendId]?.sets?.[state.microtaskSetId]?.questions || [];
    const q = questions[stepNum - 1];
    if (!q) return;
    renderMicrotaskStep(
      {
        step: stepNum,
        totalSteps: questions.length,
        time: q.time,
        speaker: q.speaker,
        speakerRole: q.speakerRole,
        message: q.message,
        prompt: q.prompt,
        options: q.options,
      },
      stepNum >= questions.length ? 'completed' : 'in_progress'
    );
  }

  async function finishMicrotasks() {
    if (!state.completedJobs.includes(state.currentJobId)) {
      state.completedJobs.push(state.currentJobId);
      renderGrowth();
    }
    await startSceneFlow();
  }

  async function submitMicrotaskStep() {
    if (!state.microtaskSelectedOption && state.microtaskStep < 6) return;

    const bank = await loadMicrotaskBank();
    const backendId = API_JOB_TO_BACKEND[state.currentJobId] || state.currentJobId;
    const total = bank.jobs?.[backendId]?.sets?.[state.microtaskSetId]?.questions?.length || 6;

    state.microtaskStepAnswers[state.microtaskStep] = state.microtaskSelectedOption;

    if (state.microtaskStep < state.microtaskMaxSubmittedStep) {
      if (state.microtaskStep >= total) {
        await finishMicrotasks();
        return;
      }
      await renderMicrotaskFromLocal(state.currentJobId, state.microtaskStep + 1);
      restoreMicrotaskSelection(state.microtaskStep);
      updateMicrotaskNavButtons(state.microtaskStep, total, 'in_progress');
      return;
    }

    const answer = { selectedOptionId: state.microtaskSelectedOption };

    if (!state.useMock && state.taskSessionId) {
      try {
        const res = await api('/tasks/' + state.taskSessionId + '/step', {
          method: 'POST',
          headers: apiHeaders(true),
          body: JSON.stringify({ answer, events: [] }),
        });
        if (res.taskRadar) state.microtaskRadar = res.taskRadar;
        state.microtaskMaxSubmittedStep = Math.max(state.microtaskMaxSubmittedStep, state.microtaskStep + 1);
        state.localMicrotaskAnswers.push({
          step: state.microtaskStep,
          selectedOptionId: state.microtaskSelectedOption,
        });
        if (res.status === 'completed') {
          if (!state.microtaskRadar && state.taskSessionId) {
            try {
              state.microtaskRadar = await api('/tasks/' + state.taskSessionId + '/radar', { headers: apiHeaders() });
            } catch (e) {
              console.warn('雷达数据获取失败', e);
            }
          }
          await finishMicrotasks();
          return;
        }
        renderMicrotaskStep(res.stepContent, res.status);
        return;
      } catch (err) {
        console.warn('微任务提交失败，使用本地推进', err);
      }
    }

    state.localMicrotaskAnswers.push({
      step: state.microtaskStep,
      selectedOptionId: state.microtaskSelectedOption,
    });
    state.microtaskMaxSubmittedStep = Math.max(state.microtaskMaxSubmittedStep, state.microtaskStep + 1);

    if (state.microtaskStep >= total) {
      try {
        await loadMicrotaskScores();
        state.microtaskRadar = buildLocalRadar();
      } catch (err) {
        console.warn('本地雷达计算失败', err);
      }
      await finishMicrotasks();
      return;
    }
    await renderMicrotaskFromLocal(state.currentJobId, state.microtaskStep + 1);
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
    const taskRadar = report.taskRadar || state.microtaskRadar;
    if (taskRadar) {
      state.microtaskRadar = taskRadar;
      renderReportRadar(taskRadar);
    }

    if (report.judgmentBasis && report.judgmentBasis.length) {
      patchJudgmentBasis(report.judgmentBasis);
    } else {
      buildLocalJudgmentBasis().then((localBasis) => {
        if (localBasis.length) patchJudgmentBasis(localBasis);
        else {
          const basis = document.getElementById('figmaReportBasis');
          if (basis) basis.innerHTML = '<li>暂无判断依据，请完成微任务后重新生成报告。</li>';
        }
      });
    }
    if (report.learningAdvice && report.learningAdvice.length) {
      patchLearningAdvice(report.learningAdvice);
    } else if (taskRadar) {
      patchAdviceFromRadar(taskRadar);
    }

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
              <p class="figma-hero-lead" id="figmaReportHeadline">你在本轮微任务中完成了 6 道情境判断题。</p>
              <p id="figmaReportSummary">本轮微任务与情景选择已记录，以下仅供继续探索参考。</p>
            </div>
            <img class="figma-hero-art" src="assets/report/hero-workspace.png" alt="AI 产品经理工作场景插画" />
          </section>
          <section class="figma-evidence-panel">
            <div class="figma-evidence-left">
              <h2>🌳 你的岗位能力对照：</h2>
              <div class="figma-radar-wrap">
                <svg id="reportRadarSvg" viewBox="0 0 370 335" role="img" aria-label="岗位能力雷达图"></svg>
              </div>
            </div>
            <div class="figma-evidence-copy"><h3>AI判断依据</h3><ul id="figmaReportBasis"><li class="figma-basis-pending">正在结合你的答题生成判断依据…</li></ul><p id="figmaReportNotice" class="figma-notice"></p></div>
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
      renderRecommendations();
    } catch (err) {
      console.warn('推荐接口失败', err);
    }
  }

  async function startJob(jobId) {
    const job = getJob(jobId);
    if (!job) return;
    state.currentJobId = jobId;
    state.microtaskStep = 1;
    state.microtaskSelectedOption = null;
    state.microtaskSetId = pickMicrotaskSetId();
    state.localMicrotaskAnswers = [];
    state.microtaskStepAnswers = {};
    state.microtaskMaxSubmittedStep = 0;
    state.microtaskRadar = null;
    updateTaskTitles(jobId);

    let task = null;
    if (!state.useMock && state.sessionId) {
      try {
        task = await api('/tasks', {
          method: 'POST',
          headers: apiHeaders(true),
          body: JSON.stringify({ jobId, scaffoldType: 'career_changer' }),
        });
        state.taskSessionId = task.taskSessionId;
        if (task.setId) state.microtaskSetId = task.setId;
      } catch (err) {
        console.warn('创建任务会话失败，使用本地题库', err);
        state.taskSessionId = null;
      }
    }

    if (task?.stepContent) {
      renderMicrotaskStep(task.stepContent, task.status);
    } else {
      try {
        await renderMicrotaskFromLocal(jobId, 1);
      } catch (err) {
        console.warn('本地微任务题库加载失败', err);
      }
    }
    go('choice');
  }

  async function loadSceneScripts() {
    if (state.sceneScripts) return state.sceneScripts;
    const jobId = state.currentJobId;
    const sceneIds = [
      SCENE_S1_IDS[jobId],
      SCENE_S2_IDS[jobId],
      SCENE_S3_IDS[jobId],
    ].filter(Boolean);

    const loadLocal = async () => {
      try {
        const [s1, s2, s3] = await Promise.all([
          fetch('data/s1-meeting.json'),
          fetch('data/s2-client.json'),
          fetch('data/s3-release.json'),
        ]);
        if (!s1.ok || !s2.ok || !s3.ok) return null;
        return {
          ...(await s1.json()),
          ...(await s2.json()),
          ...(await s3.json()),
        };
      } catch (err) {
        console.warn('本地情景剧本加载失败', err);
        return null;
      }
    };

    if (!state.useMock && state.sessionId && sceneIds.length) {
      const scripts = {};
      for (const id of sceneIds) {
        try {
          scripts[id] = await api('/scenes/' + id, { headers: apiHeaders() });
        } catch (err) {
          console.warn('情景剧本接口失败', id, err);
        }
      }
      if (Object.keys(scripts).length) {
        state.sceneScripts = scripts;
        return state.sceneScripts;
      }
    }
    state.sceneScripts = await loadLocal();
    return state.sceneScripts;
  }

  function resetScenePanelState() {
    state.scenePanelPhase = 0;
    state.scenePanelMaxPhase = 0;
    state.sceneDraft = { selectedOptionId: null, customText: '' };
  }

  function getCurrentSceneScript() {
    const step = SCENE_STEPS[state.sceneStep];
    if (!step || !step.scripted) return null;
    const sceneId = step.sceneIdForJob(state.currentJobId);
    const scripts = state.sceneScripts || {};
    return scripts[sceneId] || null;
  }

  function renderScenePanelPhase(scene, step) {
    const phase = state.scenePanelPhase;
    const draft = state.sceneDraft;
    const head =
      '<div class="scene-sim-head"><span class="scene-sim-time">' + esc(scene.time || '') + '</span>' +
      '<span class="scene-sim-title">' + esc(scene.title || step.badge) + '</span></div>';

    if (phase === 0) {
      const messages = (scene.messages || [])
        .map(
          (m) =>
            '<div class="scene-sim-message"><span class="scene-sim-message-speaker">' +
            esc(m.speaker) + '</span><span class="scene-sim-message-text">' + esc(m.text) + '</span></div>'
        )
        .join('');
      return (
        '<div class="scene-sim-phase scene-sim-phase--active">' +
        '<p class="scene-sim-phase-label">情境说明</p>' + head +
        '<p class="scene-sim-context">' + esc(scene.context || '') + '</p>' +
        (messages ? '<div class="scene-sim-messages">' + messages + '</div>' : '') +
        '</div>'
      );
    }

    if (phase === 1) {
      return (
        '<div class="scene-sim-phase scene-sim-phase--active">' +
        '<p class="scene-sim-phase-label">需要你判断</p>' +
        '<p class="scene-sim-question scene-sim-question--solo">' + esc(scene.question || '') + '</p>' +
        '</div>'
      );
    }

    const options = (scene.options || [])
      .map((opt, i) => {
        const selected = draft.selectedOptionId === opt.optionId;
        return (
          '<button class="scene-sim-option' + (selected ? ' selected' : '') + '" type="button" data-scene-option="' +
          esc(opt.optionId) + '">' + String.fromCharCode(65 + i) + '. ' + esc(opt.text) + '</button>'
        );
      })
      .join('');

    return (
      '<div class="scene-sim-phase scene-sim-phase--active">' +
      '<p class="scene-sim-phase-label">选择你的处理方式</p>' +
      '<div class="scene-sim-options" id="sceneSimOptions">' + options + '</div>' +
      '<div class="scene-sim-custom">' +
      '<label for="sceneCustomAnswer">' + esc(scene.customPrompt || '我会这样处理：') + '</label>' +
      '<textarea id="sceneCustomAnswer" placeholder="也可以用自己的方式回答…" maxlength="500">' +
      esc(draft.customText) + '</textarea></div></div>'
    );
  }

  function updateSceneSideNav(step) {
    const prevBtn = document.getElementById('sceneSimPrevBtn');
    const nextBtn = document.getElementById('sceneSimNextBtn');
    if (!prevBtn || !nextBtn) return;

    if (!step.scripted) {
      prevBtn.hidden = true;
      nextBtn.hidden = false;
      nextBtn.disabled = false;
      nextBtn.textContent = '继续';
      nextBtn.setAttribute('data-action', 'scene-continue');
      nextBtn.setAttribute('aria-label', '继续');
      return;
    }

    const scene = getCurrentSceneScript();
    if (!scene) {
      prevBtn.hidden = true;
      nextBtn.hidden = true;
      return;
    }

    const phase = state.scenePanelPhase;
    const lastPhase = SCENE_PANEL_PHASES.length - 1;
    prevBtn.hidden = phase <= 0;
    nextBtn.hidden = false;
    if (!prevBtn.hidden) {
      prevBtn.disabled = false;
    }
    prevBtn.textContent = '上一步';
    prevBtn.setAttribute('data-action', 'scene-phase-prev');
    prevBtn.setAttribute('aria-label', '上一步');

    if (phase === lastPhase) {
      nextBtn.textContent = '确认并继续';
      nextBtn.setAttribute('data-action', 'scene-submit');
      nextBtn.setAttribute('aria-label', '确认并继续');
    } else {
      nextBtn.textContent = '下一步';
      nextBtn.setAttribute('data-action', 'scene-phase-next');
      nextBtn.setAttribute('aria-label', '下一步');
    }
    nextBtn.disabled = false;
  }

  function renderSceneSim() {
    const step = SCENE_STEPS[state.sceneStep];
    const badge = document.getElementById('sceneSimBadge');
    const art = document.getElementById('sceneSimArt');
    const panel = document.getElementById('sceneSimPanel');
    if (!step || !badge || !art || !panel) return;

    badge.textContent = step.badge;
    badge.className = 'scene-sim-badge scene-sim-badge--' + step.theme;
    art.src = step.art;
    art.alt = step.badge + '情景插画';

    if (!step.scripted) {
      panel.innerHTML =
        '<div class="scene-sim-panel-inner scene-sim-placeholder">' +
        '<p class="scene-sim-step">情景 ' + (state.sceneStep + 1) + ' / ' + SCENE_STEPS.length + '</p>' +
        '<h3>' + esc(step.placeholderTitle) + '</h3>' +
        '<p>' + esc(step.placeholderDesc) + '</p></div>';
      updateSceneSideNav(step);
      return;
    }

    const scene = getCurrentSceneScript();
    if (!scene) {
      panel.innerHTML =
        '<div class="scene-sim-panel-inner scene-sim-placeholder"><p>情景剧本加载中…</p></div>';
      updateSceneSideNav(step);
      loadSceneScripts().then(() => renderSceneSim());
      return;
    }

    panel.innerHTML =
      '<div class="scene-sim-panel-inner scene-sim-panel-inner--phased">' +
      '<p class="scene-sim-step">情景 ' + (state.sceneStep + 1) + ' / ' + SCENE_STEPS.length + '</p>' +
      '<div class="scene-sim-phase-body">' + renderScenePanelPhase(scene, step) + '</div></div>';
    updateSceneSideNav(step);
  }

  function syncSceneDraftFromDom() {
    const customEl = document.getElementById('sceneCustomAnswer');
    if (customEl) state.sceneDraft.customText = customEl.value;
  }

  function goScenePanelPhase(target) {
    const max = SCENE_PANEL_PHASES.length - 1;
    const next = Math.max(0, Math.min(max, target));
    if (next > state.scenePanelMaxPhase) return;
    syncSceneDraftFromDom();
    state.scenePanelPhase = next;
    renderSceneSim();
  }

  function advanceScenePanelPhase() {
    syncSceneDraftFromDom();
    if (state.scenePanelPhase >= SCENE_PANEL_PHASES.length - 1) {
      submitSceneStep();
      return;
    }
    state.scenePanelPhase += 1;
    if (state.scenePanelPhase > state.scenePanelMaxPhase) {
      state.scenePanelMaxPhase = state.scenePanelPhase;
    }
    renderSceneSim();
  }

  async function submitSceneStep() {
    const step = SCENE_STEPS[state.sceneStep];
    if (!step) return;

    if (step.scripted) {
      syncSceneDraftFromDom();
      const draft = state.sceneDraft;
      const sceneId = step.sceneIdForJob(state.currentJobId);
      if (!draft.selectedOptionId && !draft.customText.trim()) {
        window.alert('请选择一项方案，或填写你的处理方式。');
        state.scenePanelPhase = SCENE_PANEL_PHASES.length - 1;
        state.scenePanelMaxPhase = SCENE_PANEL_PHASES.length - 1;
        renderSceneSim();
        return;
      }
      const body = draft.selectedOptionId
        ? { roleId: state.currentJobId, answerType: 'preset', selectedOptionId: draft.selectedOptionId, rawAnswer: null }
        : { roleId: state.currentJobId, answerType: 'custom', selectedOptionId: null, rawAnswer: draft.customText.trim() };
      if (!state.useMock && state.sessionId && sceneId) {
        try {
          await api('/scenes/' + sceneId + '/answers', {
            method: 'POST',
            headers: apiHeaders(true),
            body: JSON.stringify(body),
          });
        } catch (err) {
          console.warn('情景回答提交失败，继续流程', err);
        }
      }
    }

    if (state.sceneStep >= SCENE_STEPS.length - 1) {
      go('analyze2');
      window.setTimeout(() => finishAnalysisProgress('task'), 1500);
      return;
    }
    state.sceneStep += 1;
    resetScenePanelState();
    renderSceneSim();
  }

  async function startSceneFlow() {
    state.sceneStep = 0;
    state.sceneScripts = null;
    resetScenePanelState();
    await loadSceneScripts();
    go('sceneSim');
  }

  async function loadReport() {
    updateTaskTitles(state.currentJobId);
    go('report');
    if (state.microtaskRadar) {
      renderReportRadar(state.microtaskRadar);
    }
    if (!state.useMock && state.sessionId) {
      try {
        const q = '?jobId=' + encodeURIComponent(state.currentJobId);
        const signals = await buildMicrotaskChoiceSignalsForReport();
        const payload = signals.length
          ? { microtaskChoiceSignals: signals, setId: state.microtaskSetId }
          : null;
        const report = await api('/reports/generate' + q, {
          method: 'POST',
          headers: apiHeaders(payload != null),
          body: payload ? JSON.stringify(payload) : undefined,
        });
        patchReport(report);
      } catch (err) {
        console.warn('报告接口失败', err);
        if (state.microtaskRadar) patchReport({ taskRadar: state.microtaskRadar });
      }
    } else if (state.microtaskRadar) {
      patchReport({ taskRadar: state.microtaskRadar, comparisonSummary: '', boundaryNotice: '' });
    }
  }

  function bindUi() {
    document.addEventListener('input', (e) => {
      if (e.target.id !== 'sceneCustomAnswer') return;
      state.sceneDraft.customText = e.target.value;
      if (state.sceneDraft.selectedOptionId) {
        state.sceneDraft.selectedOptionId = null;
        document.querySelectorAll('.scene-sim-option.selected').forEach((o) => o.classList.remove('selected'));
      }
    });

    document.addEventListener('click', (e) => {
      const nav = e.target.closest('[data-go]');
      if (nav && !nav.closest('#drawer')) go(nav.dataset.go);

      const startBtn = e.target.closest('[data-action="start-job"]');
      if (startBtn) startJob(startBtn.dataset.jobId);

      const reportBtn = e.target.closest('[data-action="view-report"]');
      if (reportBtn) {
        state.currentJobId = reportBtn.dataset.jobId;
        loadReport();
      }

      const sceneSideBtn = e.target.closest('#sceneSimPrevBtn, #sceneSimNextBtn');
      if (sceneSideBtn && !sceneSideBtn.hidden) {
        const action = sceneSideBtn.getAttribute('data-action');
        if (action === 'scene-phase-prev') {
          goScenePanelPhase(state.scenePanelPhase - 1);
        } else if (action === 'scene-phase-next') {
          advanceScenePanelPhase();
        } else if (action === 'scene-submit' || action === 'scene-continue') {
          submitSceneStep();
        }
        return;
      }

      const sceneOption = e.target.closest('[data-scene-option]');
      if (sceneOption) {
        state.sceneDraft.selectedOptionId = sceneOption.dataset.sceneOption;
        state.sceneDraft.customText = '';
        renderSceneSim();
      }

      const microOption = e.target.closest('[data-microtask-option]');
      if (microOption) {
        state.microtaskSelectedOption = microOption.dataset.microtaskOption;
        state.microtaskStepAnswers[state.microtaskStep] = state.microtaskSelectedOption;
        document.querySelectorAll('#microtaskOptions .option').forEach((x) => x.classList.remove('selected'));
        microOption.classList.add('selected');
        const bank = state.microtaskBank;
        const backendId = API_JOB_TO_BACKEND[state.currentJobId] || state.currentJobId;
        const total = bank?.jobs?.[backendId]?.sets?.[state.microtaskSetId]?.questions?.length || 6;
        updateMicrotaskNavButtons(state.microtaskStep, total, 'in_progress');
      }

      const drawerGo = e.target.closest('#drawer [data-go]');
      if (drawerGo) {
        document.getElementById('drawer').classList.remove('show');
        document.getElementById('drawerShade').classList.remove('show');
        go(drawerGo.dataset.go);
      }
    });

    document.querySelectorAll('[data-nav]').forEach((b) => {
      b.addEventListener('click', () => go(b.dataset.nav));
    });

    document.querySelectorAll('.selectable .chip').forEach((c) => {
      c.addEventListener('click', () => c.classList.toggle('selected'));
    });

    const microtaskNextBtn = document.getElementById('microtaskNextBtn');
    if (microtaskNextBtn) {
      microtaskNextBtn.addEventListener('click', submitMicrotaskStep);
    }
    const microtaskPrevBtn = document.getElementById('microtaskPrevBtn');
    if (microtaskPrevBtn) {
      microtaskPrevBtn.addEventListener('click', goMicrotaskPrev);
    }

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
    document.getElementById('introSkipBtn').addEventListener('click', (e) => {
      e.stopPropagation();
      dismissIntro(false);
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
      go('recommend');
    });

    document.getElementById('viewReportBtn').addEventListener('click', loadReport);
  }

  document.addEventListener('DOMContentLoaded', async () => {
    buildFigmaReport();
    state.jobs = fallbackJobs();
    renderRoles();
    renderRecommendations();
    renderGrowth();
    bindUi();
    await initApi();
    go('home');
    window.scrollTo(0, 0);
    const active = document.querySelector('.screen.active');
    if (active) active.scrollTop = 0;
  });
})();
