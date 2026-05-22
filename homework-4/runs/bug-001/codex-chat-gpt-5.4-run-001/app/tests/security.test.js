import assert from "node:assert/strict";
import test from "node:test";
import { loadCatalog } from "../src/catalogRepository.js";

test("rejects catalog names that escape the catalog directory", async () => {
  await assert.rejects(
    () => loadCatalog("../catalogs/default"),
    /Invalid catalog name/
  );
});
