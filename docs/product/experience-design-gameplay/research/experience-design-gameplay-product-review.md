# AI 产品设计场景游戏 Product / UX Review

Status: Supporting Review / Non-normative  
Canonical trial: [AI UI Design Career Trial](../../trials/ai-ui-design.md)  
This review records prototype observations at the time of testing. Its 5–8 minute target and report terminology are historical review context, not current product rules.

Review date: 2026-08-28  
Scope: `prototype/experience-design-gameplay-prototype.html` only  
Method: Microsoft Edge / Playwright real browser interaction, desktop 1440×1000 and mobile 390×844. Six required paths were replayed before code changes.

## 1. Review question

判断标准不是“流程是否能点通”，而是：一个没有 UX/UI 经验、但用过日常 AI 产品的人，能否在 5–8 分钟后说：

> 我刚刚真的像一个 AI 产品设计师一样，听取了不同人的信息、调整了产品、看用户使用、发现问题并改了一次。

Baseline verdict: **部分成立，但仍被 assessment 结构明显削弱。**

## 2. Browser walkthrough evidence

### Path 1：第一次进入、不知道规则

| Step | 玩家看到 | 玩家可能以为 | 实际能做 | 下一步动机 / 反馈 | 体验判断 |
|---|---|---|---|---|---|
| Briefing | “先从桌面找线索”，两个 CTA | 系统规定先搜集证据 | 接任务或 Demo mode | 文案直接给出操作顺序 | 有角色感，但仍像 instruction screen |
| Workspace | 6 个同尺寸物件、`0/6`、右侧 5 条成功条件 | 要点完六张卡，再凑齐五个答案 | 可跳过调查直接设计 | 物件 pulse、hover 即时 | 有小游戏触感；`0/6` 把 discovery 变 checklist |
| User card | 用户引语 + 原因解释 + Pin | 需要收集这张正确线索 | Pin 或放回 | 物件变灰、证据板新增 | 有场景状态变化；卡片仍偏材料题 |
| Canvas | “用 5 个组件”、`0/5`、三个专业分区 | 从八个组件中猜系统期待的五个 | 点击/拖入；点击自动放到推荐区 | 预算计数变化 | 明显像 component quiz |

### Path 2：快速行动、很少查看证据

- 玩家可 0/6 线索直接进入画布，系统弹出“设计师通常会听几种不同角色的声音”。
- 选择 CTA、History、Templates 后运行，Mia 立即被告知“核心区域需要输入和一个明确主动作”。
- **Observation**：快速行动路径可走通，行为能被记录。
- **Problem**：后果和规则解释同时出现，像判题；玩家没有先看到 Mia 真正寻找入口的过程。

### Path 3：充分调查后再设计

- 六个物件全部变为 `PINNED ✓`，证据板列出六条压缩信息。
- Input、CTA、Progress、Source、Retry 形成 `5/5`，右侧五条全部变绿。
- 四步测试完整通过，报告显示 “5/5 条体验条件”。
- **Observation**：工作对象连续，调查结果、画布、测试和报告处于同一场景。
- **Problem**：右侧条件、5/5 预算和自动推荐分区共同泄露标准答案。充分调查更像“读完材料再答题”，而不是形成自己的设计判断。

### Path 4：缺少 Recovery → failure → revision → retest

- Demo mode 首轮到最后一步出现“出错了”，玩家返回画布加入 Retry。
- 第二轮相同步骤成功，报告准确记录 2 次测试和 1 次修订。
- **Strongest moment**：这是当前最像真实工作的部分。玩家的设计直接决定模拟用户是否进入 dead end。
- **Problem**：失败一出现就解释“页面没有告诉 Mia 下一步怎么办”，缺少短暂的真实困惑；系统先讲答案，再让玩家修。

### Path 5：故意做差设计

- History、Templates、Source、Edit、Progress 可占满 5/5。
- 测试在第一步立即指出缺 Input 和 CTA。
- **Observation**：坏设计不会造成 JS 错误，且可返回修改。
- **Problem**：`5/5` 仍让一个完全不可用的方案看起来“预算完成”；这证明计数表达的是游戏规则，不是产品 readiness。

### Path 6：390×844 mobile

- 无水平溢出；dialog 完整位于视口内；组件与三区可使用点击 fallback。
- 工作台、Laptop、Mission 纵向排列，核心流程可完成。
- sticky topbar 在长设计画布滚动时占用两行高度，full-page capture 中会遮住组件区域；移动端空间感弱化为长表单。
- 初始 Mission 位于 Laptop 后方，用户需滚动很远才能重新确认目标。

## 3. Six-problem audit

### 3.1 Immersion

角色、Deadline、持续工作台和 Mia 提供了轻叙事。破坏沉浸的不是视觉，而是显式教学结构：Briefing 规定“先找线索”，顶部显示三个 Act，左栏显示 0/6，右栏展示五条正确条件。玩家知道自己在被流程引导和记录。

### 3.2 Discovery vs Answering

物件确实可自主点击，点击/Pin 有即时反馈。但所有物件同时以相同权重呈现，`0/6` 暗示应该全部收集；右侧目标清单提前解释它们为什么重要。当前是“可自由排序的材料阅读”，还不是开放发现。

### 3.3 Design vs Component Quiz

这是 baseline 最严重的问题：`0/5`、五条成功条件、八选五、自动放入推荐区域，形成一个清晰的隐藏答案系统。限制没有被体验成首页空间、显示时机和 sprint 工程成本。

### 3.4 AI UX through consequence

系统覆盖 loading、source、failure、retry，但缺少真实时间和困惑：没有 progress 时不会真的出现“点击后毫无变化”的几秒；缺 source 时立即解释；失败时立即解释。知识点存在，体验后果不够。

### 3.5 Beginner friendliness

任务本身易懂，组件名称来自日常 AI 使用经验。`PRIMARY / SUPPORT / RECOVERY` 是未经教学的专业分类，且在操作前就要求理解。更好的顺序是先用“首页 / 生成过程中 / 出错时”完成，再在报告中命名 information hierarchy、interaction state、failure recovery。

### 3.6 Career signal validity

当前原始事件大体可解释，但展示层有过度推断：

- `visited.size` 只能说明探索广度，不能说明用户同理心或设计能力。
- 先看三条线索可说明 evidence-seeking tendency，不能说明证据质量。
- `5/5` 是规则匹配，不应呈现为职业 performance 分数。
- “异常预判型”“核心任务守门员”是推断标签，证据句比人格化标签更可靠。
- Energy 自报有效，但只问总体能量，无法回答哪个部分有劲、哪个部分烦。

## 4. Reference video comparison — baseline

| Dimension | Score | Explanation |
|---|---:|---|
| Immersion | 3/5 | 持续工作台和 Deadline 成立，但显式任务清单、阶段灯和分数不断提醒玩家这是体验流程。 |
| Agency | 3/5 | 可选择调查顺序和组件，但点击组件自动进入正确区域，真实决策权有限。 |
| Discovery | 2/5 | 信息通过物件揭示，但 `0/6` 和右侧答案清单把探索变成收集任务。 |
| Tactility | 4/5 | hover、click、Pin、drag、状态灰化和画布变化反馈清晰。 |
| State continuity | 4/5 | 同一工作台贯穿 Explore、Design、Test、Review，revision 保持玩家方案。 |
| Narrative coherence | 3/5 | Demo deadline、Mia 和工程限制一致，但“五组件游戏规则”不像项目限制。 |

## 5. Severity triage

### P0 — 会破坏体验核心

#### P0-1：正确答案在任务开始时被完整泄露

- Evidence: Mission 右侧列出 Input、CTA、Progress、Source、Recovery 五条；画布和测试一一判定。
- Impact: 玩家优化的是系统分数，不是 Mia 的体验。
- Targeted fix: 开始时只保留用户目标和项目背景；条件只在真实测试中作为观察结果出现。

#### P0-2：`5 / 5 components` 把设计变成八选五

- Evidence: 差设计也可以显示 5/5；充分调查路径直接拼出标准五件套。
- Impact: 资源约束感觉来自小游戏，而不是 sprint、页面空间和状态时机。
- Targeted fix: 移除总组件计数；改为场景容量（首页首屏 2、过程/结果旁 2、仅出错时 1）和“本 sprint 只能开发一个新行为”。

#### P0-3：点击 fallback 自动替玩家选择正确分区

- Evidence: 点击任意组件会调用推荐区域，玩家无需决定信息层级。
- Impact: 最能体现设计判断的动作被系统代做。
- Targeted fix: 点击只选中组件，玩家再点击具体场景区域放置；拖放保留。

### P1 — 明显影响体验

#### P1-1：AI UX 后果与解释同时出现

- 缺 loading 时应先经历几秒无反馈，再出现 Mia 的疑问。
- 缺 recovery 时应先形成 dead end，再逐步出现 revision 入口。
- Targeted fix: Show consequence, then explain。用短时间状态变化实现，不增加新场景。

#### P1-2：Design Review 仍是成绩单

- Evidence: `5 / 5 条体验条件`、behavior labels、缺少“设计师把这些工作叫什么”和真实日常延伸。
- Targeted fix: 改为 What you just did / How you worked / How it felt / What designers call this / Continue exploring；使用实际行为句，不输出综合分。

#### P1-3：探索被 `0 / 6` 与指令规定

- Targeted fix: 移除物件完成计数和“先找线索”指令；保留物件 affordance、访问后的可见状态与 evidence board。

#### P1-4：移动端 sticky header 遮挡长画布

- Targeted fix: ≤720px 时 topbar 改为 static，并缩短品牌区域；不重做移动布局。

### P2 — 可以后优化，本轮不修

- 物件仍是六张规整卡片，空间隐喻有限；可未来改为更自然的桌面物件布局。
- 没有拖动物件本身或更丰富的环境音/轻 surprise。
- Test 只有 Mia 一个用户与单一会议助手任务。
- 没有真实可用性研究计时、眼动或口述数据。
- 移动端仍是纵向工作区，不具有桌面场景的空间感。
- Contextual card 文本仍偏长，可在真实用户研究后继续压缩。

## 6. Career signal audit

| Signal | Decision | Reason / revised wording |
|---|---|---|
| Object visit order | Keep | Observation: “你先查看了模型状态，再查看用户反馈。”不判断优劣。 |
| Number of objects viewed | Modify | 仅表述探索范围；删除“看得多 = 更好”。 |
| Pinned evidence | Modify | 表述玩家选择保留了哪些角色的信息，不推断 empathy。 |
| Time before design | Keep | 可说明 evidence-first / action-first tendency；两者都可能有效。 |
| Component/zone choices | Keep | 直接说明玩家把什么放在用户最先看到、过程中或出错时。 |
| 5/5 criteria score | Delete | 这是规则匹配，不是有效职业信号。 |
| Test runs | Keep | 说明是否用原型获取反馈。次数本身不等于能力。 |
| Revision count | Modify | 与“具体修复了什么”一起展示；只计次数意义弱。 |
| Behavior labels | Delete from primary report | 用观察句替代“异常预判型”等人格化标签。 |
| Overall energy | Keep | 明确为自报，不进入 performance。 |
| Most energizing / frustrating moment | Add | 让报告回答喜欢哪部分、烦哪部分；仅由玩家选择。 |

## 7. Approved refinement scope

Only P0 and high-value P1:

1. 隐藏预设答案与探索计数。
2. 把 5 组件规则改为界面空间 + 一个新行为的项目约束。
3. 点击组件后由玩家自己选择区域。
4. 测试先展示真实后果，再解释。
5. 重写 Design Review 为工作回放和术语教学。
6. 修复移动端 sticky 遮挡。

No P2 implementation. No new backend, AI API, canvas system, career, scenario or illustration.

## 8. Post-refinement verification

### Changes actually made

1. 删除开场中“先找线索”的规定顺序，改为“桌面和电脑留在昨天的状态”。
2. 删除 `0/6` 探索完成计数；仅显示还有未读消息，全部查看不是完成条件。
3. 删除右侧五条预设成功条件和 `Demo readiness 5/5`。
4. 删除总组件 `0/5` 预算，改为三个来自页面状态的空间限制：首屏 2、过程/结果旁 2、出错时 1。
5. 把工程限制改为：“现有组件可重排，本 sprint 只能开发一个新行为”。Retry、人工编辑、重新生成会竞争同一开发额度。
6. 点击组件不再自动进入推荐区域。玩家先选组件，再亲自选择它出现的位置；拖放仍可用。
7. 把 `PRIMARY / SUPPORT / RECOVERY` 改为小白可理解的“用户一打开就看到 / 生成过程中或结果旁 / 只在出错时出现”。
8. 缺少 loading 时，Mia 点击后先经历 3.2 秒无反馈，再说“它收到我的内容了吗？”
9. 缺少 recovery 时，先显示模型失败和 Mia 寻找下一步，2 秒后才形成 dead end 与 revision 入口。
10. 差设计测试先让 Mia 在 History、Template 等入口中寻找，再呈现“我还是不知道从哪里开始”。
11. Design Review 删除 `5/5` 与人格化 behavior label，重组为 What you just did / How you worked / How it felt / What designers call this / Continue exploring。
12. Energy 增加“哪一段最有劲 / 哪一段最烦”的自报，不从点击行为猜测情绪。
13. ≤720px 时 topbar 改为 static，避免长画布滚动时遮挡内容。

### Before → after

| Before | After |
|---|---|
| Briefing 指定“先从桌面找线索” | 玩家进入工位后自行决定先点消息还是先改电脑 |
| 右栏提前列出五条正确条件 | 右栏只说明 Mia 的目标；问题在试用中逐步出现 |
| `0/6` 暗示要收集全部卡片 | 未读状态只表达场景变化，不作为完成进度 |
| `5/5 components` | 首屏、过程、异常各有可见空间限制 |
| 点击组件自动进入正确区域 | 点击只拿起组件，玩家必须选择出现位置 |
| 缺 loading 立即文字判错 | 先经历无反馈，再听到 Mia 的疑问 |
| 缺 recovery 立即解释规则 | 先看到失败与 dead end，再提供 revision |
| 报告输出 `5/5` 和类型标签 | 报告回放实际行为、主观感受和设计术语 |

### Reference video comparison — after refinement

| Dimension | Score | Explanation |
|---|---:|---|
| Immersion | **4/5** | 临时工位、团队消息、当前产品和 Mia 贯穿始终；预设答案与组件分数已消失。顶部 Act 灯仍是产品层提示，因此未到 5。 |
| Agency | **4/5** | 玩家决定调查顺序、组件位置、工程额度使用与是否 revision；系统不再自动放入推荐区。固定容量仍限制自由度。 |
| Discovery | **4/5** | 关键信息通过物件逐步出现，AI UX 问题主要由 Mia 试用揭示；六张卡的规整布局仍稍像材料列表。 |
| Tactility | **4/5** | click、Pin、选中、明确“放在这里”、drag、容量阻挡、测试状态变化都有即时反馈。没有更丰富的场景物理变化。 |
| State continuity | **5/5** | 调查、布局、试用、失败、revision、retest 和 Review 使用同一个工作对象，玩家修改直接改变 Mia 的下一次经历。 |
| Narrative coherence | **4/5** | 页面空间和一个新行为的限制来自 sprint；Mia 的反馈与玩家布局直接对应。部分英文工具语言仍保留低保真 Demo 感。 |

### Career signal decisions after refinement

#### Retained

- 访问顺序和进入设计前查看的信息数：用于描述 evidence-first / action-first，不评判高低。
- 组件与位置：直接描述玩家让 Mia 第一眼、生成过程中和出错时看到了什么。
- Test runs：证明玩家是否用模拟用户检查方案。
- Revision：必须与“测试后返回修改”一起表达。
- Overall energy：完全由用户自报。

#### Modified

- 探索数量只说“查看了 N 条消息”，不再映射 empathy 或能力。
- Pin 只说明玩家选择保留了哪些团队声音。
- Revision 从单纯次数改为“测试后返回修改，并重新试用”。
- Preference 改为实际顺序与布局观察句。

#### Deleted

- `5/5` performance score。
- “先调查再动手型 / 异常预判型 / 核心任务守门员”等主要报告标签。
- “点更多物件 = 更好的设计师”的隐含关系。
- 从用户操作推断喜欢或烦；改为玩家自报具体阶段。

### Browser regression after refinement

| Path | Result |
|---|---|
| First-time / unknown rules | 通过。开场不再规定调查顺序；目标只说明 Mia 要完成的事。 |
| Fast action / few clues | 通过。0 条消息也可设计；缺 loading 时先经历 3.2 秒无变化，再出现 Mia 疑问。 |
| Thorough investigation | 通过。6 条消息可按任意顺序查看；完整布局顺利经过输入、等待、依据和失败恢复。 |
| Missing recovery → revision → retest | 通过。首次失败先形成 dead end；加入 Retry 后第二次试用完成；报告记录 2 次试用、1 次修改。 |
| Deliberately bad design | 通过。History / Templates 占据首屏时，Mia 先寻找入口，1.7 秒后明确说不知道从哪里开始。 |
| Engineering constraint | 通过。第二个“需要开发”行为被阻止，并说明必须先替换当前开发项。 |
| Space constraint | 通过。首屏达到 2/2 后不再显示放置入口，不出现虚假的总完成分。 |
| Mobile 390×844 | 通过。无水平溢出；dialog 完整可见；topbar 为 static；点击放置可完成。 |
| Reset | 通过。布局、消息、试用、反思状态全部清空并回到 briefing。 |
| Refresh | 通过。稳定回到初始 briefing，无残留错误。 |
| Console / page errors | **0**，所有回归路径。 |

### Review conclusion

修改后核心链路已从：

```text
读任务 → 收集规定数量线索 → 选正确五组件 → 系统判分
```

转为：

```text
进入临时工位 → 自己发现团队信息 → 在真实空间和工程限制下调整页面
→ Mia 实际试用 → 缺陷产生可见后果 → 返回修改 → 再试
→ 回放“你刚才做了什么”，并学习这些动作在设计工作中的名称
```

当前版本已经能让 AI 小白理解“体验设计不是把 UI 画漂亮，而是听取不同信息、组织流程、预判 AI 状态、观察用户并反复修改”。它仍是低保真单场景验证，不应被视为已经完成真实用户验证。
