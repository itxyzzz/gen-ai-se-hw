import { mkdir, readFile, writeFile } from "node:fs/promises";
import path from "node:path";
import { loadAgentSpecs } from "../pipeline/lib/agentSpec.js";
import { validateRequiredArtifacts, writeJson } from "../pipeline/lib/artifacts.js";
import { resolveHomeworkPath } from "../pipeline/lib/config.js";
import { createRunWorkspace, getRunRoot, writeRunFile } from "../pipeline/lib/workspace.js";

export async function prepareCodexChat(config, options = {}) {
  const workspace = await createRunWorkspace(config, options);
  const specs = await loadAgentSpecs();
  const promptRoot = path.join(workspace.runRoot, "codex-chat-prompts");
  await mkdir(promptRoot, { recursive: true });

  for (const stageId of config.stageOrder) {
    const spec = specs.get(stageId);
    const prompt = renderPromptPacket(stageId, spec, workspace);
    await writeFile(path.join(promptRoot, `${stageId}.md`), prompt, "utf8");
  }

  await writeRunFile(workspace.runRoot, "command-log.md", "# Command Log\n\nPrepared Codex chat prompt packets.\n");
  await writeJson(path.join(workspace.runRoot, "run-metadata.json"), {
    runId: workspace.runId,
    scenarioId: workspace.scenarioId,
    adapter: "codex-chat",
    provider: "codex-desktop",
    model: options.model ?? "chat-selected",
    reasoningEffort: options.reasoning ?? "operator-selected",
    pipelineVersion: config.pipelineVersion,
    status: "prepared",
    startedAt: new Date().toISOString(),
    endedAt: new Date().toISOString(),
    manualInterventions: 1,
    promoted: false
  });

  return { workspace, promptRoot };
}

export async function validateCodexChat(config, options = {}) {
  const scenarioId = options.scenarioId ?? config.defaultScenario;
  const runId = options.runId ?? config.defaultRunId;
  const runRoot = getRunRoot(scenarioId, runId);
  await validateRequiredArtifacts(runRoot, config.requiredArtifacts);
  const metadataPath = path.join(runRoot, "run-metadata.json");
  const metadata = JSON.parse(await readFile(metadataPath, "utf8"));
  metadata.status = metadata.status === "prepared" ? "validated-prompt-prep" : metadata.status;
  await writeJson(metadataPath, metadata);
  return { runRoot, metadata };
}

function renderPromptPacket(stageId, spec, workspace) {
  return [
    `# Codex Chat Prompt Packet: ${stageId}`,
    "",
    `Run workspace: \`${workspace.runRoot}\``,
    "",
    "## Agent Frontmatter",
    "",
    "```json",
    JSON.stringify(spec.frontmatter, null, 2),
    "```",
    "",
    "## Agent Instructions",
    "",
    spec.body,
    "",
    "## Skill Loading",
    "",
    "Load every skill listed in frontmatter before acting.",
    "",
    "## Write Safety",
    "",
    "Write only inside the run workspace. Do not edit `app/baseline` or earlier homework folders.",
    ""
  ].join("\n");
}
