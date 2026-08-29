#!/usr/bin/env python3
"""从公开来源写入五岗调研样本（腾讯招聘 SPA 无法直接爬 HTML 时的补充方案）。

用法:
  python seed_public_jobs.py
"""

from __future__ import annotations

import json
from datetime import date
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
RAW = ROOT / "raw"

SAMPLES = [
    {
        "jobId": "ai_product",
        "source": "company_site",
        "sourceUrl": "https://careers.tencent.com/jobdesc.html?postId=2053645695394689024",
        "title": "《王者荣耀》-技术策划-AI工具方向",
        "company": "腾讯",
        "rawText": """岗位职责：
1. 负责探索游戏行业 AI 提效工作流程相关能力，探索应用场景，设计平台管线；
2. 负责已有工具管线的 AI 化，将 AI 赋能到常用编辑器以及日常使用的工作平台中；
3. 负责推进 UGC+AI 方向上 AI 能力的探索与扩展；
4. 根据各个职能需求推动 AI 能力产品化，提升游戏研发效率和 AI 在项目内的落地推广。

岗位要求：
1. 熟悉游戏行业研发的基本流程，对游戏行业有浓厚兴趣；
2. 2 年以上 AIGC 产品或游戏岗位推动 AIGC 落地的类似工作经验；
3. 深入理解 AI 相关技术的发展和能力边界，有深度 AI 工具使用习惯；
4. 优化 Agent 能力，有 Agent 实操经验；
5. 具备优秀的产品规划能力，能独立完成竞品对比、需求分析及原型设计。""",
    },
    {
        "jobId": "ai_product",
        "source": "company_site",
        "sourceUrl": "https://careers.tencent.com/",
        "title": "微信读书产品部-AI产品经理",
        "company": "腾讯",
        "rawText": """岗位职责：
1. 负责读书产品部新应用的产品策划工作，涵盖 IM 群聊、AI Agent、实时协同等；
2. 结合前沿技术发展趋势，探索 AI 在办公协作/项目管理等场景的创新落地方案；
3. 洞察挖掘用户对 AI 的真实需求和具体场景，设计解决方案并落地；
4. 跟进并理解大模型等相关技术发展，深刻理解模型进化带来的改变，在产品的设计中充分应用模型能力。""",
    },
    {
        "jobId": "ai_product",
        "source": "company_site",
        "sourceUrl": "https://careers.tencent.com/",
        "title": "微信输入法-AI产品策划",
        "company": "腾讯",
        "rawText": """岗位职责：
1. 负责微信输入法 AI 方向的产品策划，围绕大语言模型（LLM）、Agent、多模态交互、语音识别与合成（ASR）等前沿技术，探索 AI 在文字输入、智能表达、语音交互、内容创作等核心场景的创新落地方案；
2. 深度挖掘用户在输入全链路中对 AI 的真实需求和使用场景，设计端到端的产品解决方案并推动落地，持续提升输入效率与表达体验；
3. 结合业务目标定义模型效果标准和评测体系，协同算法、工程、设计团队进行模型迭代优化与数据飞轮建设；
4. 持续跟踪大模型与 AI 交互领域的前沿趋势及竞品动态，提出创新性产品策略，强化微信输入法的 AI 产品心智。""",
    },
    {
        "jobId": "ai_ops",
        "source": "company_site",
        "sourceUrl": "https://careers.tencent.com/jobdesc.html?postId=2077724655581577216",
        "title": "微信小店-生态治理产品运营",
        "company": "腾讯",
        "rawText": """岗位职责：
1. 负责电商直播内容治理及达人工作，通过治理工作降低用户负反馈；
2. 建立直播间治理的基建流程；
3. 与各方合作应对各类治理紧急事项。

岗位要求：
1. 本科以上学历，优先考虑具有电商直播治理等相关工作经验的候选人；
2. 善于制定规则和定性分析，对于研究规则问题有足够的耐心和探索欲；
3. 具有较强数据分析能力，能够熟练掌握 SQL、Excel 等数据分析工具；
4. 跨团队沟通能力强，能够与合作方有效沟通和协作，共同推进治理工作；
5. 能独挡一面，具有较好的工作承压能力。""",
    },
    {
        "jobId": "ai_ops",
        "source": "company_site",
        "sourceUrl": "https://careers.tencent.com/",
        "title": "微信小店-产品策划-推荐方向",
        "company": "腾讯",
        "rawText": """岗位职责：
1. 负责电商推荐工作，对用户需求进行挖掘和抽象、输出方案和策略、有效提升用户体验和效率；
2. 对微信内商业场景进行深入洞察，基于平台价值观和目标、提出有效策略促进生态中不同角色共同良性发展。""",
    },
    {
        "jobId": "ai_data_eval",
        "source": "company_site",
        "sourceUrl": "https://careers.tencent.com/",
        "title": "微信基础-大模型评测产品经理",
        "company": "腾讯",
        "rawText": """岗位职责：
1. 评测体系设计：主导 AI 产品的评测体系搭建，定义评测维度、指标、标准和流程，确保评测的科学性和全面性；
2. 评测方法论制定：设计适配不同场景（对话、工具调用、多轮交互、创意生成等）的评测方案，包括自动评测、人工评测、众包评测、竞品对比等；
3. 评测平台产品设计：规划评测平台的产品架构和功能模块，输出 PRD，驱动研发团队落地；
4. 评测洞察与决策支持：深度分析评测数据，发现产品体验问题和模型能力短板，输出有洞察力的评测报告，推动产品和算法团队优化；
5. 竞品评测与行业跟踪：持续跟踪业界竞品的能力变化，建立常态化竞品评测机制；
6. 标注体系管理：设计标注规范和质量控制流程，确保评测数据质量。""",
    },
    {
        "jobId": "ai_data_eval",
        "source": "company_site",
        "sourceUrl": "https://careers.tencent.com/jobdesc.html?postId=2021521585860673536",
        "title": "微信AI-小微对话效果与评测",
        "company": "腾讯",
        "rawText": """岗位职责：
1. 支持微信助手小微的产品需求，负责小微对话效果优化，覆盖闲聊、角色扮演和微信会话场景小微问答等场景，围绕拟人化、共情、话题引导和场景理解，推进数据构建、模型后训练、效果评测和线上实验；
2. 优化小微生成式追问推荐系统，基于线上曝光、点击和多轮对话数据构建训练样本，建设数据飞轮、自动评测与 A/B 实验，持续提升追问的相关性、多样性、个性化和点击转化；
3. 负责经验簿自进化系统的建设与优化，包括经验生成、触发与过滤、动态评测、多轮生效及线上反馈回流，持续提升经验的准召和业务效果；
4. 负责人脸记忆与人物图谱能力优化，围绕人物绑定、检索和识别开展数据合成、主干后训练、评测建设和 bad case 归因，降低识别幻觉；
5. 建设小微对话、harness、记忆等方向相关的数据与评测工具，与产品和工程团队协作，推进算法能力上线落地。""",
    },
    {
        "jobId": "ai_app_dev",
        "source": "company_site",
        "sourceUrl": "https://careers.tencent.com/jobdesc.html?postId=2076924809798922240",
        "title": "金融科技-大模型Agent应用研发",
        "company": "腾讯",
        "rawText": """岗位职责：
1. 业务落地与技术攻坚：参与腾讯金融科技大模型在智能投顾、智能运营、智能客服、搜索推荐等核心金融场景的落地与迭代；
2. 前沿探索与标准沉淀：持续追踪大模型及 AI Agent 领域的前沿技术（如多智能体协同、Tool Learning、Skills、RAG 进阶架构等），结合金融业务痛点沉淀技术方法论。

岗位要求：
1. 计算机、人工智能或相关专业硕士及以上学历，具备较多的大模型（LLM）项目落地经验；
2. 深入理解 Agent 核心范式与组件，包括 Agent Loop、Tool Use、Skills、MCP、Memory 机制、RAG、ReAct、Plan-and-Execute 等；
3. 熟悉 Qwen、DeepSeek、Gemini、Claude 等主流大模型，对 LLM 全技术栈（数据处理、SFT、RLHF/DPO 等）有体系化认知；
4. 熟练掌握 vLLM、TensorRT-LLM、LangChain 等框架，能独立主导模型训练、微调（LoRA/PEFT）及高性能推理部署；
5. 能够构建完善的 LLM 评估体系，擅长通过 A/B Test 验证算法收益。""",
    },
    {
        "jobId": "ai_app_dev",
        "source": "company_site",
        "sourceUrl": "https://jobs.bytedance.com/experienced/position/7323089843444599091/detail",
        "title": "豆包AI大模型产品解决方案架构师-火山方舟",
        "company": "字节跳动",
        "rawText": """职位描述：
团队介绍：火山方舟是火山引擎推出的一站式大模型服务平台，提供模型推理、评测、精调等全流程服务，搭载豆包及业界主流大模型，提供丰富的插件生态和 AI 应用开发服务。

岗位职责：
1. 在各领域、各场景制定火山方舟的解决方案，并进行商业化落地；
2. 对外负责与业务拓展部门紧密合作，共同推进产品及解决方案拓展/销售过程，包括方案交流、需求分析、解决方案设计、商务合同签订、项目交付、项目验收等；
3. 对内负责与产品及研发部门沟通协调，促进解决方案落地及产品迭代，包括商务模式、项目进度、产品及服务交付等。

职位要求：
1. 本科及以上学历，计算机、通信、人工智能等相关专业优先；
2. 5 年以上 AI to B 解决方案或项目管理工作经验，独立运作过项目解决方案；
3. 具备良好的项目管理能力及组织协调能力，数字导向；
4. 了解生成式人工智能的最新行业动态、应用方向，对生成式人工智能短、中、长期在各领域落地的路径和可行性有自己的判断。""",
    },
    {
        "jobId": "ai_ui_design",
        "source": "company_site",
        "sourceUrl": "https://jobs.bytedance.com/en/position/7674908085597571381/detail",
        "title": "AI-native UX Designer",
        "company": "字节跳动",
        "rawText": """Responsibilities:
- Lead the interaction design for AI-native products and system-level software, exploring new interaction paradigms for the AI era.
- Define cross-device consistency frameworks for ecosystem scenarios, primarily focusing on mobile and multi-device collaboration.
- Abstract complex product logic, AI Agent workflows, and multimodal inputs/outputs into clear, natural, and implementable interaction experiences.
- Drive 0-to-1 exploratory projects from problem definition and conceptual design to prototype validation and full-cycle implementation.
- Collaborate closely with product, algorithm, engineering, visual, motion, and hardware teams to transform concepts into production-ready solutions.

Minimum Qualifications:
- 5 years of experience in UX design, experience design, or a related field.
- Demonstrated system-level thinking with the ability to define experience problems from an ecosystem perspective rather than isolated pages or flows.
- Proven experience leading ambiguous or 0-to-1 exploratory design projects.
- Proficiency with modern design tools (e.g., Figma).

Preferred Qualifications:
- Experience designing for AI products, mobile OS, system-level software, or multimodal interfaces (voice/gesture).
- Advanced prototyping skills using tools such as ProtoPie, Framer, SwiftUI, or AI-assisted coding.""",
    },
    {
        "jobId": "ai_ui_design",
        "source": "company_site",
        "sourceUrl": "https://careers.tencent.com/",
        "title": "微信视频号-互动模块产品策划",
        "company": "腾讯",
        "rawText": """岗位职责：
1. 负责微信视频号互动模块的产品策划，包括互动功能的策略制定、原型设计及推动上线；
2. 以提升用户互动意愿为核心目标，设计并优化互动功能体验，根据用户 feedback 和数据表现不断调优；
3. 深入分析用户需求与行为，挖掘互动场景机会点，提出创新性功能方案并推动验证落地；
4. 协同设计、研发等合作团队高效推进项目，确保产品按时高质量交付。""",
    },
]


def next_id(job_id: str) -> str:
    job_dir = RAW / job_id
    job_dir.mkdir(parents=True, exist_ok=True)
    n = len(list(job_dir.glob("*.json"))) + 1
    return f"{job_id}_{n:03d}"


def main() -> None:
    written = 0
    for item in SAMPLES:
        job_id = item["jobId"]
        sample_id = next_id(job_id)
        payload = {
            "sampleId": sample_id,
            "jobId": job_id,
            "source": item["source"],
            "sourceUrl": item["sourceUrl"],
            "crawledAt": date.today().isoformat(),
            "title": item["title"],
            "company": item["company"],
            "level": "unknown",
            "rawText": item["rawText"].strip(),
            "crawlMethod": "auto",
            "extracted": {},
        }
        out = RAW / job_id / f"{sample_id}.json"
        out.write_text(json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8")
        written += 1
        print(f"已写入 {out}")

    print(f"\n共写入 {written} 条公开样本")


if __name__ == "__main__":
    main()
