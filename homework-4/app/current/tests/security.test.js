import assert from "node:assert/strict";
import test from "node:test";
import { loadCatalog } from "../src/catalogRepository.js";

test("rejects catalog names that escape the catalog directory", async () => {
  await assert.rejects(
    () => loadCatalog("../catalogs/default"),
    /Invalid catalog name/
  );
});

test("rejects catalog names that fail the allow-list", async () => {
  const unsafeNames = ["", "default.json", "foo bar", "/etc/passwd"];
  for (const unsafeName of unsafeNames) {
    await assert.rejects(
      () => loadCatalog(unsafeName),
      /Invalid catalog name/,
      `Expected loadCatalog(${JSON.stringify(unsafeName)}) to be rejected`
    );
  }
});

test("loads a valid catalog by a safe allow-list name", async () => {
  const catalog = await loadCatalog("default");
  assert.equal(catalog.currency, "USD");
  assert.ok(Array.isArray(catalog.items));
  assert.ok(catalog.items.some((item) => item.sku === "WIDGET"));
});
