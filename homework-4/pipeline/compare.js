import { mkdir, readFile, readdir, writeFile } from "node:fs/promises";
import path from "node:path";
import { pathToFileURL } from "node:url";
import { loadConfig, resolveHomeworkPath } from "./lib/config.js";

if (process.argv[1] && import.meta.url === pathToFileURL(process.argv[1]).href) {
  const args = parseArgs(process.argv.slice(2));
  const config = await loadConfig();
  const scenarioId = args.scenario ?? config.defaultScenario;
  const scoredRuns = await generateComparison(config, scenarioId);
  process.stdout.write(`Generated benchmark comparison for ${scoredRuns.length} run(s).\n`);
}

export async function generateComparison(config, scenarioId = config.defaultScenario) {
  const runRoot = path.resolve(process.env.HW4_RUNS_ROOT ?? resolveHomeworkPath("runs"), scenarioId);
  const benchmarkRoot = path.resolve(process.env.HW4_BENCHMARK_ROOT ?? resolveHomeworkPath("benchmark"));

  await mkdir(benchmarkRoot, { recursive: true });
  const runs = (await readRuns(runRoot)).filter((run) => run.status === "completed");
  const scoredRuns = runs.map((run) => scoreRun(run, config.benchmarkWeights));

  await writeFile(
    path.join(benchmarkRoot, `${scenarioId}-results.json`),
    `${JSON.stringify(scoredRuns, null, 2)}\n`,
    "utf8"
  );
  await writeFile(path.join(benchmarkRoot, `${scenarioId}-comparison.md`), renderComparison(scenarioId, scoredRuns), "utf8");
  await writeFile(path.join(benchmarkRoot, "scoring-rubric.md"), renderRubric(config.benchmarkWeights), "utf8");
  return scoredRuns;
}

async function readRuns(root) {
  try {
    const entries = await readdir(root, { withFileTypes: true });
    const runs = [];
    for (const entry of entries) {
      if (!entry.isDirectory()) {
        continue;
      }
      const metadataPath = path.join(root, entry.name, "run-metadata.json");
      try {
        runs.push(JSON.parse(await readFile(metadataPath, "utf8")));
      } catch {
        // Ignore incomplete run folders.
      }
    }
    return runs;
  } catch {
    return [];
  }
}

function scoreRun(run, weights) {
  const testsPassing = run.metrics?.testsPassing === true;
  const noImportantSecurityFindings = run.metrics?.securityFindingsHighOrWorse === 0;
  const hasGeneratedTests = (run.metrics?.testsAdded ?? 0) > 0;
  const completed = run.status === "completed";

  const score =
    (testsPassing ? weights.correctness : 0) +
    (noImportantSecurityFindings ? weights.securityRemediation : 0) +
    (hasGeneratedTests ? weights.testQuality : 0) +
    (completed ? weights.maintainability : 0) +
    (completed && run.manualInterventions === 0 ? weights.reproducibility : 0);

  return {
    runId: run.runId,
    scenarioId: run.scenarioId,
    adapter: run.adapter,
    model: run.model,
    reasoningEffort: run.reasoningEffort,
    status: run.status,
    promoted: run.promoted,
    score,
    metrics: run.metrics
  };
}

function renderComparison(scenarioId, runs) {
  const rows = runs.map((run) => `| ${run.runId} | ${run.adapter} | ${run.model} | ${run.status} | ${run.score} | ${run.promoted ? "yes" : "no"} |`);
  return [
    `# Benchmark Comparison: ${scenarioId}`,
    "",
    "| Run | Adapter | Model | Status | Score | Promoted |",
    "| --- | --- | --- | --- | ---: | --- |",
    ...rows,
    ""
  ].join("\n");
}

function renderRubric(weights) {
  return [
    "# Benchmark Scoring Rubric",
    "",
    `- Correctness: ${weights.correctness} points for passing changed-app tests.`,
    `- Security remediation: ${weights.securityRemediation} points for no HIGH-or-worse findings.`,
    `- Test quality: ${weights.testQuality} points for generated tests.`,
    `- Maintainability and minimality: ${weights.maintainability} points for completed bounded changes.`,
    `- Reproducibility and autonomy: ${weights.reproducibility} points for zero manual interventions.`,
    ""
  ].join("\n");
}

function parseArgs(argv) {
  const parsed = {};
  for (let index = 0; index < argv.length; index += 1) {
    if (argv[index] === "--scenario") {
      parsed.scenario = argv[index + 1];
      index += 1;
    }
  }
  return parsed;
}
