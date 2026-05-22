import { spawn } from "node:child_process";
import { mkdir, readFile, stat, writeFile } from "node:fs/promises";
import path from "node:path";

export async function runCommand(command, args, options = {}) {
  const startedAt = new Date().toISOString();
  const output = await new Promise((resolve) => {
    const child = spawn(command, args, {
      cwd: options.cwd,
      shell: process.platform === "win32"
    });
    let stdout = "";
    let stderr = "";

    child.stdout.on("data", (chunk) => {
      stdout += chunk.toString();
    });
    child.stderr.on("data", (chunk) => {
      stderr += chunk.toString();
    });
    child.on("close", (exitCode) => {
      resolve({ exitCode, stdout, stderr });
    });
  });

  return {
    command: [command, ...args].join(" "),
    cwd: options.cwd,
    startedAt,
    endedAt: new Date().toISOString(),
    ...output
  };
}

export async function appendCommandLog(runRoot, result) {
  const logPath = path.join(runRoot, "command-log.md");
  const block = [
    `## ${result.command}`,
    "",
    `- CWD: ${result.cwd}`,
    `- Exit code: ${result.exitCode}`,
    `- Started: ${result.startedAt}`,
    `- Ended: ${result.endedAt}`,
    "",
    "### STDOUT",
    "",
    "```text",
    trimForLog(result.stdout),
    "```",
    "",
    "### STDERR",
    "",
    "```text",
    trimForLog(result.stderr),
    "```",
    ""
  ].join("\n");
  await writeFile(logPath, `${await readExisting(logPath)}${block}`, "utf8");
}

export async function writeJson(filePath, value) {
  await mkdir(path.dirname(filePath), { recursive: true });
  await writeFile(filePath, `${JSON.stringify(value, null, 2)}\n`, "utf8");
}

export async function validateRequiredArtifacts(runRoot, requiredArtifacts) {
  const missing = [];
  for (const relativePath of requiredArtifacts) {
    try {
      await stat(path.join(runRoot, relativePath));
    } catch {
      missing.push(relativePath);
    }
  }

  if (missing.length > 0) {
    throw new Error(`Run is missing required artifacts: ${missing.join(", ")}`);
  }
}

async function readExisting(filePath) {
  try {
    return await readFile(filePath, "utf8");
  } catch {
    return "";
  }
}

function trimForLog(value) {
  const text = value.trim();
  if (text.length <= 6000) {
    return text;
  }
  return `${text.slice(0, 6000)}\n... truncated ...`;
}
