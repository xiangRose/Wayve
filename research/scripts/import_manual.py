#!/usr/bin/env python3
"""从 CSV 批量导入手采样本（BOSS / 小红书 / 访谈等）。

CSV 表头（UTF-8）:
  jobId,source,sourceUrl,title,company,level,rawText

用法:
  python import_manual.py samples.csv
  python import_manual.py samples.csv --dry-run
"""

from __future__ import annotations

import argparse
import csv
import json
import sys
from datetime import date
from pathlib import Path

import yaml

ROOT = Path(__file__).resolve().parents[1]
RAW_DIR = ROOT / "raw"

VALID_SOURCES = {"boss", "company_site", "linkedin", "xhs", "interview", "other"}
VALID_LEVELS = {"junior", "mid", "senior", "unknown"}

LEVEL_ALIASES = {
    "junior": {"junior", "初级", "入门", "应届", "1-3年", "1–3年", "1-3 年", "本科1-3年", "专科1-3年"},
    "mid": {"mid", "中级", "3-5年", "3–5年", "3-5 年"},
    "senior": {"senior", "高级", "资深", "专家", "5年以上", "5年+", "5-10年"},
    "unknown": {"unknown", "unknow", "不详", "未知", ""},
}


def open_csv_reader(path: Path) -> tuple[csv.DictReader, str]:
    data = path.read_bytes()
    last_error: Exception | None = None
    for encoding in ("utf-8-sig", "utf-8", "gbk", "gb18030"):
        try:
            text = data.decode(encoding)
            reader = csv.DictReader(text.splitlines())
            if reader.fieldnames:
                return reader, encoding
        except Exception as exc:
            last_error = exc
    raise UnicodeDecodeError("csv", b"", 0, 1, str(last_error))


def normalize_level(raw: str) -> str:
    value = (raw or "").strip().lower()
    if value in VALID_LEVELS:
        return value
    for level, aliases in LEVEL_ALIASES.items():
        if value in {a.lower() for a in aliases}:
            return level
    if any(token in value for token in ("1-3", "1–3", "应届", "初级")):
        return "junior"
    if any(token in value for token in ("3-5", "3–5", "中级")):
        return "mid"
    if any(token in value for token in ("5年", "资深", "高级", "专家")):
        return "senior"
    return "unknown"


def row_text(row: dict) -> str:
    parts = [
        (row.get("rawText") or "").strip(),
        (row.get("title") or "").strip(),
        (row.get("company") or "").strip(),
        (row.get("level") or "").strip(),
    ]
    return "\n".join(p for p in parts if p)


def merge_broken_rows(rows: list[dict]) -> list[dict]:
    """合并 Excel 导出时因换行被拆碎的续行（jobId 为空）。"""
    merged: list[dict] = []
    for row in rows:
        job_id = (row.get("jobId") or "").strip()
        if job_id:
            merged.append(dict(row))
            continue
        if not merged:
            continue
        extra = row_text(row)
        if extra:
            prev = merged[-1]
            prev["rawText"] = f"{(prev.get('rawText') or '').strip()}\n{extra}".strip()
    return merged


def load_valid_job_ids() -> set[str]:
    with (ROOT / "config" / "jobs.yaml").open(encoding="utf-8") as f:
        return set(yaml.safe_load(f)["jobs"].keys())


def next_sample_id(job_id: str) -> str:
    job_dir = RAW_DIR / job_id
    job_dir.mkdir(parents=True, exist_ok=True)
    return f"{job_id}_{len(list(job_dir.glob('*.json'))) + 1:03d}"


def import_row(row: dict, valid_jobs: set[str], dry_run: bool) -> str | None:
    job_id = (row.get("jobId") or "").strip()
    source = (row.get("source") or "other").strip()
    raw_text = (row.get("rawText") or "").strip()

    if job_id not in valid_jobs:
        return f"跳过：无效 jobId {job_id}"
    if source not in VALID_SOURCES:
        return f"跳过：无效 source {source}"
    if len(raw_text) < 50:
        return f"跳过：rawText 不足 50 字 ({job_id})"

    level = normalize_level(row.get("level") or "")
    if level == "unknown":
        level = normalize_level(row.get("company") or "")

    sample_id = next_sample_id(job_id)
    payload = {
        "sampleId": sample_id,
        "jobId": job_id,
        "source": source,
        "sourceUrl": (row.get("sourceUrl") or "").strip(),
        "crawledAt": date.today().isoformat(),
        "title": (row.get("title") or "").strip() or sample_id,
        "company": (row.get("company") or "").strip(),
        "level": level,
        "rawText": raw_text,
        "crawlMethod": "manual",
        "extracted": {},
    }

    out = RAW_DIR / job_id / f"{sample_id}.json"
    if dry_run:
        return f"[dry-run] 将写入 {out}"
    out.write_text(json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8")
    return f"已写入 {out}"


def main() -> int:
    parser = argparse.ArgumentParser(description="CSV 导入手采岗位样本")
    parser.add_argument("csv_path", type=Path)
    parser.add_argument("--dry-run", action="store_true")
    args = parser.parse_args()

    if not args.csv_path.exists():
        print(f"文件不存在: {args.csv_path}", file=sys.stderr)
        return 1

    valid_jobs = load_valid_job_ids()
    try:
        reader, encoding = open_csv_reader(args.csv_path)
    except UnicodeDecodeError:
        print("无法识别 CSV 编码，请让同学另存为 CSV UTF-8", file=sys.stderr)
        return 1

    print(f"使用编码: {encoding}")
    required = {"jobId", "source", "rawText"}
    if not required.issubset(reader.fieldnames or []):
        print(f"CSV 缺少列: {required - set(reader.fieldnames or [])}", file=sys.stderr)
        return 1

    rows = merge_broken_rows(list(reader))
    for row in rows:
        msg = import_row(row, valid_jobs, args.dry_run)
        if msg:
            print(msg)
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
