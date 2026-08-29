/**
 * ui.js — 前端交互层（设计同学主要改这个文件）
 *
 * 可自由添加：动画、手势、微任务交互、Tab 切换、表单校验提示等
 * 页面跳转请调用：CareerApp.go('screenId')
 * 不要改 app.js（API / 数据 / 后端对接）
 */
(function () {
  'use strict';

  function bindNavigation() {
    document.addEventListener('click', (e) => {
      const nav = e.target.closest('[data-go]');
      if (!nav || nav.closest('#drawer')) return;
      CareerApp.go(nav.dataset.go);
      if (nav.dataset.scrollTarget) {
        const target = document.getElementById(nav.dataset.scrollTarget);
        if (target) requestAnimationFrame(() => target.scrollIntoView({ behavior: 'smooth', block: 'start' }));
      }
    });

    document.querySelectorAll('[data-nav]').forEach((btn) => {
      btn.addEventListener('click', () => CareerApp.go(btn.dataset.nav));
    });
  }

  function bindHomeArrow() {
    const arrow = document.getElementById('homeNextArrow');
    const home = document.getElementById('home');
    const next = home && home.querySelector('.home-panel:nth-child(2)');
    if (arrow && next) arrow.addEventListener('click', () => next.scrollIntoView({ behavior: 'smooth', block: 'start' }));
  }

  function bindDrawer() {
    const drawer = document.getElementById('drawer');
    const shade = document.getElementById('drawerShade');
    if (!drawer || !shade) return;

    const closeDrawer = () => {
      drawer.classList.remove('show');
      shade.classList.remove('show');
    };

    const profileBtn = document.getElementById('profileBtn');
    if (profileBtn) {
      profileBtn.addEventListener('click', () => {
        drawer.classList.add('show');
        shade.classList.add('show');
      });
    }

    const closeBtn = document.getElementById('drawerClose');
    if (closeBtn) closeBtn.addEventListener('click', closeDrawer);
    shade.addEventListener('click', closeDrawer);

    drawer.querySelectorAll('[data-go]').forEach((item) => {
      item.addEventListener('click', () => {
        closeDrawer();
        CareerApp.go(item.dataset.go);
      });
    });
  }

  function bindIntro() {
    const intro = document.getElementById('intro');
    if (!intro) return;
    intro.addEventListener('click', (e) => {
      e.currentTarget.classList.add('hide');
    });
  }

  function bindChips() {
    document.querySelectorAll('.selectable .chip').forEach((chip) => {
      chip.addEventListener('click', () => chip.classList.toggle('selected'));
    });
  }

  function bindSingleSelect() {
    document.querySelectorAll('.single-select .option').forEach((option) => {
      option.addEventListener('click', () => {
        option.parentElement.querySelectorAll('.option').forEach((x) => x.classList.remove('selected'));
        option.classList.add('selected');
        const group = option.closest('[data-validity="single-choice"]');
        const btn = document.getElementById('choiceContinue');
        if (group && btn) { btn.disabled = group.querySelectorAll('.option.selected').length !== 1; document.getElementById('choiceFeedback').textContent = '已选择 1 项，可以继续。'; }
      });
    });
  }

  function bindEvidenceSelection() {
    const group = document.getElementById('evidenceChoices');
    const btn = document.getElementById('evidenceContinue');
    const feedback = document.getElementById('evidenceFeedback');
    if (!group || !btn || !feedback) return;
    const boxes = [...group.querySelectorAll('input[type="checkbox"]')];
    const update = () => { const n = boxes.filter(x => x.checked).length; feedback.textContent = n === 2 ? '已选择 2/2 项，可以继续。' : `已选择 ${n}/2 项，请选择 2 项。`; btn.disabled = n !== 2; boxes.forEach(x => { x.disabled = n >= 2 && !x.checked; }); };
    boxes.forEach(x => x.addEventListener('change', update)); update();
  }

  function bindRankingDrag() {
    const rankingList = document.getElementById('rankingList');
    if (!rankingList) return;

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
  }

  function bindMatchingBoard() {
    const matchingBoard = document.getElementById('matchingBoard');
    const matchLines = document.getElementById('matchLines');
    if (!matchingBoard || !matchLines) return;

    let selectedSource = null;
    const connections = new Map();

    const drawConnections = () => {
      const boardRect = matchingBoard.getBoundingClientRect();
      matchLines.replaceChildren();
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
        selectedSource = null;
        drawConnections();
      });
    });

    window.addEventListener('resize', drawConnections);
  }

  /** 在此函数内追加你们的自定义交互 */
  function bindCustomInteractions() {
    // 示例：成长页 Tab 切换（可按设计稿扩展）
    // document.querySelectorAll('#growth .tab').forEach(...)
  }

  function init() {
    bindNavigation();
    bindHomeArrow();
    bindDrawer();
    bindIntro();
    bindChips();
    bindSingleSelect();
    bindEvidenceSelection();
    bindRankingDrag();
    bindMatchingBoard();
    bindCustomInteractions();
  }

  document.addEventListener('DOMContentLoaded', init);
})();
