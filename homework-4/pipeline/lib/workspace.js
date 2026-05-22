import { mkdir, rm, cp, writeFile, readFile, readdir } from "node:fs/promises";
import path from "node:path";
import { getScenario, resolveHomeworkPath } from "./config.js";

export function getRunRoot(scenarioId, runId) {
  return path.resolve(process.env.HW4_RUNS_ROOT ?? resolveHomeworkPath("runs"), scenarioId, runId);
}

export async function createRunWorkspace(config, options = {}) {
  const scenarioId = options.scenarioId ?? config.defaultScenario;
  const runId = options.runId ?? config.defaultRunId;
  const scenario = getScenario(config, scenarioId);
  const runRoot = getRunRoot(scenarioId, runId);
  const appRoot = path.join(runRoot, "app");

  await rm(runRoot, { recursive: true, force: true });
  await mkdir(runRoot, { recursive: true });
  await cp(resolveHomeworkPath(scenario.baselineApp), appRoot, { recursive: true });
  await mkdir(path.join(runRoot, "research"), { recursive: true });
  await mkdir(path.join(runRoot, "raw-responses"), { recursive: true });

  return {
    scenarioId,
    runId,
    scenario,
    runRoot,
    appRoot
  };
}

export function assertInsideRun(runRoot, candidatePath) {
  const resolvedRunRoot = path.resolve(runRoot);
  const resolvedCandidate = path.resolve(candidatePath);
  const relative = path.relative(resolvedRunRoot, resolvedCandidate);

  if (relative.startsWith("..") || path.isAbsolute(relative)) {
    throw new Error(`Refusing to write outside run workspace: ${candidatePath}`);
  }
}

export async function writeRunFile(runRoot, relativePath, content) {
  const targetPath = path.join(runRoot, relativePath);
  assertInsideRun(runRoot, targetPath);
  await mkdir(path.dirname(targetPath), { recursive: true });
  await writeFile(targetPath, content, "utf8");
}

export async function copyRunToCurrent(config, scenarioId, runId) {
  const scenario = getScenario(config, scenarioId);
  const sourceApp = path.join(getRunRoot(scenarioId, runId), "app");
  const targetApp = path.resolve(process.env.HW4_CURRENT_APP_ROOT ?? resolveHomeworkPath(scenario.currentApp));
  await mkdir(targetApp, { recursive: true });
  await copyTreePreservingLockedFiles(sourceApp, targetApp);
  return targetApp;
}

export async function markPromoted(scenarioId, runId) {
  const metadataPath = path.join(getRunRoot(scenarioId, runId), "run-metadata.json");
  const metadata = JSON.parse(await readFile(metadataPath, "utf8"));
  metadata.promoted = true;
  await writeFile(metadataPath, `${JSON.stringify(metadata, null, 2)}\n`, "utf8");
  return metadata;
}

async function copyTreePreservingLockedFiles(sourceDir, targetDir) {
  await mkdir(targetDir, { recursive: true });
  const entries = await readdir(sourceDir, { withFileTypes: true });
  for (const entry of entries) {
    const sourcePath = path.join(sourceDir, entry.name);
    const targetPath = path.join(targetDir, entry.name);
    if (entry.isDirectory()) {
      await copyTreePreservingLockedFiles(sourcePath, targetPath);
    } else if (entry.isFile()) {
      const sourceContent = await readFile(sourcePath);
      const targetContent = await readExistingBuffer(targetPath);
      if (targetContent && Buffer.compare(sourceContent, targetContent) === 0) {
        continue;
      }
      await mkdir(path.dirname(targetPath), { recursive: true });
      await writeFile(targetPath, sourceContent);
    }
  }
}

async function readExistingBuffer(filePath) {
  try {
    return await readFile(filePath);
  } catch {
    return null;
  }
}
