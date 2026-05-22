import { readdir, readFile, stat, writeFile } from "node:fs/promises";
import path from "node:path";

export async function writeDirectoryDiff(beforeDir, afterDir, outputPath) {
  const beforeFiles = await listFiles(beforeDir);
  const afterFiles = await listFiles(afterDir);
  const allFiles = new Set([...beforeFiles, ...afterFiles]);
  const sections = ["# Patch Summary", ""];

  for (const relativePath of [...allFiles].sort()) {
    const beforePath = path.join(beforeDir, relativePath);
    const afterPath = path.join(afterDir, relativePath);
    const before = beforeFiles.has(relativePath) ? await readFile(beforePath, "utf8") : "";
    const after = afterFiles.has(relativePath) ? await readFile(afterPath, "utf8") : "";

    if (before === after) {
      continue;
    }

    sections.push(`## ${relativePath}`, "");
    sections.push("```diff");
    if (!beforeFiles.has(relativePath)) {
      sections.push(`+ added ${relativePath}`);
    } else if (!afterFiles.has(relativePath)) {
      sections.push(`- removed ${relativePath}`);
    } else {
      sections.push(...summarizeChangedLines(before, after));
    }
    sections.push("```", "");
  }

  if (sections.length === 2) {
    sections.push("No file changes detected.", "");
  }

  await writeFile(outputPath, `${sections.join("\n")}\n`, "utf8");
}

async function listFiles(rootDir, currentDir = rootDir, accumulator = new Set()) {
  const entries = await readdir(currentDir, { withFileTypes: true });
  for (const entry of entries) {
    const fullPath = path.join(currentDir, entry.name);
    if (entry.isDirectory()) {
      await listFiles(rootDir, fullPath, accumulator);
    } else if (entry.isFile()) {
      const fileStat = await stat(fullPath);
      if (fileStat.size < 500000) {
        accumulator.add(path.relative(rootDir, fullPath).replaceAll("\\", "/"));
      }
    }
  }
  return accumulator;
}

function summarizeChangedLines(before, after) {
  const beforeLines = before.split(/\r?\n/);
  const afterLines = after.split(/\r?\n/);
  const maxLength = Math.max(beforeLines.length, afterLines.length);
  const diffLines = [];

  for (let index = 0; index < maxLength; index += 1) {
    if (beforeLines[index] !== afterLines[index]) {
      if (beforeLines[index] !== undefined) {
        diffLines.push(`- ${beforeLines[index]}`);
      }
      if (afterLines[index] !== undefined) {
        diffLines.push(`+ ${afterLines[index]}`);
      }
    }

    if (diffLines.length >= 80) {
      diffLines.push("... truncated ...");
      break;
    }
  }

  return diffLines;
}
