#!/usr/bin/env python3
"""将 aggregated/*.summary.json 同步到 src/main/resources/seed/jobs.json。

保留 seed 里已有的 taskStatus、estimatedMinutes（Demo 配置，不由 LLM 生成）。

用法:
  python sync_aggregated_to_seed.py
  python sync_aggregated_to_seed.py --dry-run
"""

from __future__ import annotations

import argparse
import json
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
AGG_DIR = ROOT / "aggregated"
SEED_PATH = ROOT.parent / "src" / "main" / "resources" / "seed" / "jobs.json"

JOB_ORDER = [
    "ai_product",
    "ai_ops",
    "ai_data_eval",
    "ai_app_dev",
    "ai_ui_design",
]

SEED_FIELDS = [
    "jobId",
    "name",
    "definition",
    "coreWorkObject",
    "typicalWorkSnippet",
    "whyExperience",
    "estimatedMinutes",
    "taskStatus",
    "competencyRequirements",
    "specificCompetencies",
]


def load_existing_seed() -> dict[str, dict]:
    if not SEED_PATH.exists():
        return {}
    data = json.loads(SEED_PATH.read_text(encoding="utf-8"))
    return {j["jobId"]: j for j in data.get("jobs", [])}


def load_aggregated(job_id: str) -> dict | None:
    path = AGG_DIR / f"{job_id}.summary.json"
    if not path.exists():
        return None
    return json.loads(path.read_text(encoding="utf-8"))


def merge_job(job_id: str, agg: dict, existing: dict | None) -> dict:
    prev = existing or {}
    return {
        "jobId": job_id,
        "name": agg.get("name", prev.get("name", job_id)),
        "definition": agg["definition"],
        "coreWorkObject": agg["coreWorkObject"],
        "typicalWorkSnippet": agg["typicalWorkSnippet"],
        "whyExperience": agg["whyExperience"],
        "estimatedMinutes": prev.get("estimatedMinutes", 7),
        "taskStatus": prev.get("taskStatus", "preview_only"),
        "competencyRequirements": agg.get("competencyRequirements", {}),
        "specificCompetencies": agg.get("specificCompetencies", []),
    }


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--dry-run", action="store_true")
    args = parser.parse_args()

    existing = load_existing_seed()
    jobs: list[dict] = []
    missing: list[str] = []

    for job_id in JOB_ORDER:
        agg = load_aggregated(job_id)
        if agg is None:
            missing.append(job_id)
            if job_id in existing:
                jobs.append(existing[job_id])
            continue
        jobs.append(merge_job(job_id, agg, existing.get(job_id)))

    if missing:
        print(f"警告: 缺少归纳文件: {', '.join(missing)}", file=sys.stderr)

    output = {"jobs": jobs}
    text = json.dumps(output, ensure_ascii=False, indent=2) + "\n"

    if args.dry_run:
        print(text)
        return 0

    SEED_PATH.write_text(text, encoding="utf-8")
    print(f"已同步 {len(jobs)} 个岗位 → {SEED_PATH}")
    print("若后端已启动过，请删除 data/jobsearch.* 后重启以重新灌库。")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
