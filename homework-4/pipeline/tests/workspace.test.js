import assert from "node:assert/strict";
import { readFile } from "node:fs/promises";
import path from "node:path";
import test, { before } from "node:test";
import { loadConfig, resolveHomeworkPath } from "../lib/config.js";
import { assertInsideRun, createRunWorkspace, getRunRoot, writeRunFile } from "../lib/workspace.js";

before(() => {
  process.env.HW4_RUNS_ROOT = resolveHomeworkPath(".test-runs");
  process.env.HW4_CURRENT_APP_ROOT = resolveHomeworkPath(".test-current");
  process.env.HW4_BENCHMARK_ROOT = resolveHomeworkPath(".test-benchmark");
});

test("creates an isolated run workspace from the baseline app", async () => {
  const config = await loadConfig();
  const runId = `test-workspace-${Date.now()}`;
  const workspace = await createRunWorkspace(config, { scenarioId: "bug-001", runId });
  const copiedSource = await readFile(path.join(workspace.appRoot, "src", "quoteCalculator.js"), "utf8");

  assert.match(copiedSource, /item\.quantity \+ item\.unitPrice/);
  assert.equal(workspace.runRoot, getRunRoot("bug-001", runId));
});

test("rejects writes outside a run workspace", async () => {
  const config = await loadConfig();
  const runId = `test-safety-${Date.now()}`;
  const workspace = await createRunWorkspace(config, { scenarioId: "bug-001", runId });

  assert.throws(
    () => assertInsideRun(workspace.runRoot, path.join(workspace.runRoot, "..", "outside.txt")),
    /Refusing to write outside run workspace/
  );

  await writeRunFile(workspace.runRoot, "nested/safe.txt", "safe");
  assert.equal(await readFile(path.join(workspace.runRoot, "nested", "safe.txt"), "utf8"), "safe");
});
