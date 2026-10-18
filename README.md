# 远近在线 API 文档项目

本项目的核心资料位于 [docs/zbiot-api/README.md](docs/zbiot-api/README.md)。

这份文档快照覆盖 `https://api.zbiot.cc/` 当前公开的 12 个业务模块、135 个接口，既保留了站点原始 TXT 文档，也生成了适合 AI 检索的结构化 JSON 与 Markdown。

常用入口：

- [AI 索引](docs/zbiot-api/llms.txt)
- [接口速查](docs/zbiot-api/endpoint-index.json)
- [完整接口数据](docs/zbiot-api/api-index.json)
- [全部接口 Markdown](docs/zbiot-api/all-api.md)
- [原始文档快照](docs/zbiot-api/source/)

重新生成 Markdown、JSON 与清单：

```powershell
python scripts\build_zbiot_docs.py
```

生成脚本只读取 `docs/zbiot-api/source/` 下的原始文档，不会修改原始快照。
