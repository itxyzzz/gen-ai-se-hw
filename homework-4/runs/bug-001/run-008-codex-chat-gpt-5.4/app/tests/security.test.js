import assert from "node:assert/strict";
import test from "node:test";
import { loadCatalog } from "../src/catalogRepository.js";

test("loads a valid catalog name from the catalogs directory", async () => {
  const catalog = await loadCatalog("default");

  assert.equal(catalog.currency, "USD");
  assert.equal(Array.isArray(catalog.items), true);
  assert.equal(catalog.items.length, 2);
});

test("rejects catalog names that escape the catalog directory", async () => {
  await assert.rejects(
    () => loadCatalog("../catalogs/default"),
    /Invalid catalog name/
  );
});

test("rejects catalog names with unsupported characters", async () => {
  const invalidNames = ["", "default.json", "catalog name", "catalog..name", "../default", "catalog/../../etc"];

  for (const catalogName of invalidNames) {
    await assert.rejects(
      () => loadCatalog(catalogName),
      /Invalid catalog name/
    );
  }
});
