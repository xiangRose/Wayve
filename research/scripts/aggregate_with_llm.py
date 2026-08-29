#!/usr/bin/env python3
"""将某岗全部 raw 样本归纳为一篇 Role Spec（调用 OpenAI 兼容 API）。

需要环境变量: AI_API_KEY, 可选 AI_BASE_URL / AI_MODEL_PRO

用法:
  python aggregate_with_llm.py --job-id ai_product
  python aggregate_with_llm.py --job-id ai_product --dry-run
"""

from __future__ import annotations

import argparse
import json
import os
import sys
from pathlib import Path
from typing import Any

from dotenv import load_dotenv
from openai import OpenAI

ROOT = Path(__file__).resolve().parents[1]
RAW_DIR = ROOT / "raw"
AGG_DIR = ROOT / "aggregated"
PROMPT_PATH = ROOT / "prompts" / "03-jd-aggregation.md"


def load_samples(job_id: str) -> list[dict]:
    job_dir = RAW_DIR / job_id
    if not job_dir.exists():
        return []
    samples = []
    for path in sorted(job_dir.glob("*.json")):
        samples.append(json.loads(path.read_text(encoding="utf-8")))
    return samples


def build_user_message(job_id: str, samples: list[dict], job_name: str) -> str:
    blocks = [f"岗位: {job_name} ({job_id})", f"样本数: {len(samples)}", ""]
    for i, s in enumerate(samples, 1):
        blocks.append(f"--- 样本 {i} [{s.get('source')}] {s.get('title', '')} ---")
        blocks.append(f"来源: {s.get('sourceUrl', '无')}")
        blocks.append(s.get("rawText", "")[:4000])
        blocks.append("")
    return "\n".join(blocks)


def _to_dict(resp: Any) -> dict | None:
    if isinstance(resp, dict):
        return resp
    if hasattr(resp, "model_dump"):
        return resp.model_dump()
    return None


def normalize_base_url(url: str) -> str:
    """OpenAI 兼容网关常漏写 /v1，纯域名根路径时自动补上。"""
    url = url.strip().rstrip("/")
    if url.startswith("http") and url.count("/") <= 2:
        return f"{url}/v1"
    return url


def reject_html_response(text: str, base_url: str) -> None:
    lower = text.lstrip().lower()
    if lower.startswith("<!doctype") or lower.startswith("<html"):
        raise ValueError(
            "API 返回了 HTML 页面而非 JSON，通常是 AI_BASE_URL 配置错误。\n"
            f"当前地址: {base_url}\n"
            "请改为带 /v1 的 OpenAI 兼容 endpoint，例如: https://api.openai-next.com/v1"
        )


def extract_message_content(resp: Any, base_url: str = "") -> str:
    """兼容 OpenAI SDK 对象 / dict / 部分网关直接返回字符串。"""
    if resp is None:
        raise ValueError("模型返回为空")

    if isinstance(resp, str):
        text = resp.strip()
        if not text:
            raise ValueError("模型返回空字符串")
        reject_html_response(text, base_url)
        if text.startswith("{"):
            try:
                return extract_message_content(json.loads(text), base_url)
            except json.JSONDecodeError:
                return text
        return text

    data = _to_dict(resp)
    if data is not None:
        if "error" in data:
            err = data["error"]
            msg = err.get("message") if isinstance(err, dict) else str(err)
            raise ValueError(f"API 错误: {msg}")
        choices = data.get("choices") or []
        if choices:
            message = choices[0].get("message") or {}
            content = message.get("content")
            if content:
                return str(content).strip()
        if "content" in data:
            return str(data["content"]).strip()
        raise ValueError(f"无法解析响应 JSON: {json.dumps(data, ensure_ascii=False)[:800]}")

    try:
        content = resp.choices[0].message.content
        if content:
            return str(content).strip()
    except (AttributeError, IndexError, TypeError) as exc:
        raise ValueError(f"无法解析模型响应（类型 {type(resp).__name__}）") from exc

    raise ValueError("模型响应缺少 content 字段")


def call_llm(
    client: OpenAI, model: str, system_prompt: str, user_msg: str, base_url: str
) -> str:
    messages = [
        {"role": "system", "content": system_prompt},
        {"role": "user", "content": user_msg},
    ]
    kwargs_list = [
        {"response_format": {"type": "json_object"}},
        {},
    ]
    last_error: Exception | None = None
    for extra in kwargs_list:
        try:
            resp = client.chat.completions.create(
                model=model,
                messages=messages,
                temperature=0.3,
                **extra,
            )
            return extract_message_content(resp, base_url)
        except Exception as exc:
            last_error = exc
            print(f"调用失败，尝试下一种参数: {exc}", file=sys.stderr)
    raise last_error or RuntimeError("模型调用失败")


def parse_json_content(content: str, base_url: str = "") -> dict:
    text = content.strip()
    reject_html_response(text, base_url)
    if text.startswith("```"):
        lines = text.splitlines()
        lines = [ln for ln in lines if not ln.strip().startswith("```")]
        text = "\n".join(lines).strip()
    try:
        return json.loads(text)
    except json.JSONDecodeError as exc:
        raise ValueError(f"模型返回不是合法 JSON，前 500 字:\n{text[:500]}") from exc


def main() -> int:
    load_dotenv(ROOT.parent / ".env")
    load_dotenv()

    parser = argparse.ArgumentParser()
    parser.add_argument("--job-id", required=True)
    parser.add_argument("--dry-run", action="store_true")
    args = parser.parse_args()

    import yaml

    with (ROOT / "config" / "jobs.yaml").open(encoding="utf-8") as f:
        jobs = yaml.safe_load(f)["jobs"]
    if args.job_id not in jobs:
        print(f"无效 job-id: {args.job_id}", file=sys.stderr)
        return 1

    samples = load_samples(args.job_id)
    if len(samples) < 3:
        print(f"样本不足（当前 {len(samples)}），建议至少 5 条再归纳。", file=sys.stderr)
        return 1

    system_prompt = PROMPT_PATH.read_text(encoding="utf-8")
    user_msg = build_user_message(args.job_id, samples, jobs[args.job_id]["name"])

    if args.dry_run:
        print(user_msg[:2000])
        print(f"\n... 共 {len(user_msg)} 字符，将调用 LLM 归纳")
        return 0

    api_key = os.getenv("AI_API_KEY", "")
    if not api_key:
        print("请设置 AI_API_KEY（可与后端 application.yml 共用）", file=sys.stderr)
        return 1

    base_url = normalize_base_url(
        os.getenv("AI_BASE_URL", "https://api.openai.com/v1")
    )
    model = os.getenv("AI_MODEL_PRO", "gpt-4o")
    print(f"使用模型: {model}", file=sys.stderr)
    print(f"API 地址: {base_url}", file=sys.stderr)

    client = OpenAI(api_key=api_key, base_url=base_url)
    content = call_llm(client, model, system_prompt, user_msg, base_url)
    result = parse_json_content(content, base_url)

    AGG_DIR.mkdir(parents=True, exist_ok=True)
    out = AGG_DIR / f"{args.job_id}.summary.json"
    out.write_text(json.dumps(result, ensure_ascii=False, indent=2), encoding="utf-8")
    print(f"已保存归纳结果: {out}")
    print("下一步: 人工校对后，把字段同步到 src/main/resources/seed/jobs.json")
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
