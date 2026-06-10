import { readFile } from "node:fs/promises";
import path from "node:path";
import { fileURLToPath } from "node:url";

const moduleDirectory = path.dirname(fileURLToPath(import.meta.url));
const catalogDirectory = path.resolve(moduleDirectory, "../data/catalogs");

export async function loadCatalog(catalogName = "default") {
  if (!/^[A-Za-z0-9_-]+$/u.test(catalogName)) {
    throw new Error("Invalid catalog name.");
  }

  const catalogPath = path.join(catalogDirectory, `${catalogName}.json`);
  const resolvedCatalogPath = path.resolve(catalogDirectory, catalogPath);
  const relativeCatalogPath = path.relative(catalogDirectory, resolvedCatalogPath);

  if (relativeCatalogPath.startsWith("..") || path.isAbsolute(relativeCatalogPath)) {
    throw new Error("Invalid catalog name.");
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
