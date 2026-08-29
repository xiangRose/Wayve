"""Parse tmp-5jobs-questions.txt into microtask-bank.json and task template files."""
import json
import re
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SRC = ROOT / "tmp-5jobs-questions.txt"
OUT_BANK = ROOT / "src/main/resources/seed/microtask-bank.json"
OUT_TEMPLATES = ROOT / "src/main/resources/seed/task-templates"
OUT_FRONT = ROOT / "front/data/microtask-bank.json"

JOB_MARKERS = [
    ("一、AI 产品", "ai_pm", "AI 产品经理"),
    ("二、AI UI设计", "ai_ux", "AI 产品设计（UI/UX）"),
    ("三、AI运营", "ai_operator", "AI 产品运营"),
    ("四、AI数据与评测", "ai_researcher", "AI 数据与评测"),
    ("五、AI应用开发", "ai_consultant", "AI 应用开发"),
]

TIME_RE = r"(?<![0-9])(\d{1,2}:\d{2})"
SET_SPLIT_RE = re.compile(r"([A-D])套｜([^0-9]+?)(?=" + TIME_RE + "|[A-D]套｜|$)")
QUESTION_SPLIT_RE = re.compile(
    r"(\d{1,2}:\d{2})([^｜]+?)｜([^“]+?)“([^”]+)”(?:“([^”]+)”)?"
    r"([A-D]\..+?)(?=对应维度：([^0-9A-D套]+?)(?=\d{1,2}:\d{2}|[A-D]套｜|$))"
)
OPTION_RE = re.compile(r"([A-D])\.\s*(.+?)【(\d)】")


def parse_question_block(block: str):
    m = re.match(
        r"(?<![0-9])(\d{1,2}:\d{2})([^｜“]+?)(?:｜([^“]+?))?“([^”]+)”(?:“([^”]+)”)?(.*)",
        block,
        re.DOTALL,
    )
    if not m:
        return None
    time, speaker, role, message, prompt, rest = (
        m.group(1),
        m.group(2),
        m.group(3) or "",
        m.group(4),
        m.group(5) or "",
        m.group(6),
    )
    dim_m = re.search(r"对应维度：(.+?)(?=$)", rest)
    dimension = dim_m.group(1).strip() if dim_m else ""
    options_part = rest[: dim_m.start()] if dim_m else rest
    options = []
    for om in OPTION_RE.finditer(options_part):
        options.append(
            {"id": om.group(1), "label": om.group(2), "score": int(om.group(3))}
        )
    if len(options) != 4:
        return None
    return {
        "time": time,
        "speaker": speaker,
        "speakerRole": role,
        "message": message,
        "prompt": prompt,
        "dimension": dimension,
        "options": options,
    }


def parse_job_section(text: str, job_id: str, name: str) -> dict:
    sets: dict = {}
    for sm in SET_SPLIT_RE.finditer(text):
        set_id = sm.group(1)
        theme = sm.group(2).strip()
        body = text[sm.end():]
        next_set = SET_SPLIT_RE.search(body)
        set_text = body[: next_set.start()] if next_set else body
        questions = []
        parts = re.split(r"(?=(?<![0-9])\d{1,2}:\d{2})", set_text)
        for part in parts:
            part = part.strip()
            if not part:
                continue
            q = parse_question_block(part)
            if q:
                questions.append(q)
        sets[set_id] = {"setId": set_id, "theme": theme, "questions": questions}
    return {"jobId": job_id, "name": name, "sets": sets}


def parse(text: str) -> dict:
    jobs: dict = {}
    for i, (marker, job_id, name) in enumerate(JOB_MARKERS):
        start = text.find(marker)
        if start < 0:
            continue
        end = len(text)
        if i + 1 < len(JOB_MARKERS):
            next_marker = JOB_MARKERS[i + 1][0]
            next_pos = text.find(next_marker, start + len(marker))
            if next_pos > 0:
                end = next_pos
        section = text[start:end]
        # strip header before first set
        first_set = re.search(r"[A-D]套｜", section)
        if first_set:
            section = section[first_set.start():]
        jobs[job_id] = parse_job_section(section, job_id, name)
    return {"jobs": jobs}


def to_template(job: dict, set_id: str = "A") -> dict:
    set_data = job["sets"][set_id]
    steps = []
    for i, q in enumerate(set_data["questions"], start=1):
        steps.append(
            {
                "stepNumber": i,
                "stepTitle": f"微任务 {i}",
                "stepType": "microtask_choice",
                "time": q["time"],
                "speaker": q["speaker"],
                "speakerRole": q["speakerRole"],
                "message": q["message"],
                "prompt": q["prompt"],
                "dimension": q["dimension"],
                "options": q["options"],
                "decision": {
                    "type": "single_choice",
                    "prompt": q["prompt"],
                    "options": q["options"],
                },
            }
        )
    return {
        "jobId": job["jobId"],
        "scaffoldType": "career_changer",
        "title": set_data["theme"],
        "scenario": set_data["theme"],
        "setId": set_id,
        "estimatedMinutes": 7,
        "steps": steps,
    }


def public_bank(bank: dict) -> dict:
    out = {"jobs": {}}
    for job_id, job in bank["jobs"].items():
        pub_sets = {}
        for set_id, set_data in job["sets"].items():
            pub_sets[set_id] = {
                "setId": set_id,
                "theme": set_data["theme"],
                "questions": [
                    {
                        "time": q["time"],
                        "speaker": q["speaker"],
                        "speakerRole": q["speakerRole"],
                        "message": q["message"],
                        "prompt": q["prompt"],
                        "options": [
                            {"id": o["id"], "label": f"{o['id']}. {o['label']}"}
                            for o in q["options"]
                        ],
                    }
                    for q in set_data["questions"]
                ],
            }
        out["jobs"][job_id] = {
            "jobId": job_id,
            "name": job["name"],
            "sets": pub_sets,
        }
    return out


def main():
    text = SRC.read_text(encoding="utf-8")
    bank = parse(text)
    OUT_BANK.parent.mkdir(parents=True, exist_ok=True)
    OUT_BANK.write_text(json.dumps(bank, ensure_ascii=False, indent=2), encoding="utf-8")

    OUT_FRONT.parent.mkdir(parents=True, exist_ok=True)
    OUT_FRONT.write_text(
        json.dumps(public_bank(bank), ensure_ascii=False, indent=2),
        encoding="utf-8",
    )

    scores_bank = {"jobs": {}}
    for job_id, job in bank["jobs"].items():
        sets_out = {}
        for set_id, set_data in job["sets"].items():
            sets_out[set_id] = [
                {
                    "dimension": q["dimension"],
                    "options": q["options"],
                }
                for q in set_data["questions"]
            ]
        scores_bank["jobs"][job_id] = {"sets": sets_out}

    scores_path = ROOT / "front/data/microtask-scores.json"
    scores_path.write_text(json.dumps(scores_bank, ensure_ascii=False, indent=2), encoding="utf-8")

    for job_id, job in bank["jobs"].items():
        if "A" not in job["sets"]:
            print(f"WARN: no set A for {job_id}")
            continue
        template = to_template(job, "A")
        path = OUT_TEMPLATES / f"{job_id}.career_changer.json"
        path.write_text(json.dumps(template, ensure_ascii=False, indent=2), encoding="utf-8")
        print(f"Wrote {path} ({len(template['steps'])} steps)")

    for job_id, job in bank["jobs"].items():
        for set_id, set_data in job["sets"].items():
            print(f"  {job_id} set {set_id}: {len(set_data['questions'])} questions")

    print(f"Wrote {OUT_BANK}")
    print(f"Wrote {OUT_FRONT}")


if __name__ == "__main__":
    main()
