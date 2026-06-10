import assert from "node:assert/strict";
import test from "node:test";
import { calculateLineTotal, applyDiscount, calculateQuote } from "../src/quoteCalculator.js";
import { loadCatalog } from "../src/catalogRepository.js";

test("calculateLineTotal multiplies quantity by unit price", () => {
  assert.equal(calculateLineTotal({ quantity: 2, unitPrice: 12.5 }), 25);
  assert.equal(calculateLineTotal({ quantity: 1, unitPrice: 100 }), 100);
  assert.equal(calculateLineTotal({ quantity: 5, unitPrice: 1.99 }), 9.95);
});

test("calculateLineTotal rejects zero quantity", () => {
  assert.throws(() => calculateLineTotal({ quantity: 0, unitPrice: 10 }), /Quantity must be a positive integer/);
});

test("calculateLineTotal rejects negative quantity", () => {
  assert.throws(() => calculateLineTotal({ quantity: -1, unitPrice: 10 }), /Quantity must be a positive integer/);
});

test("calculateLineTotal rejects non-integer quantity", () => {
  assert.throws(() => calculateLineTotal({ quantity: 1.5, unitPrice: 10 }), /Quantity must be a positive integer/);
});

test("calculateLineTotal rejects negative price", () => {
  assert.throws(() => calculateLineTotal({ quantity: 1, unitPrice: -5 }), /Unit price must be a non-negative number/);
});

test("applyDiscount returns subtotal when no discount code", () => {
  assert.equal(applyDiscount(100, undefined), 100);
  assert.equal(applyDiscount(100, ""), 100);
  assert.equal(applyDiscount(100, null), 100);
});

test("applyDiscount throws for unknown discount code", () => {
  assert.throws(() => applyDiscount(100, "INVALID"), /Unknown discount code: INVALID/);
  assert.throws(() => applyDiscount(100, "HALF"), /Unknown discount code: HALF/);
});

test("calculateQuote with multiple items", () => {
  const quote = calculateQuote([
    { sku: "WIDGET", quantity: 2, unitPrice: 25 },
    { sku: "GADGET", quantity: 3, unitPrice: 12.5 }
  ]);
  assert.equal(quote.lines[0].lineTotal, 50);
  assert.equal(quote.lines[1].lineTotal, 37.5);
  assert.equal(quote.subtotal, 87.5);
});

test("calculateQuote with discount and multiple items", () => {
  const quote = calculateQuote([
    { sku: "WIDGET", quantity: 2, unitPrice: 25 }
  ], { discountCode: "SAVE10" });
  assert.equal(quote.subtotal, 50);
  assert.equal(quote.total, 45);
});

test("calculateQuote rejects empty items", () => {
  assert.throws(() => calculateQuote([]), /At least one quote item is required/);
});

test("loadCatalog rejects invalid catalog names", async () => {
  await assert.rejects(() => loadCatalog("../etc/passwd"), /Invalid catalog name/);
  await assert.rejects(() => loadCatalog("default/../../etc"), /Invalid catalog name/);
  await assert.rejects(() => loadCatalog(""), /Invalid catalog name/);
  await assert.rejects(() => loadCatalog("catalog with spaces"), /Invalid catalog name/);
  await assert.rejects(() => loadCatalog("a<b>c"), /Invalid catalog name/);
});

test("loadCatalog loads default catalog", async () => {
  const catalog = await loadCatalog("default");
  assert.equal(catalog.currency, "USD");
  assert.ok(Array.isArray(catalog.items));
  assert.equal(catalog.items.length, 2);
});
