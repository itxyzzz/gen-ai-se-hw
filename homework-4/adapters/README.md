# Pipeline Adapters

## Mock Adapter

`mock` is a deterministic local adapter used when live model credentials are unavailable. It applies the same artifact contract as a model-backed run and is the default for reproducible homework verification.

## OpenAI SDK Adapter

`openai-sdk` is the intended primary live single-command path. In this dependency-free homework folder it records a blocked run when `OPENAI_API_KEY` or the SDK package is unavailable, rather than pretending a model executed.

## Codex Chat Adapter

`codex-chat` prepares prompt packets for this Codex Desktop workflow and validates externally produced artifacts against the same run contract.

## Future Claude Code Adapter

A future Claude Code adapter can map each universal `agents/*.agent.md` file to `.claude/agents` or headless command prompts, while preserving the same `runs/<scenario>/<run-id>/` artifact contract.
