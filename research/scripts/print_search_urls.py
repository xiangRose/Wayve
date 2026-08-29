#!/usr/bin/env python3
"""打印各岗 BOSS 搜索链接与 Google 搜索提示，供手采时快速打开。

用法:
  python print_search_urls.py
  python print_search_urls.py --job-id ai_product
"""

from __future__ import annotations

import argparse
from urllib.parse import quote

import yaml

ROOT = __import__("pathlib").Path(__file__).resolve().parents[1]


def main() -> None:
    parser = argparse.ArgumentParser()
    parser.add_argument("--job-id", default="")
    args = parser.parse_args()

    with (ROOT / "config" / "jobs.yaml").open(encoding="utf-8") as f:
        config = yaml.safe_load(f)

    jobs = config["jobs"]
    if args.job_id:
        jobs = {args.job_id: jobs[args.job_id]}

    for job_id, meta in jobs.items():
        print(f"\n=== {meta['name']} ({job_id}) ===")
        for kw in meta.get("boss_keywords", []):
            url = f"https://www.zhipin.com/web/geek/job?query={quote(kw)}"
            print(f"  BOSS: {url}")
        for hint in meta.get("search_hints", []):
            g = f"https://www.google.com/search?q={quote(hint)}"
            print(f"  搜索: {g}")


if __name__ == "__main__":
    main()
