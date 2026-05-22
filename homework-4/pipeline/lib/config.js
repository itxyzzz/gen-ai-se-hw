import { readFile } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";

export const homeworkRoot = path.resolve(path.dirname(fileURLToPath(import.meta.url)), "../..");

export async function loadConfig(configPath = path.join(homeworkRoot, "pipeline.config.yaml")) {
  const rawConfig = await readFile(configPath, "utf8");
  const config = JSON.parse(rawConfig);
  validateConfigShape(config);
  return config;
}

export function resolveHomeworkPath(...segments) {
  return path.resolve(homeworkRoot, ...segments);
}

export function validateConfigShape(config) {
  const requiredKeys = ["pipelineVersion", "stageOrder", "modelPolicies", "scenarios", "requiredArtifacts"];
  for (const key of requiredKeys) {
    if (!(key in config)) {
      throw new Error(`Missing config key: ${key}`);
    }
  }

  if (!Array.isArray(config.stageOrder) || config.stageOrder.length === 0) {
    throw new Error("Config stageOrder must be a non-empty array.");
  }

  for (const stageId of config.stageOrder) {
    if (typeof stageId !== "string" || stageId.length === 0) {
      throw new Error("Every stage id must be a non-empty string.");
    }
  }
}

export function getScenario(config, scenarioId = config.defaultScenario) {
  const scenario = config.scenarios[scenarioId];
  if (!scenario) {
    throw new Error(`Unknown scenario: ${scenarioId}`);
  }
  return scenario;
}
