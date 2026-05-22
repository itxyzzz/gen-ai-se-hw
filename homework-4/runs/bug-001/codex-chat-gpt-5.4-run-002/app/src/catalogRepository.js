import { readFile } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";

const moduleDirectory = path.dirname(fileURLToPath(import.meta.url));
const catalogDirectory = path.resolve(moduleDirectory, "../data/catalogs");
const catalogNamePattern = /^[A-Za-z0-9_-]+$/;

export async function loadCatalog(catalogName = "default") {
  const catalogPath = resolveCatalogPath(catalogName);
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

function resolveCatalogPath(catalogName) {
  if (typeof catalogName !== "string" || !catalogNamePattern.test(catalogName)) {
    throw new Error("Invalid catalog name.");
  }

  const catalogPath = path.resolve(catalogDirectory, `${catalogName}.json`);
  const relativePath = path.relative(catalogDirectory, catalogPath);

  if (relativePath.startsWith("..") || path.isAbsolute(relativePath)) {
    throw new Error("Invalid catalog name.");
  }

  return catalogPath;
}
