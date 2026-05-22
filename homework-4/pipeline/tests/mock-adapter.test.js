import assert from "node:assert/strict";
import { readFile } from "node:fs/promises";
import path from "node:path";
import test, { before } from "node:test";
import { runMockPipeline } from "../../adapters/mock.js";
import { loadConfig, resolveHomeworkPath } from "../lib/config.js";

before(() => {
  process.env.HW4_RUNS_ROOT = resolveHomeworkPath(".test-runs");
  process.env.HW4_CURRENT_APP_ROOT = resolveHomeworkPath(".test-current");
  process.env.HW4_BENCHMARK_ROOT = resolveHomeworkPath(".test-benchmark");
});

test("mock adapter creates a complete fixed run", async () => {
  const config = await loadConfig();
  const runId = `test-mock-adapter-${Date.now()}`;
  const { workspace, metadata } = await runMockPipeline(config, {
    scenarioId: "bug-001",
    runId
  });

  assert.equal(metadata.status, "completed");
  assert.equal(metadata.metrics.testsPassing, true);
  assert.equal(metadata.metrics.testsAdded, 2);
  assert.deepEqual(metadata.stages.map((stage) => stage.stageId), config.stageOrder);

  const fixedCalculator = await readFile(path.join(workspace.appRoot, "src", "quoteCalculator.js"), "utf8");
  const securityReport = await readFile(path.join(workspace.runRoot, "security-report.md"), "utf8");
  const testReport = await readFile(path.join(workspace.runRoot, "test-report.md"), "utf8");

  assert.match(fixedCalculator, /item\.quantity \* item\.unitPrice/);
  assert.match(securityReport, /No CRITICAL, HIGH, MEDIUM, or LOW/);
  assert.match(testReport, /FIRST Assessment/);
});
