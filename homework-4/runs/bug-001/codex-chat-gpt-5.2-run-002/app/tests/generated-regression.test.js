import assert from "node:assert/strict";
import test from "node:test";
import { calculateQuote } from "../src/quoteCalculator.js";
import { loadCatalog } from "../src/catalogRepository.js";

test("generated regression: combines quantity totals and percentage discount", () => {
  const quote = calculateQuote([
    { sku: "WIDGET", quantity: 2, unitPrice: 25 },
    { sku: "GADGET", quantity: 4, unitPrice: 12.5 }
  ], { discountCode: "SAVE10" });

  assert.equal(quote.subtotal, 100);
  assert.equal(quote.total, 90);
});

test("generated regression: catalog path traversal remains blocked", async () => {
  await assert.rejects(() => loadCatalog("../catalogs/default"), /Invalid catalog name/);
});
