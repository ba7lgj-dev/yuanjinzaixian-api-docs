#!/usr/bin/env python3
"""Build searchable documentation from the downloaded ZBIOT API text files."""

from __future__ import annotations

import hashlib
import json
import re
from datetime import datetime, timedelta, timezone
from pathlib import Path
from typing import Any


ROOT = Path(__file__).resolve().parents[1]
DOCS = ROOT / "docs" / "zbiot-api"
SOURCE = DOCS / "source"
MODULES_DIR = DOCS / "modules"
GUIDES_DIR = DOCS / "guides"
SOURCE_URL = "https://api.zbiot.cc/"

MODULES = [
    ("user", "用户管理 (User)", "账户(user)"),
    ("family", "设备车组 (Family)", "车组(family)"),
    ("device", "设备管理 (Device)", "设备(device)"),
    ("location", "设备轨迹 (Location)", "轨迹(location)"),
    ("alarm", "设备报警 (Alarm)", "报警(alarm)"),
    ("fence", "电子围栏 (Fence)", "围栏(fence)"),
    ("sim", "SIM卡 (SIM)", "sim卡(sim)"),
    ("loc", "设备模式 (Loc)", "模式(loc)"),
    ("timerswitch", "定时开关机（timerswitch）", "定时开关机(timerswitch)"),
    ("summary", "统计报表 (Summary)", "统计(summary)"),
    ("temphumi", "温湿度 (Temphumi)", "温度、湿度(temphumi)"),
    ("attendance", "打卡 (Attendance)", "打卡(attendance)"),
]

ENDPOINT_RE = re.compile(r"^\*{5,}(.*)\(([^()]+)\)\*{5,}\s*$")
MARKER_RE = re.compile(r"^(说明|get传入参数|post传入参数|传出结果|示例)\s*[:：]\s*(.*)$")

REQUIRED_ENDPOINT_COUNT = 135


def now_shanghai() -> datetime:
    try:
        from zoneinfo import ZoneInfo

        return datetime.now(ZoneInfo("Asia/Shanghai"))
    except Exception:
        return datetime.now(timezone(timedelta(hours=8)))


def read_text(path: Path) -> str:
    for encoding in ("utf-8-sig", "utf-8", "gb18030"):
        try:
            return path.read_text(encoding=encoding)
        except UnicodeDecodeError:
            continue
    raise UnicodeDecodeError("utf-8", b"", 0, 1, f"无法识别文件编码: {path}")


def json_dump(value: Any) -> str:
    return json.dumps(value, ensure_ascii=False, indent=2) + "\n"


def sha256(path: Path) -> str:
    digest = hashlib.sha256()
    with path.open("rb") as handle:
        for chunk in iter(lambda: handle.read(1024 * 1024), b""):
            digest.update(chunk)
    return digest.hexdigest()


def normalize_block(lines: list[str]) -> str:
    return "\n".join(line.rstrip() for line in lines).strip("\n")


def parse_fixed_table(lines: list[str]) -> tuple[list[dict[str, str]], int]:
    rows: list[dict[str, str]] = []
    column_count = 0
    for line in lines:
        stripped = line.strip()
        if not stripped.startswith("│"):
            continue
        cells = [cell.strip() for cell in stripped.split("│")[1:-1]]
        if not cells:
            continue
        column_count = max(column_count, len(cells))

        if len(cells) == 2:
            name, description = cells
            if name and description:
                rows.append({"name": name, "description": description})
            elif description and rows:
                rows[-1]["description"] += "\n" + description
            continue

        if len(cells) >= 3:
            name, field_type, description = cells[0], cells[1], "│".join(cells[2:])
            if name or field_type:
                rows.append(
                    {
                        "name": name,
                        "type": field_type,
                        "description": description,
                    }
                )
            elif description and rows:
                rows[-1]["description"] += "\n" + description

    return rows, column_count


def extract_json_example(text: str) -> tuple[Any | None, str | None]:
    start = text.find("{")
    end = text.rfind("}")
    if start == -1 or end == -1 or end < start:
        return None, "未找到 JSON 对象"
    raw = text[start : end + 1]
    try:
        return json.loads(raw), None
    except json.JSONDecodeError as exc:
        return None, f"{exc.msg} (第 {exc.lineno} 行，第 {exc.colno} 列)"


def get_marker_positions(lines: list[str]) -> list[tuple[int, str, str]]:
    positions: list[tuple[int, str, str]] = []
    for index, line in enumerate(lines):
        match = MARKER_RE.match(line.strip())
        if match:
            positions.append((index, match.group(1), match.group(2).strip()))
    return positions


def section_lines(
    lines: list[str],
    markers: list[tuple[int, str, str]],
    marker_index: int,
    line_offset: int,
) -> tuple[str, list[str]]:
    position, _, remainder = markers[marker_index]
    next_position = markers[marker_index + 1][0] if marker_index + 1 < len(markers) else len(lines)
    content: list[str] = []
    if remainder:
        content.append(remainder)
    content.extend(lines[position + 1 : next_position])
    return normalize_block(content), lines[position + 1 : next_position]


def parse_endpoint(
    module_key: str,
    module_title: str,
    source_name: str,
    chunk: list[str],
    start_line: int,
    index_description: str,
) -> dict[str, Any]:
    match = ENDPOINT_RE.match(chunk[0].strip())
    if not match:
        raise ValueError(f"{source_name}:{start_line} 无法解析接口标题: {chunk[0]}")

    name = match.group(1).strip()
    func = match.group(2).strip()
    markers = get_marker_positions(chunk)
    marker_names = [marker[1] for marker in markers]

    if marker_names.count("传出结果") != 1 or marker_names.count("示例") != 2:
        raise ValueError(
            f"{module_key}.{func} 的章节结构异常: 传出结果={marker_names.count('传出结果')}, "
            f"示例={marker_names.count('示例')}"
        )

    note_text = ""
    get_params_text = ""
    request_text = ""
    request_example_text = ""
    response_text = ""
    response_example_text = ""

    if "说明" in marker_names:
        note_text, _ = section_lines(chunk, markers, marker_names.index("说明"), start_line)
    if "get传入参数" in marker_names:
        get_params_text, _ = section_lines(
            chunk, markers, marker_names.index("get传入参数"), start_line
        )

    request_text, _ = section_lines(
        chunk, markers, marker_names.index("post传入参数"), start_line
    )
    first_example = marker_names.index("示例")
    request_example_text, _ = section_lines(chunk, markers, first_example, start_line)
    response_text, _ = section_lines(
        chunk, markers, marker_names.index("传出结果"), start_line
    )
    second_example = marker_names.index("示例", first_example + 1)
    response_example_text, _ = section_lines(chunk, markers, second_example, start_line)

    request_fields, _ = parse_fixed_table(request_text.splitlines())
    response_fields, _ = parse_fixed_table(response_text.splitlines())
    request_example, request_error = extract_json_example(request_example_text)
    response_example, response_error = extract_json_example(response_example_text)

    return {
        "id": f"{module_key}.{func}",
        "module": module_key,
        "module_title": module_title,
        "func": func,
        "name": name,
        "summary": index_description or name,
        "anchor": f"{module_key}-{func}",
        "notes": note_text,
        "get_params": get_params_text,
        "request_fields": request_fields,
        "request_example": request_example,
        "request_example_raw": request_example_text if request_error else None,
        "request_example_error": request_error,
        "response_fields": response_fields,
        "response_example": response_example,
        "response_example_raw": response_example_text if response_error else None,
        "response_example_error": response_error,
        "source_file": f"source/{source_name}.txt",
        "source_line": start_line,
    }


def parse_environment(lines: list[str]) -> dict[str, str]:
    environment: dict[str, str] = {}
    patterns = [
        ("dev", re.compile(r"^dev\(开发\)环境URL\s*[:：]\s*(.+)$")),
        ("beta", re.compile(r"^beta\(体验测试\)环境URL\s*[:：]\s*(.+)$")),
        ("production", re.compile(r"^IDC（正式）环境URL\s*[:：]\s*(.+)$")),
        ("owner", re.compile(r"^模块负责人\s*[:：]\s*(.+)$")),
    ]
    for line in lines:
        stripped = line.strip()
        for key, pattern in patterns:
            match = pattern.match(stripped)
            if match:
                environment[key] = match.group(1).strip()
                break
    return environment


def parse_module(module_key: str, module_title: str, source_name: str) -> dict[str, Any]:
    source_path = SOURCE / f"{source_name}.txt"
    text = read_text(source_path)
    lines = text.splitlines()

    index_header = None
    for line_number, line in enumerate(lines, start=1):
        if re.match(rf"^{re.escape(module_key)}模块索引\s*[:：]?\s*$", line.strip()):
            index_header = line_number
            break
    if index_header is None:
        raise ValueError(f"{source_name}: 找不到 {module_key} 模块索引")

    table_start = None
    table_end = None
    for index in range(index_header, len(lines)):
        if lines[index].lstrip().startswith("┌"):
            table_start = index
            break
    if table_start is None:
        raise ValueError(f"{source_name}: 找不到模块索引表格")
    for index in range(table_start, len(lines)):
        if lines[index].lstrip().startswith("└"):
            table_end = index
            break
    if table_end is None:
        raise ValueError(f"{source_name}: 模块索引表格未闭合")

    index_rows, _ = parse_fixed_table(lines[table_start : table_end + 1])
    index_map = {row["name"]: row["description"] for row in index_rows}

    endpoint_starts: list[int] = []
    for index, line in enumerate(lines):
        if ENDPOINT_RE.match(line.strip()):
            endpoint_starts.append(index)
    if not endpoint_starts:
        raise ValueError(f"{source_name}: 找不到接口详情")

    endpoints: list[dict[str, Any]] = []
    for position, start in enumerate(endpoint_starts):
        end = endpoint_starts[position + 1] if position + 1 < len(endpoint_starts) else len(lines)
        chunk = lines[start:end]
        func_match = ENDPOINT_RE.match(chunk[0].strip())
        if not func_match:
            continue
        func = func_match.group(2).strip()
        endpoints.append(
            parse_endpoint(
                module_key=module_key,
                module_title=module_title,
                source_name=source_name,
                chunk=chunk,
                start_line=start + 1,
                index_description=index_map.get(func, ""),
            )
        )

    endpoint_keys = [endpoint["func"] for endpoint in endpoints]
    missing_from_details = [key for key in index_map if key not in endpoint_keys]
    missing_from_index = [key for key in endpoint_keys if key not in index_map]
    duplicates = sorted({key for key in endpoint_keys if endpoint_keys.count(key) > 1})
    if missing_from_details or missing_from_index or duplicates:
        raise ValueError(
            f"{source_name}: 索引与详情不一致; "
            f"缺少详情={missing_from_details}, 缺少索引={missing_from_index}, 重复={duplicates}"
        )

    return {
        "module": module_key,
        "title": module_title,
        "source_file": f"source/{source_name}.txt",
        "environment": parse_environment(lines),
        "endpoint_count": len(endpoints),
        "endpoints": endpoints,
    }


def markdown_cell(value: str) -> str:
    return (
        value.strip()
        .replace("\\", "\\\\")
        .replace("|", "\\|")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\n", "<br>")
    )


def render_fields(fields: list[dict[str, str]]) -> str:
    if not fields:
        return "_未解析到结构化字段，请以原始文档或示例为准。_"
    rows = ["| 字段 | 类型 | 说明 |", "| --- | --- | --- |"]
    for field in fields:
        name = markdown_cell(field.get("name", ""))
        field_type = markdown_cell(field.get("type", ""))
        description = markdown_cell(field.get("description", ""))
        rows.append(f"| `{name}` | `{field_type}` | {description} |")
    return "\n".join(rows)


def render_json(value: Any, raw: str | None, error: str | None) -> str:
    if value is not None:
        return "```json\n" + json.dumps(value, ensure_ascii=False, indent=2) + "\n```"
    return "```text\n" + (raw or "") + "\n```\n\n> JSON 解析失败：" + (error or "未知错误")


def render_endpoint(endpoint: dict[str, Any]) -> str:
    lines = [
        f'<a id="{endpoint["anchor"]}"></a>',
        f'## {endpoint["name"]} (`{endpoint["func"]}`)',
        "",
        f'- 接口 ID：`{endpoint["id"]}`',
        f'- 模块：`{endpoint["module"]}`',
        f'- 用途：{endpoint["summary"]}',
        f'- 原始位置：`{endpoint["source_file"]}` 第 {endpoint["source_line"]} 行',
    ]
    if endpoint["get_params"]:
        lines.append(f'- URL 参数：{endpoint["get_params"]}')
    else:
        lines.append("- URL 参数：源文档未列出")

    if endpoint["notes"]:
        lines.extend(["", "### 说明", "", endpoint["notes"]])

    lines.extend(
        [
            "",
            "### 请求字段",
            "",
            render_fields(endpoint["request_fields"]),
            "",
            "### 请求示例",
            "",
            render_json(
                endpoint["request_example"],
                endpoint["request_example_raw"],
                endpoint["request_example_error"],
            ),
            "",
            "### 返回字段",
            "",
            render_fields(endpoint["response_fields"]),
            "",
            "### 返回示例",
            "",
            render_json(
                endpoint["response_example"],
                endpoint["response_example_raw"],
                endpoint["response_example_error"],
            ),
            "",
        ]
    )
    return "\n".join(lines)


def render_module(module: dict[str, Any]) -> str:
    environment = module["environment"]
    lines = [
        f'# {module["title"]}',
        "",
        f'- 模块标识：`{module["module"]}`',
        f'- 接口数量：{module["endpoint_count"]}',
        f'- 原始文档：`{module["source_file"]}`',
    ]
    if environment.get("production"):
        lines.append(f'- 正式环境：`{environment["production"]}`')
    if environment.get("beta"):
        lines.append(f'- 测试环境：`{environment["beta"]}`')
    if environment.get("dev"):
        lines.append(f'- 开发环境：`{environment["dev"]}`')
    if environment.get("owner"):
        lines.append(f'- 模块负责人：{environment["owner"]}')

    lines.extend(
        [
            "",
            "## 接口索引",
            "",
            "| 接口 | 名称 | 请求字段数 | 返回字段数 |",
            "| --- | --- | ---: | ---: |",
        ]
    )
    for endpoint in module["endpoints"]:
        lines.append(
            f'| [`{endpoint["func"]}`](#{endpoint["anchor"]}) | {markdown_cell(endpoint["name"])} | '
            f'{len(endpoint["request_fields"])} | {len(endpoint["response_fields"])} |'
        )

    lines.extend(["", "## 接口详情", ""])
    for endpoint in module["endpoints"]:
        lines.extend([render_endpoint(endpoint).rstrip(), ""])
    return "\n".join(lines).rstrip() + "\n"


def render_combined(modules: list[dict[str, Any]], generated_at: str) -> str:
    lines = [
        "# 远近在线开放云服务 API 完整接口文档",
        "",
        f"> 来源：{SOURCE_URL}  ",
        f"> 抓取时间：{generated_at}  ",
        f"> 覆盖：12 个模块，{sum(item['endpoint_count'] for item in modules)} 个接口",
        "",
        "## 模块目录",
        "",
        "| 模块 | 接口数 | 文档 |",
        "| --- | ---: | --- |",
    ]
    for module in modules:
        lines.append(
            f'| `{module["module"]}` | {module["endpoint_count"]} | '
            f'[modules/{module["module"]}.md](modules/{module["module"]}.md) |'
        )
    lines.append("")
    for module in modules:
        lines.append(render_module(module))
        lines.append("")
    return "\n".join(lines).rstrip() + "\n"


def render_preface_guide(source_text: str) -> str:
    return f"""# 前言与接入规则

> 原始文档：[`source/前言.txt`](../source/前言.txt)

## 快速结论

- 所有业务接口共用同一入口，通过 POST JSON 中的 `module`、`func`、`params` 选择具体接口。
- 登录成功后，将 `user.Login` 返回的 `sid` 作为 URL 查询参数传给后续请求。
- `sid` 长时间不用会在 2 天后失效；同一账号最多保留 100 个有效 `sid`。
- 单模块单接口限频为 100ms 一次，1 分钟 500 次；超限错误码为 `268697629`。
- 列表采用瀑布流分页，优先使用服务端返回的最后一条记录参数请求下一页。
- 文档中的示例值由系统随机生成，仅用于展示 JSON 结构，不代表真实业务数据。
- 站点前言写“11 个模块”，但当前站点索引实际包含 12 个业务模块；本快照以实际索引和接口详情为准。

## 源文档原文

以下内容按源文件原样保存，便于核对所有细节。

````text
{source_text.rstrip()}
````
"""


def render_coordinate_guide(source_text: str) -> str:
    marker = "JS代码："
    if marker not in source_text:
        raise ValueError("坐标系转换代码.txt 缺少 JS代码 分段")
    cpp_part, js_part = source_text.split(marker, 1)
    cpp_code = cpp_part.split("C++代码：", 1)[1].strip("\n")
    js_code = js_part.strip("\n")
    return f"""# 坐标系转换代码

> 原始文档：[`source/坐标系转换代码.txt`](../source/坐标系转换代码.txt)

用于 WGS-84 与 GCJ-02（火星坐标系）之间的转换。以下是源文档代码快照。

## C++ 代码

```cpp
{cpp_code}
```

## JavaScript 代码

```javascript
{js_code}
```
"""


def compact_endpoint(endpoint: dict[str, Any]) -> dict[str, Any]:
    return {
        "id": endpoint["id"],
        "module": endpoint["module"],
        "func": endpoint["func"],
        "name": endpoint["name"],
        "summary": endpoint["summary"],
        "anchor": endpoint["anchor"],
        "document": f"modules/{endpoint['module']}.md#{endpoint['anchor']}",
        "request_field_names": [field.get("name", "") for field in endpoint["request_fields"]],
        "response_field_names": [field.get("name", "") for field in endpoint["response_fields"]],
    }


def build_readme(modules: list[dict[str, Any]], generated_at: str) -> str:
    total = sum(module["endpoint_count"] for module in modules)
    module_rows = "\n".join(
        f'| `{module["module"]}` | {module["title"]} | {module["endpoint_count"]} | '
        f'[打开](modules/{module["module"]}.md) |'
        for module in modules
    )
    return f"""# 远近在线开放云服务 API 文档快照

本目录完整保存了 [https://api.zbiot.cc/]({SOURCE_URL}) 当前公开的接口文档，并转换为适合 AI 检索的结构化资料。

- 抓取时间：{generated_at}
- 覆盖范围：12 个业务模块，{total} 个接口
- 原始来源：站点 `public/txt/*.txt` 文档数据
- 原始快照：[`source/`](source/)

## AI 读取顺序

1. 先读 [`llms.txt`](llms.txt) 了解文档入口。
2. 查找接口时优先读 [`endpoint-index.json`](endpoint-index.json)，它包含全部接口的 ID、名称、用途和字段名。
3. 需要完整字段说明与示例时读 [`api-index.json`](api-index.json) 或对应模块 Markdown。
4. 需要一次性全文检索时读 [`llms-full.txt`](llms-full.txt) 或 [`all-api.md`](all-api.md)。

## 调用模型

```http
POST http://cciot.cc/mapi?sid=<登录返回的sid>
Content-Type: application/json

{{
  "module": "user",
  "func": "Login",
  "params": {{}}
}}
```

- 接口入口固定，`module` 选择模块，`func` 选择函数，`params` 承载业务参数。
- `sid` 通过 URL 查询参数传递。每个模块文档开头也保留了该模块列出的开发、测试和正式地址。
- 返回值均为 JSON，成功时通常包含 `errcode: 0`。
- 本项目只保存文档快照，不包含可用账号、密码、`sid` 或真实设备数据。

## 关键接入规则

| 主题 | 规则 |
| --- | --- |
| 登录凭证 | `user.Login` 返回 `sid`；连续 2 天未使用会过期 |
| sid 上限 | 同一账号最多 100 个有效 `sid` |
| 限频 | 单模块单接口 100ms 一次，1 分钟最多 500 次 |
| 超限错误 | `268697629`，操作频率过快 |
| 分页 | 瀑布流分页，下一页使用响应给出的最后一条记录参数 |
| 示例数据 | 示例值随机生成，只表示结构和类型 |
| 设备标识 | 多数接口使用加密设备号 `simei`，不是展示用 IMEI |
| 列表建议 | App 约 50 条/页，Web 约 100 条/页，或使用接口默认值 |

## 模块目录

| 模块标识 | 模块名称 | 接口数 | 文档 |
| --- | --- | ---: | --- |
{module_rows}

## 指南与索引

- [前言与接入规则](guides/overview.md)：认证、分页、限频、组织结构和常见错误。
- [坐标系转换代码](guides/coordinate-conversion.md)：WGS-84 / GCJ-02 的 C++ 与 JavaScript 实现。
- [完整接口 JSON](api-index.json)：包含每个接口的参数、返回字段和请求/返回示例。
- [接口速查 JSON](endpoint-index.json)：轻量索引，适合搜索后定位模块。
- [LLM 索引](llms.txt) / [LLM 全文](llms-full.txt)。
- [采集清单](manifest.json)：源文件、生成文件、哈希与覆盖统计。

## 已知文档特点

- 部分字段表把枚举值写在说明列，已按多行合并保留。
- 文档示例中的字符串和数值是随机占位值，不能直接作为业务参数使用。
- 源站点所有未知路径都会回退到同一 HTML 页面，因此本快照直接采集该页面实际加载的 `public/txt/*.txt` 文件。
- 前言中的模块数量与当前索引存在差异，正文和 JSON 均以实际接口详情为准。
"""


def build_llms_txt(modules: list[dict[str, Any]], generated_at: str) -> str:
    total = sum(module["endpoint_count"] for module in modules)
    module_lines = "\n".join(
        f'- [modules/{module["module"]}.md](modules/{module["module"]}.md): '
        f'{module["title"]}，{module["endpoint_count"]} 个接口'
        for module in modules
    )
    return f"""# 远近在线开放云服务 API

> {total} 个接口、12 个业务模块的完整文档快照，采集自 {SOURCE_URL}，抓取时间 {generated_at}。

调用统一使用 `POST /mapi?sid=...`，请求体为 `{{"module","func","params"}}`。示例值为随机占位数据。

## 首选入口

- [README.md](README.md): 调用方式、认证、限频、分页和模块目录
- [endpoint-index.json](endpoint-index.json): 全部接口的轻量检索索引
- [api-index.json](api-index.json): 全部接口的完整结构化字段与示例
- [all-api.md](all-api.md): 全部接口合并文档
- [llms-full.txt](llms-full.txt): 可直接吞入的全文

## Guides

- [guides/overview.md](guides/overview.md): 前言、接入规则与常见错误
- [guides/coordinate-conversion.md](guides/coordinate-conversion.md): 坐标系转换代码

## Modules

{module_lines}

## Source

- [source/](source/): 站点原始 TXT 文档与页面抓取快照
- [manifest.json](manifest.json): 文件哈希、覆盖统计和采集清单
"""


def write_file(path: Path, content: str) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(content, encoding="utf-8", newline="\n")


def validate_catalog(modules: list[dict[str, Any]]) -> dict[str, Any]:
    endpoints = [
        endpoint for module in modules for endpoint in module["endpoints"]
    ]
    endpoint_ids = [endpoint["id"] for endpoint in endpoints]
    duplicate_ids = sorted(
        {endpoint_id for endpoint_id in endpoint_ids if endpoint_ids.count(endpoint_id) > 1}
    )
    missing_envelope = [
        endpoint["id"]
        for endpoint in endpoints
        if not {"module", "func", "params"}.issubset(
            {field.get("name") for field in endpoint["request_fields"]}
        )
    ]
    missing_examples = [
        endpoint["id"]
        for endpoint in endpoints
        if endpoint["request_example"] is None or endpoint["response_example"] is None
    ]
    empty_fields = [
        endpoint["id"]
        for endpoint in endpoints
        if any(
            not field.get("name") or not field.get("type")
            for field in endpoint["request_fields"] + endpoint["response_fields"]
        )
    ]
    if duplicate_ids or missing_envelope or missing_examples or empty_fields:
        raise ValueError(
            "生成结果校验失败: "
            f"重复 ID={duplicate_ids}, 缺少请求信封字段={missing_envelope}, "
            f"缺少示例={missing_examples}, 空字段={empty_fields}"
        )
    return {
        "index_and_detail_match": True,
        "duplicate_endpoints": 0,
        "expected_endpoint_count": REQUIRED_ENDPOINT_COUNT,
        "actual_endpoint_count": len(endpoints),
        "request_fields_total": sum(
            len(endpoint["request_fields"]) for endpoint in endpoints
        ),
        "response_fields_total": sum(
            len(endpoint["response_fields"]) for endpoint in endpoints
        ),
        "json_examples_parsed": len(endpoints) * 2,
        "missing_envelope_fields": 0,
        "empty_field_definitions": 0,
    }


def main() -> None:
    generated = now_shanghai()
    generated_at = generated.isoformat(timespec="seconds")
    MODULES_DIR.mkdir(parents=True, exist_ok=True)
    GUIDES_DIR.mkdir(parents=True, exist_ok=True)

    modules = [
        parse_module(module_key, module_title, source_name)
        for module_key, module_title, source_name in MODULES
    ]
    total = sum(module["endpoint_count"] for module in modules)
    if total != REQUIRED_ENDPOINT_COUNT:
        raise ValueError(f"接口数量校验失败: 期望 {REQUIRED_ENDPOINT_COUNT}, 实际 {total}")
    validation = validate_catalog(modules)

    preface_text = read_text(SOURCE / "前言.txt")
    coordinate_text = read_text(SOURCE / "坐标系转换代码.txt")

    module_documents = {}
    for module in modules:
        path = MODULES_DIR / f"{module['module']}.md"
        content = render_module(module)
        write_file(path, content)
        module_documents[module["module"]] = content

    write_file(GUIDES_DIR / "overview.md", render_preface_guide(preface_text))
    write_file(
        GUIDES_DIR / "coordinate-conversion.md",
        render_coordinate_guide(coordinate_text),
    )

    all_api = render_combined(modules, generated_at)
    write_file(DOCS / "all-api.md", all_api)

    api_index = {
        "name": "远近在线开放云服务 API",
        "source_url": SOURCE_URL,
        "generated_at": generated_at,
        "coverage": {
            "modules": len(modules),
            "endpoints": total,
        },
        "transport": {
            "method": "POST",
            "path": "/mapi",
            "sid_query_parameter": "sid",
            "request_envelope": ["module", "func", "params"],
        },
        "modules": modules,
    }
    write_file(DOCS / "api-index.json", json_dump(api_index))

    endpoint_index = {
        "name": api_index["name"],
        "source_url": SOURCE_URL,
        "generated_at": generated_at,
        "coverage": api_index["coverage"],
        "endpoints": [
            compact_endpoint(endpoint)
            for module in modules
            for endpoint in module["endpoints"]
        ],
    }
    write_file(DOCS / "endpoint-index.json", json_dump(endpoint_index))

    readme = build_readme(modules, generated_at)
    write_file(DOCS / "README.md", readme)
    write_file(DOCS / "llms.txt", build_llms_txt(modules, generated_at))

    llms_full_parts = [
        readme.rstrip(),
        "",
        "# 前言与接入规则",
        "",
        render_preface_guide(preface_text).split("## 源文档原文", 1)[1].strip(),
        "",
        "# 坐标系转换代码",
        "",
        render_coordinate_guide(coordinate_text),
        "",
        all_api,
    ]
    write_file(DOCS / "llms-full.txt", "\n\n".join(llms_full_parts).rstrip() + "\n")

    source_files = sorted(
        path for path in SOURCE.iterdir() if path.is_file()
    )
    generated_files = sorted(
        path
        for path in DOCS.rglob("*")
        if path.is_file()
        and path.name != "manifest.json"
        and SOURCE not in path.parents
        and path.parent != SOURCE
    )
    manifest = {
        "source_url": SOURCE_URL,
        "fetched_at": generated_at,
        "coverage": api_index["coverage"],
        "source_files": [
            {
                "path": path.relative_to(DOCS).as_posix(),
                "bytes": path.stat().st_size,
                "sha256": sha256(path),
            }
            for path in source_files
        ],
        "generated_files": [
            {
                "path": path.relative_to(DOCS).as_posix(),
                "bytes": path.stat().st_size,
                "sha256": sha256(path),
            }
            for path in generated_files
        ],
        "module_endpoint_counts": {
            module["module"]: module["endpoint_count"] for module in modules
        },
        "validation": validation,
    }
    write_file(DOCS / "manifest.json", json_dump(manifest))

    print(f"Generated {len(modules)} module documents and {total} endpoints.")
    print(f"API index: {DOCS / 'api-index.json'}")
    print(f"Combined doc: {DOCS / 'all-api.md'}")
    print(f"Manifest: {DOCS / 'manifest.json'}")
    print(
        "Validation: "
        f"{validation['actual_endpoint_count']} endpoints, "
        f"{validation['request_fields_total']} request fields, "
        f"{validation['response_fields_total']} response fields, "
        f"{validation['json_examples_parsed']} examples parsed."
    )


if __name__ == "__main__":
    main()
