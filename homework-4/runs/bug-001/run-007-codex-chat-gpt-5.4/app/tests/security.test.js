import assert from "node:assert/strict";
import test from "node:test";
import { loadCatalog } from "../src/catalogRepository.js";

test("rejects catalog names that escape the catalog directory", async () => {
  await assert.rejects(
    () => loadCatalog("../catalogs/default"),
    /Invalid catalog name/
  );
});

test("rejects absolute catalog paths", async () => {
  await assert.rejects(
    () => loadCatalog("/tmp/default"),
    /Invalid catalog name/
  );
});

test("loads allowlisted catalog names", async () => {
  const catalog = await loadCatalog("default");

  assert.equal(catalog.items.length, 2);
});
