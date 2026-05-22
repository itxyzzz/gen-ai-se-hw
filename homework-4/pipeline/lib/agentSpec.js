import { readFile, readdir } from "node:fs/promises";
import path from "node:path";
import { resolveHomeworkPath } from "./config.js";

export async function loadAgentSpecs(agentsDir = resolveHomeworkPath("agents")) {
  const entries = await readdir(agentsDir, { withFileTypes: true });
  const specs = new Map();

  for (const entry of entries) {
    if (!entry.isFile() || !entry.name.endsWith(".agent.md")) {
      continue;
    }

    const fullPath = path.join(agentsDir, entry.name);
    const spec = await parseAgentSpec(fullPath);
    specs.set(spec.frontmatter.id, spec);
  }

  return specs;
}

export async function parseAgentSpec(filePath) {
  const raw = await readFile(filePath, "utf8");
  const match = raw.match(/^---\r?\n([\s\S]*?)\r?\n---\r?\n([\s\S]*)$/);
  if (!match) {
    throw new Error(`Agent spec missing frontmatter: ${filePath}`);
  }

  const frontmatter = parseSimpleYaml(match[1]);
  validateAgentFrontmatter(frontmatter, filePath);
  return {
    filePath,
    frontmatter,
    body: match[2].trim()
  };
}

export function validateAgentFrontmatter(frontmatter, filePath = "agent") {
  const requiredKeys = ["id", "role", "model_policy", "reasoning_effort", "inputs", "outputs", "skills", "allowed_actions"];
  for (const key of requiredKeys) {
    if (!(key in frontmatter)) {
      throw new Error(`${filePath} missing required frontmatter key: ${key}`);
    }
  }
}

export async function validateManifestAgents(config, specs) {
  for (const stageId of config.stageOrder) {
    const spec = specs.get(stageId);
    if (!spec) {
      throw new Error(`Manifest stage has no matching agent spec: ${stageId}`);
    }

    const policy = config.modelPolicies[spec.frontmatter.model_policy];
    if (!policy) {
      throw new Error(`Agent ${stageId} references unknown model policy: ${spec.frontmatter.model_policy}`);
    }

    for (const skillPath of spec.frontmatter.skills) {
      await readFile(resolveHomeworkPath(skillPath), "utf8");
    }
  }
}

function parseSimpleYaml(rawYaml) {
  const result = {};
  const lines = rawYaml.split(/\r?\n/);
  let currentListKey = null;

  for (const line of lines) {
    if (line.trim().length === 0) {
      continue;
    }

    const listMatch = line.match(/^\s+-\s+(.*)$/);
    if (listMatch && currentListKey) {
      result[currentListKey].push(listMatch[1].trim());
      continue;
    }

    const keyValueMatch = line.match(/^([A-Za-z0-9_-]+):\s*(.*)$/);
    if (!keyValueMatch) {
      throw new Error(`Unsupported frontmatter line: ${line}`);
    }

    const [, key, value] = keyValueMatch;
    if (value === "[]") {
      result[key] = [];
      currentListKey = null;
    } else if (value.length === 0) {
      result[key] = [];
      currentListKey = key;
    } else {
      result[key] = value.trim();
      currentListKey = null;
    }
  }

  return result;
}
