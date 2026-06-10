import assert from "node:assert/strict";
import test from "node:test";
import { loadCatalog } from "../src/catalogRepository.js";

test("rejects catalog names that escape the catalog directory", async () => {
  await assert.rejects(
    () => loadCatalog("../catalogs/default"),
    /Invalid catalog name/
  );
});

test("rejects catalog names with multiple parent directory references", async () => {
  await assert.rejects(
    () => loadCatalog("../../package.json"),
    /Invalid catalog name/
  );
});

test("rejects catalog names with path traversal to system files", async () => {
  await assert.rejects(
    () => loadCatalog("../../../etc/passwd"),
    /Invalid catalog name/
  );
});

test("rejects catalog names with current and parent directory references", async () => {
  await assert.rejects(
    () => loadCatalog("./../../secret"),
    /Invalid catalog name/
  );
});

test("rejects catalog names with special characters", async () => {
  await assert.rejects(
    () => loadCatalog("catalog/../admin"),
    /Invalid catalog name/
  );
});

test("rejects catalog names with slashes", async () => {
  await assert.rejects(
    () => loadCatalog("../admin/config"),
    /Invalid catalog name/
  );
});

test("accepts valid catalog names with alphanumeric characters", async () => {
  // This will fail if the catalog doesn't exist, but validates the security check passes
  await assert.rejects(
    () => loadCatalog("catalog2024"),
    (err) => {
      // Should fail with file not found, not validation error
      return !err.message.includes("Invalid catalog name");
    }
  );
});

test("accepts valid catalog names with hyphens", async () => {
  await assert.rejects(
    () => loadCatalog("valid-catalog"),
    (err) => {
      return !err.message.includes("Invalid catalog name");
    }
  );
});

test("accepts valid catalog names with underscores", async () => {
  await assert.rejects(
    () => loadCatalog("valid_catalog"),
    (err) => {
      return !err.message.includes("Invalid catalog name");
    }
  );
});
