import { readFile } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";

const moduleDirectory = path.dirname(fileURLToPath(import.meta.url));
const catalogDirectory = path.resolve(moduleDirectory, "../data/catalogs");

export async function loadCatalog(catalogName = "default") {
  if (typeof catalogName !== "string" || !/^[a-zA-Z0-9_-]+$/.test(catalogName)) {
    throw new Error(`Invalid catalog name: ${catalogName}`);
  }
  const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);
  const resolvedPath = path.resolve(catalogPath);
  if (!resolvedPath.startsWith(catalogDirectory + path.sep)) {
    throw new Error(`Invalid catalog name: ${catalogName}`);
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
