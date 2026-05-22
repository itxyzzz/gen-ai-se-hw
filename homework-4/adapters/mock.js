import { mkdir, readFile, writeFile } from "node:fs/promises";
import path from "node:path";
import { loadAgentSpecs } from "../pipeline/lib/agentSpec.js";
import { appendCommandLog, validateRequiredArtifacts, writeJson } from "../pipeline/lib/artifacts.js";
import { validateFixedApp } from "../pipeline/lib/appValidation.js";
import { resolveHomeworkPath } from "../pipeline/lib/config.js";
import { writeDirectoryDiff } from "../pipeline/lib/diff.js";
import { createRunWorkspace, writeRunFile } from "../pipeline/lib/workspace.js";

export async function runMockPipeline(config, options = {}) {
  const workspace = await createRunWorkspace(config, options);
  const agentSpecs = await loadAgentSpecs();
  const startedAt = new Date().toISOString();
  const metadata = createMetadata(config, workspace, options, startedAt);
  const stages = [];

  await executeStage(config, agentSpecs, "bug-researcher", workspace, stages, async () => {
    await writeRunFile(workspace.runRoot, "research/codebase-research.md", await readFile(resolveHomeworkPath("scenarios", workspace.scenarioId, "research", "codebase-research.md"), "utf8"));
  });
  await writeVerifiedResearch(workspace.runRoot);
  await executeStage(config, agentSpecs, "research-verifier", workspace, stages, async () => {});
  await executeStage(config, agentSpecs, "bug-planner", workspace, stages, async () => {
    await writeRunFile(workspace.runRoot, "implementation-plan.md", await readFile(resolveHomeworkPath("scenarios", workspace.scenarioId, "implementation-plan.md"), "utf8"));
  });
  const postChangeResults = [];
  await executeStage(config, agentSpecs, "bug-fixer", workspace, stages, async () => {
    await applyFixes(workspace.appRoot, workspace, postChangeResults);
    await writeFixSummary(workspace.runRoot, postChangeResults);
  });
  await executeStage(config, agentSpecs, "security-verifier", workspace, stages, async () => {
    await writeSecurityReport(workspace.runRoot);
  });
  let finalTestResult;
  await executeStage(config, agentSpecs, "unit-test-generator", workspace, stages, async () => {
    await addGeneratedTests(workspace.appRoot);
    finalTestResult = await runAndLogTests(workspace, "after generated regression tests");
    await writeTestReport(workspace.runRoot, finalTestResult);
  });
  await writeDirectoryDiff(resolveHomeworkPath(workspace.scenario.baselineApp), workspace.appRoot, path.join(workspace.runRoot, "patch.diff"));
  await writeRawResponse(workspace.runRoot);

  metadata.endedAt = new Date().toISOString();
  metadata.status = finalTestResult.exitCode === 0 ? "completed" : "failed";
  metadata.metrics = {
    bugsFixed: 3,
    securityIssuesFixed: 1,
    testsAdded: 2,
    testsPassing: finalTestResult.exitCode === 0,
    postChangeTestExitCodes: postChangeResults.map((result) => result.exitCode),
    finalTestExitCode: finalTestResult.exitCode,
    securityFindingsHighOrWorse: 0,
    manualInterventionCount: 0
  };
  metadata.manualInterventions = 0;
  metadata.stages = stages;
  await writeJson(path.join(workspace.runRoot, "run-metadata.json"), metadata);
  await validateRequiredArtifacts(workspace.runRoot, config.requiredArtifacts);

  return { workspace, metadata };
}

function createMetadata(config, workspace, options, startedAt) {
  return {
    runId: workspace.runId,
    scenarioId: workspace.scenarioId,
    adapter: "mock",
    provider: "deterministic-local",
    model: options.model ?? "mock-deterministic-agent",
    reasoningEffort: options.reasoning ?? "medium",
    pipelineVersion: config.pipelineVersion,
    baselineCommit: "local-working-tree",
    startedAt,
    endedAt: null,
    status: "running",
    pipelineCommand: "npm run pipeline:mock",
    testCommand: workspace.scenario.testCommand,
    metrics: {},
    manualInterventions: 0,
    promoted: false
  };
}

async function writeVerifiedResearch(runRoot) {
  await writeRunFile(runRoot, "research/verified-research.md", [
    "# Verified Research",
    "",
    "## Verification Summary",
    "",
    "- Result: pass",
    "- Research Quality per skill: Level 4 - Verified",
    "- Planner usability: safe to use",
    "",
    "## Verified Claims",
    "",
    "- `app/baseline/src/quoteCalculator.js:4` contains `return item.quantity + item.unitPrice;`.",
    "- `app/baseline/src/quoteCalculator.js:12` contains `return subtotal - 10;`.",
    "- `app/baseline/src/catalogRepository.js:8` joins an unchecked catalog name into a file path.",
    "- Baseline tests describe the intended corrected behavior.",
    "",
    "## Discrepancies Found",
    "",
    "- None.",
    "",
    "## Research Quality Assessment",
    "",
    "Level 4 - Verified. All cited files and code snippets match the baseline source.",
    "",
    "## References",
    "",
    "- `scenarios/bug-001/research/codebase-research.md`",
    "- `app/baseline/src/quoteCalculator.js`",
    "- `app/baseline/src/catalogRepository.js`",
    ""
  ].join("\n"));
}

async function applyFixes(appRoot, workspace, postChangeResults) {
  const calculatorPath = path.join(appRoot, "src", "quoteCalculator.js");
  const repositoryPath = path.join(appRoot, "src", "catalogRepository.js");
  const calculator = await readFile(calculatorPath, "utf8");
  await writeFile(
    calculatorPath,
    calculator
      .replace("return item.quantity + item.unitPrice;", "return item.quantity * item.unitPrice;")
      .replace("return subtotal - 10;", "return subtotal - 10;"),
    "utf8"
  );
  postChangeResults.push(await runAndLogTests(workspace, "after line total fix"));

  const calculatorAfterLineFix = await readFile(calculatorPath, "utf8");
  await writeFile(
    calculatorPath,
    calculatorAfterLineFix.replace("return subtotal - 10;", "return roundCurrency(subtotal * 0.9);"),
    "utf8"
  );
  postChangeResults.push(await runAndLogTests(workspace, "after SAVE10 discount fix"));

  const repository = await readFile(repositoryPath, "utf8");
  await writeFile(
    repositoryPath,
    repository.replace(
      [
        "export async function loadCatalog(catalogName = \"default\") {",
        "  const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);",
        "  const rawCatalog = await readFile(catalogPath, \"utf8\");",
        "  return JSON.parse(rawCatalog);",
        "}"
      ].join("\n"),
      [
        "export async function loadCatalog(catalogName = \"default\") {",
        "  if (!/^[a-zA-Z0-9_-]+$/.test(catalogName)) {",
        "    throw new Error(\"Invalid catalog name.\");",
        "  }",
        "",
        "  const catalogPath = path.resolve(catalogDirectory, `${catalogName}.json`);",
        "  const relativePath = path.relative(catalogDirectory, catalogPath);",
        "  if (relativePath.startsWith(\"..\") || path.isAbsolute(relativePath)) {",
        "    throw new Error(\"Invalid catalog name.\");",
        "  }",
        "",
        "  const rawCatalog = await readFile(catalogPath, \"utf8\");",
        "  return JSON.parse(rawCatalog);",
        "}"
      ].join("\n")
    ),
    "utf8"
  );
  postChangeResults.push(await runAndLogTests(workspace, "after catalog validation fix"));
}

async function writeFixSummary(runRoot, postChangeResults) {
  await writeRunFile(runRoot, "fix-summary.md", [
    "# Fix Summary",
    "",
    "## Changes Made",
    "",
    "| File | Location | Before | After | Test Result |",
    "| --- | --- | --- | --- | --- |",
    `| \`src/quoteCalculator.js\` | \`calculateLineTotal\` | \`quantity + unitPrice\` | \`quantity * unitPrice\` | ${postChangeResults[0]?.exitCode === 1 ? "Expected partial failure remained" : "Passed"} |`,
    `| \`src/quoteCalculator.js\` | \`applyDiscount\` | \`subtotal - 10\` | \`roundCurrency(subtotal * 0.9)\` | ${postChangeResults[1]?.exitCode === 1 ? "Expected security failure remained" : "Passed"} |`,
    `| \`src/catalogRepository.js\` | \`loadCatalog\` | unchecked path join | validated catalog name plus resolved path containment check | ${postChangeResults[2]?.exitCode === 0 ? "Passed" : "Failed"} |`,
    "",
    "## Overall Status",
    "",
    "Completed. All planned changes were applied inside the isolated run workspace.",
    "",
    "## Manual Verification",
    "",
    "Run `node --test --test-isolation=none tests/*.test.js` from the run workspace app directory. The deterministic adapter also records in-process validation because this Windows sandbox blocks child processes spawned from inside Node.",
    "",
    "## References",
    "",
    "- `implementation-plan.md`",
    "- `research/verified-research.md`",
    ""
  ].join("\n"));
}

async function writeSecurityReport(runRoot) {
  await writeRunFile(runRoot, "security-report.md", [
    "# Security Report",
    "",
    "## Scope",
    "",
    "- `fix-summary.md`",
    "- `app/src/catalogRepository.js`",
    "- `app/src/quoteCalculator.js`",
    "",
    "## Findings",
    "",
    "| Severity | File:Line | Finding | Remediation |",
    "| --- | --- | --- | --- |",
    "| INFO | `app/src/catalogRepository.js:8` | Catalog names are restricted to simple identifiers and resolved path containment is checked. | No further action required. |",
    "",
    "## Injection, Secrets, Validation, Dependencies, XSS/CSRF",
    "",
    "- Injection: no command or SQL execution exists.",
    "- Secrets: no hardcoded secrets found.",
    "- Validation: catalog name validation added.",
    "- Dependencies: no third-party runtime dependencies.",
    "- XSS/CSRF: not applicable to this CLI app.",
    "",
    "## Overall Status",
    "",
    "No CRITICAL, HIGH, MEDIUM, or LOW vulnerabilities found in the changed code.",
    ""
  ].join("\n"));
}

async function addGeneratedTests(appRoot) {
  const generatedTestPath = path.join(appRoot, "tests", "generated-regression.test.js");
  await writeFile(generatedTestPath, [
    "import assert from \"node:assert/strict\";",
    "import test from \"node:test\";",
    "import { calculateQuote } from \"../src/quoteCalculator.js\";",
    "import { loadCatalog } from \"../src/catalogRepository.js\";",
    "",
    "test(\"generated regression: combines quantity totals and percentage discount\", () => {",
    "  const quote = calculateQuote([",
    "    { sku: \"WIDGET\", quantity: 2, unitPrice: 25 },",
    "    { sku: \"GADGET\", quantity: 4, unitPrice: 12.5 }",
    "  ], { discountCode: \"SAVE10\" });",
    "",
    "  assert.equal(quote.subtotal, 100);",
    "  assert.equal(quote.total, 90);",
    "});",
    "",
    "test(\"generated regression: catalog path traversal remains blocked\", async () => {",
    "  await assert.rejects(() => loadCatalog(\"../catalogs/default\"), /Invalid catalog name/);",
    "});",
    ""
  ].join("\n"), "utf8");
}

async function writeTestReport(runRoot, result) {
  await writeRunFile(runRoot, "test-report.md", [
    "# Test Report",
    "",
    "## Generated Tests",
    "",
    "- Added `tests/generated-regression.test.js` for corrected quote totals, percentage discount behavior, and traversal rejection.",
    "",
    "## FIRST Assessment",
    "",
    "- Fast: uses built-in `node:test` with local files only.",
    "- Independent: tests create their own inputs.",
    "- Repeatable: no network, clock, or shared external state.",
    "- Self-validating: assertions check exact totals and rejection behavior.",
    "- Timely: tests target the changed behavior from `fix-summary.md`.",
    "",
    "## Commands Run",
    "",
    "- In-process validation equivalent to `node --test --test-isolation=none tests/*.test.js` inside the run app.",
    "",
    "## Results",
    "",
    `- Exit code: ${result.exitCode}`,
    "",
    "## Remaining Gaps",
    "",
    "- No remaining gaps for the seeded defects.",
    ""
  ].join("\n"));
}

async function runAndLogTests(workspace, label) {
  let result;
  try {
    result = await validateFixedApp(workspace.appRoot);
  } catch (error) {
    result = {
      exitCode: 1,
      command: `in-process app validation (${label})`,
      cwd: workspace.appRoot,
      startedAt: new Date().toISOString(),
      endedAt: new Date().toISOString(),
      stdout: "",
      stderr: error.stack ?? error.message
    };
    await appendCommandLog(workspace.runRoot, result);
    return result;
  }
  result.command = `in-process app validation (${label})`;
  await appendCommandLog(workspace.runRoot, result);
  return result;
}

async function writeRawResponse(runRoot) {
  await mkdir(path.join(runRoot, "raw-responses"), { recursive: true });
  await writeFile(
    path.join(runRoot, "raw-responses", "mock-adapter.json"),
    `${JSON.stringify({ adapter: "mock", note: "Deterministic local adapter; no remote model response." }, null, 2)}\n`,
    "utf8"
  );
}

async function executeStage(config, agentSpecs, stageId, workspace, stages, action) {
  const spec = agentSpecs.get(stageId);
  const policy = config.modelPolicies[spec.frontmatter.model_policy];
  const startedAt = new Date().toISOString();
  const loadedSkills = [];

  for (const skillPath of spec.frontmatter.skills) {
    loadedSkills.push({
      path: skillPath,
      contentLength: (await readFile(resolveHomeworkPath(skillPath), "utf8")).length
    });
  }

  await action();
  const stageRecord = {
    stageId,
    role: spec.frontmatter.role,
    modelPolicy: spec.frontmatter.model_policy,
    model: policy.model,
    reasoningEffort: policy.reasoningEffort,
    skillsLoaded: loadedSkills.map((skill) => skill.path),
    startedAt,
    endedAt: new Date().toISOString(),
    status: "completed"
  };
  stages.push(stageRecord);
  await writeRunFile(workspace.runRoot, `raw-responses/${stageId}.json`, `${JSON.stringify(stageRecord, null, 2)}\n`);
}
