#!/usr/bin/env python3
"""按 urls_to_crawl.txt 批量自动抓取。

用法:
  python batch_collect.py ../templates/urls_to_crawl.txt
  python batch_collect.py ../templates/urls_to_crawl.txt --dry-run
"""

from __future__ import annotations

import argparse
import subprocess
import sys
from pathlib import Path

SCRIPT = Path(__file__).resolve().parent / "collect_url.py"


def parse_line(line: str) -> tuple[str, str, str, str, str] | None:
    line = line.strip()
    if not line or line.startswith("#"):
        return None
    parts = line.split("\t") if "\t" in line else line.split()
    if len(parts) < 3:
        return None
    job_id, source, url = parts[0], parts[1], parts[2]
    title = parts[3] if len(parts) > 3 else ""
    company = parts[4] if len(parts) > 4 else ""
    return job_id, source, url, title, company


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("list_file", type=Path)
    parser.add_argument("--dry-run", action="store_true")
    args = parser.parse_args()

    if not args.list_file.exists():
        print(f"文件不存在: {args.list_file}", file=sys.stderr)
        return 1

    ok, fail = 0, 0
    for line in args.list_file.read_text(encoding="utf-8").splitlines():
        parsed = parse_line(line)
        if not parsed:
            continue
        job_id, source, url, title, company = parsed
        cmd = [
            sys.executable,
            str(SCRIPT),
            "--job-id",
            job_id,
            "--source",
            source,
            "--url",
            url,
        ]
        if title:
            cmd.extend(["--title", title])
        if company:
            cmd.extend(["--company", company])

        if args.dry_run:
            print(" ".join(cmd))
            continue

        result = subprocess.run(cmd, capture_output=True, text=True)
        if result.returncode == 0:
            ok += 1
            print(result.stdout.strip())
        else:
            fail += 1
            print(result.stderr.strip() or result.stdout.strip(), file=sys.stderr)

    if not args.dry_run:
        print(f"\n完成: 成功 {ok}, 失败 {fail}")
    return 0 if fail == 0 or ok > 0 else 1


if __name__ == "__main__":
    raise SystemExit(main())
