#!/usr/bin/env python3
"""从公开 URL 抓取正文，保存为调研样本 JSON。

适用：企业官网 Career Page、公开访谈文章等（反爬弱的页面）。
不适用：BOSS 直聘、小红书（请用手采 + import_manual.py）。

用法:
  python collect_url.py --job-id ai_product --source company_site --url "https://..."
"""

from __future__ import annotations

import argparse
import json
import re
import sys
from datetime import date
from pathlib import Path
from urllib.parse import urlparse

import requests
import trafilatura
import yaml
from bs4 import BeautifulSoup

ROOT = Path(__file__).resolve().parents[1]
RAW_DIR = ROOT / "raw"

HEADERS = {
    "User-Agent": (
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
        "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/127.0.0.0 Safari/537.36"
    ),
    "Accept-Language": "zh-CN,zh;q=0.9,en;q=0.8",
}


def load_valid_job_ids() -> set[str]:
    config_path = ROOT / "config" / "jobs.yaml"
    with config_path.open(encoding="utf-8") as f:
        data = yaml.safe_load(f)
    return set(data["jobs"].keys())


def slugify(text: str, max_len: int = 40) -> str:
    cleaned = re.sub(r"[^\w\u4e00-\u9fff-]+", "_", text.strip())
    return cleaned[:max_len] or "page"


def fetch_text(url: str, timeout: int = 20) -> tuple[str, str]:
    resp = requests.get(url, headers=HEADERS, timeout=timeout)
    resp.raise_for_status()
    resp.encoding = resp.apparent_encoding or "utf-8"
    html = resp.text

    if "<div id=app></div>" in html or 'id="app"></div>' in html:
        return "", html

    try:
        extracted = trafilatura.extract(html, include_comments=False)
        if extracted and len(extracted.strip()) >= 50:
            return extracted.strip(), html
    except Exception:
        pass

    soup = BeautifulSoup(html, "lxml")
    for tag in soup(["script", "style", "nav", "footer", "header"]):
        tag.decompose()
    text = soup.get_text("\n", strip=True)
    if len(text.strip()) >= 50:
        return text.strip(), html
    return html[:8000], html


def next_sample_id(job_id: str) -> str:
    job_dir = RAW_DIR / job_id
    job_dir.mkdir(parents=True, exist_ok=True)
    existing = list(job_dir.glob("*.json"))
    return f"{job_id}_{len(existing) + 1:03d}"


def save_sample(
    job_id: str,
    source: str,
    url: str,
    raw_text: str,
    title: str = "",
    company: str = "",
) -> Path:
    sample_id = next_sample_id(job_id)
    host = urlparse(url).netloc.replace("www.", "")
    payload = {
        "sampleId": sample_id,
        "jobId": job_id,
        "source": source,
        "sourceUrl": url,
        "crawledAt": date.today().isoformat(),
        "title": title or sample_id,
        "company": company or host,
        "level": "unknown",
        "rawText": raw_text,
        "crawlMethod": "auto",
        "extracted": {},
    }
    out = RAW_DIR / job_id / f"{sample_id}.json"
    out.write_text(json.dumps(payload, ensure_ascii=False, indent=2), encoding="utf-8")
    return out


def main() -> int:
    parser = argparse.ArgumentParser(description="抓取公开 URL 为岗位调研样本")
    parser.add_argument("--job-id", required=True, help="ai_product / ai_ui_design / ...")
    parser.add_argument(
        "--source",
        default="company_site",
        choices=["boss", "company_site", "linkedin", "xhs", "interview", "other"],
    )
    parser.add_argument("--url", required=True)
    parser.add_argument("--title", default="")
    parser.add_argument("--company", default="")
    args = parser.parse_args()

    valid_ids = load_valid_job_ids()
    if args.job_id not in valid_ids:
        print(f"无效 job-id: {args.job_id}，可选: {sorted(valid_ids)}", file=sys.stderr)
        return 1

    if "zhipin.com" in args.url or "xiaohongshu.com" in args.url:
        print(
            "BOSS/小红书反爬强，请浏览器打开后复制正文，用 import_manual.py 导入。",
            file=sys.stderr,
        )
        return 1

    try:
        text, html = fetch_text(args.url)
    except requests.RequestException as exc:
        print(f"抓取失败: {exc}", file=sys.stderr)
        return 1

    if not text.strip():
        print(
            "检测到前端渲染页面（如腾讯招聘），HTML 无 JD 正文。"
            "请运行: python seed_public_jobs.py 或改用手采 CSV。",
            file=sys.stderr,
        )
        return 1

    if len(text.strip()) < 50:
        print("正文过短，可能页面需登录、反爬或为前端渲染空壳，请改用手采或 seed_public_jobs.py。", file=sys.stderr)
        return 1

    out = save_sample(args.job_id, args.source, args.url, text, args.title, args.company)
    print(f"已保存: {out} ({len(text)} 字)")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
