import { readFile } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";

const moduleDirectory = path.dirname(fileURLToPath(import.meta.url));
const catalogDirectory = path.resolve(moduleDirectory, "../data/catalogs");

export async function loadCatalog(catalogName = "default") {
  // Validate catalog name to prevent path traversal
  const validNamePattern = /^[a-zA-Z0-9_-]+$/;
  if (!validNamePattern.test(catalogName)) {
    throw new Error("Invalid catalog name: must contain only alphanumeric characters, hyphens, and underscores");
  }

  const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);

  // Resolve the path and ensure it's within the catalog directory
  const resolvedPath = path.resolve(catalogPath);
  const resolvedCatalogDir = path.resolve(catalogDirectory);

  if (!resolvedPath.startsWith(resolvedCatalogDir + path.sep) && resolvedPath !== resolvedCatalogDir) {
    throw new Error("Invalid catalog name: path traversal detected");
  }

  const rawCatalog = await readFile(catalogPath, "utf8");
  return JSON.parse(rawCatalog);
}

export function findCatalogItem(catalog, sku) {
  const item = catalog.items.find((candidate) => candidate.sku === sku);
  if (!item) {
    throw new Error(`Unknown catalog item: ${sku}`);
  }
  return item;
}
