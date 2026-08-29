#!/usr/bin/env python3
"""合并多人提交的 CSV，去重后导入 raw/。

用法:
  python merge_and_import.py ..\incoming\手采_小王_ai_product.csv ..\incoming\手采_小李_ai_ui_design.csv
  python merge_and_import.py ..\incoming\*.csv --dry-run
"""

from __future__ import annotations

import argparse
import csv
import hashlib
import subprocess
import sys
import tempfile
from pathlib import Path

SCRIPT_DIR = Path(__file__).resolve().parent
IMPORT_SCRIPT = SCRIPT_DIR / "import_manual.py"


def text_hash(text: str) -> str:
    return hashlib.md5(text.strip().encode("utf-8")).hexdigest()


def merge_csv_files(paths: list[Path], out_path: Path) -> tuple[int, int]:
    fieldnames = ["jobId", "source", "sourceUrl", "title", "company", "level", "rawText"]
    seen: set[str] = set()
    rows: list[dict] = []
    skipped = 0

    for path in paths:
        with path.open(encoding="utf-8-sig", newline="") as f:
            reader = csv.DictReader(f)
            for row in reader:
                raw = (row.get("rawText") or "").strip()
                if len(raw) < 50:
                    skipped += 1
                    continue
                h = text_hash(raw)
                if h in seen:
                    skipped += 1
                    continue
                seen.add(h)
                rows.append({k: (row.get(k) or "").strip() for k in fieldnames})

    with out_path.open("w", encoding="utf-8-sig", newline="") as f:
        writer = csv.DictWriter(f, fieldnames=fieldnames)
        writer.writeheader()
        writer.writerows(rows)

    return len(rows), skipped


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("csv_files", nargs="+", type=Path)
    parser.add_argument("--dry-run", action="store_true")
    args = parser.parse_args()

    paths = []
    for p in args.csv_files:
        if p.exists():
            paths.append(p)
        else:
            paths.extend(Path().glob(str(p)))

    if not paths:
        print("未找到 CSV 文件", file=sys.stderr)
        return 1

    with tempfile.NamedTemporaryFile(
        mode="w", suffix=".csv", delete=False, encoding="utf-8-sig", newline=""
    ) as tmp:
        merged_path = Path(tmp.name)

    total, skipped = merge_csv_files(paths, merged_path)
    print(f"合并 {len(paths)} 个文件 → {total} 条有效样本（跳过重复/过短 {skipped} 条）")

    if args.dry_run:
        print(f"合并文件: {merged_path}")
        return 0

    result = subprocess.run(
        [sys.executable, str(IMPORT_SCRIPT), str(merged_path)],
        cwd=SCRIPT_DIR,
    )
    merged_path.unlink(missing_ok=True)
    return result.returncode


if __name__ == "__main__":
    raise SystemExit(main())
