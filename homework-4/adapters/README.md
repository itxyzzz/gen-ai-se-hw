# Text Pipeline Adapters

Adapters are Markdown mappings from the universal Homework 4 harness to a
specific coding assistant. They are not executable scripts.

## Primary Adapter

- `codex-chat.md`: the canonical submission path. One Codex chat phrase loads
  the harness, agents, skills, scenario, and artifact contract.

## Portable Adapter Mappings

- `claude-code.md`: maps the same hierarchy to Claude Code.
- `open-code.md`: maps the same hierarchy to Open Code.
- `google-antigravity.md`: maps the same hierarchy to Google Antigravity.
- `generic-agent.md`: fallback mapping for a capable agentic tool without a
  dedicated adapter.

Each adapter must preserve the same run workspace rules and artifact names so
results can be reviewed with the same benchmark rubric.
