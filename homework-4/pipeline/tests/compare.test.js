import assert from "node:assert/strict";
import { readFile } from "node:fs/promises";
import path from "node:path";
import test, { before } from "node:test";
import { runMockPipeline } from "../../adapters/mock.js";
import { loadConfig, homeworkRoot, resolveHomeworkPath } from "../lib/config.js";
import { generateComparison } from "../compare.js";

before(() => {
  process.env.HW4_RUNS_ROOT = resolveHomeworkPath(".test-runs");
  process.env.HW4_CURRENT_APP_ROOT = resolveHomeworkPath(".test-current");
  process.env.HW4_BENCHMARK_ROOT = resolveHomeworkPath(".test-benchmark");
});

test("comparison command writes benchmark outputs from run metadata", async () => {
  const config = await loadConfig();
  const runId = `test-compare-${Date.now()}`;
  await runMockPipeline(config, { scenarioId: "bug-001", runId });

  const runs = await generateComparison(config, "bug-001");
  const comparison = await readFile(path.join(resolveHomeworkPath(".test-benchmark"), "bug-001-comparison.md"), "utf8");

  assert.ok(runs.length > 0);
  assert.match(comparison, new RegExp(runId));
});
