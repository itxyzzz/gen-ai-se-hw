import assert from "node:assert/strict";
import { readFile } from "node:fs/promises";
import path from "node:path";
import test, { before } from "node:test";
import { runMockPipeline } from "../../adapters/mock.js";
import { loadConfig, resolveHomeworkPath } from "../lib/config.js";
import { copyRunToCurrent, markPromoted } from "../lib/workspace.js";

before(() => {
  process.env.HW4_RUNS_ROOT = resolveHomeworkPath(".test-runs");
  process.env.HW4_CURRENT_APP_ROOT = resolveHomeworkPath(".test-current");
  process.env.HW4_BENCHMARK_ROOT = resolveHomeworkPath(".test-benchmark");
});

test("promotes a verified run into app/current and marks metadata", async () => {
  const config = await loadConfig();
  const runId = `test-promotion-${Date.now()}`;
  await runMockPipeline(config, { scenarioId: "bug-001", runId });

  const currentApp = await copyRunToCurrent(config, "bug-001", runId);
  const metadata = await markPromoted("bug-001", runId);
  const currentCalculator = await readFile(path.join(currentApp, "src", "quoteCalculator.js"), "utf8");

  assert.equal(metadata.promoted, true);
  assert.equal(currentApp, resolveHomeworkPath(".test-current"));
  assert.match(currentCalculator, /item\.quantity \* item\.unitPrice/);
});
