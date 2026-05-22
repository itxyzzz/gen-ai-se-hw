import { mkdir, writeFile } from "node:fs/promises";
import path from "node:path";
import { writeJson } from "../pipeline/lib/artifacts.js";
import { createRunWorkspace, writeRunFile } from "../pipeline/lib/workspace.js";

export async function runOpenAiSdkPipeline(config, options = {}) {
  const workspace = await createRunWorkspace(config, options);
  const metadata = {
    runId: workspace.runId,
    scenarioId: workspace.scenarioId,
    adapter: "openai-sdk",
    provider: "openai",
    model: options.model ?? "gpt-5.3-codex",
    reasoningEffort: options.reasoning ?? "high",
    pipelineVersion: config.pipelineVersion,
    baselineCommit: "local-working-tree",
    startedAt: new Date().toISOString(),
    endedAt: new Date().toISOString(),
    status: "blocked",
    pipelineCommand: "npm run pipeline:openai",
    testCommand: workspace.scenario.testCommand,
    metrics: {
      bugsFixed: 0,
      securityIssuesFixed: 0,
      testsAdded: 0,
      testsPassing: false,
      securityFindingsHighOrWorse: null,
      manualInterventionCount: 0
    },
    manualInterventions: 0,
    promoted: false,
    blocker: process.env.OPENAI_API_KEY ? "OpenAI SDK package is not installed in this dependency-free homework folder." : "OPENAI_API_KEY is not set."
  };

  await mkdir(path.join(workspace.runRoot, "raw-responses"), { recursive: true });
  await writeRunFile(workspace.runRoot, "command-log.md", "# Command Log\n\nOpenAI SDK live execution was blocked before model invocation.\n");
  await writeRunFile(workspace.runRoot, "research/verified-research.md", "# Verified Research\n\nLive OpenAI SDK execution blocked; no verified research generated.\n");
  await writeRunFile(workspace.runRoot, "fix-summary.md", "# Fix Summary\n\nLive OpenAI SDK execution blocked; no fixes applied.\n");
  await writeRunFile(workspace.runRoot, "security-report.md", "# Security Report\n\nLive OpenAI SDK execution blocked; no security review generated.\n");
  await writeRunFile(workspace.runRoot, "test-report.md", "# Test Report\n\nLive OpenAI SDK execution blocked; no generated tests run.\n");
  await writeRunFile(workspace.runRoot, "patch.diff", "# Patch Summary\n\nLive OpenAI SDK execution blocked; no patch generated.\n");
  await writeFile(path.join(workspace.runRoot, "raw-responses", "openai-sdk-blocked.json"), `${JSON.stringify(metadata, null, 2)}\n`, "utf8");
  await writeJson(path.join(workspace.runRoot, "run-metadata.json"), metadata);

  return { workspace, metadata };
}
