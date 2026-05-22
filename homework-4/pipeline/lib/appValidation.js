import assert from "node:assert/strict";
import { pathToFileURL } from "node:url";
import path from "node:path";

export async function validateFixedApp(appRoot) {
  const { calculateQuote, applyDiscount } = await importFresh(path.join(appRoot, "src", "quoteCalculator.js"));
  const { loadCatalog } = await importFresh(path.join(appRoot, "src", "catalogRepository.js"));

  const quote = calculateQuote([{ sku: "WIDGET", quantity: 3, unitPrice: 25 }]);
  assert.equal(quote.lines[0].lineTotal, 75);
  assert.equal(quote.subtotal, 75);
  assert.equal(applyDiscount(75, "SAVE10"), 67.5);
  await assert.rejects(() => loadCatalog("../catalogs/default"), /Invalid catalog name/);

  return {
    exitCode: 0,
    command: "in-process app validation",
    cwd: appRoot,
    startedAt: new Date().toISOString(),
    endedAt: new Date().toISOString(),
    stdout: "Fixed app validation passed.",
    stderr: ""
  };
}

export async function validateSeededBaseline(appRoot) {
  const { calculateQuote, applyDiscount } = await importFresh(path.join(appRoot, "src", "quoteCalculator.js"));
  const { loadCatalog } = await importFresh(path.join(appRoot, "src", "catalogRepository.js"));
  const failures = [];

  try {
    assert.equal(calculateQuote([{ sku: "WIDGET", quantity: 3, unitPrice: 25 }]).lines[0].lineTotal, 75);
  } catch {
    failures.push("line total multiplication");
  }

  try {
    assert.equal(applyDiscount(75, "SAVE10"), 67.5);
  } catch {
    failures.push("SAVE10 percentage discount");
  }

  try {
    await assert.rejects(() => loadCatalog("../catalogs/default"), /Invalid catalog name/);
  } catch {
    failures.push("catalog traversal rejection");
  }

  return failures;
}

async function importFresh(filePath) {
  const url = pathToFileURL(filePath);
  url.searchParams.set("cacheBust", `${Date.now()}-${Math.random()}`);
  return import(url.href);
}
