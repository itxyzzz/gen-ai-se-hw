import assert from "node:assert/strict";
import test from "node:test";
import { applyDiscount, calculateQuote } from "../src/quoteCalculator.js";
import { findCatalogItem, loadCatalog } from "../src/catalogRepository.js";

test("generated regression: quote totals use multiplication before discount", () => {
  const quote = calculateQuote(
    [
      { sku: "WIDGET", quantity: 2, unitPrice: 12.5 },
      { sku: "GADGET", quantity: 4, unitPrice: 3 }
    ],
    { discountCode: "SAVE10" }
  );

  assert.deepEqual(
    quote.lines.map((line) => line.lineTotal),
    [25, 12]
  );
  assert.equal(quote.subtotal, 37);
  assert.equal(quote.total, 33.3);
});

test("generated regression: SAVE10 scales with subtotal", () => {
  assert.equal(applyDiscount(250, "SAVE10"), 225);
  assert.equal(applyDiscount(19.99, "SAVE10"), 17.991);
});

test("generated regression: default catalog still loads valid items", async () => {
  const catalog = await loadCatalog("default");
  const item = findCatalogItem(catalog, "WIDGET");

  assert.equal(item.sku, "WIDGET");
  assert.equal(typeof item.unitPrice, "number");
});

test("generated regression: catalog names must be simple identifiers", async () => {
  await assert.rejects(() => loadCatalog("../catalogs/default"), /Invalid catalog name/);
  await assert.rejects(() => loadCatalog("nested/default"), /Invalid catalog name/);
  await assert.rejects(() => loadCatalog("default.json"), /Invalid catalog name/);
});
