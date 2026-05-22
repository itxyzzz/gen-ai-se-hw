import assert from "node:assert/strict";
import test from "node:test";
import { applyDiscount, calculateQuote } from "../src/quoteCalculator.js";

test("calculates line totals by multiplying quantity and unit price", () => {
  const quote = calculateQuote([
    { sku: "WIDGET", quantity: 3, unitPrice: 25 }
  ]);

  assert.equal(quote.lines[0].lineTotal, 75);
  assert.equal(quote.subtotal, 75);
});

test("applies SAVE10 as ten percent discount", () => {
  assert.equal(applyDiscount(100, "SAVE10"), 90);
  assert.equal(applyDiscount(75, "SAVE10"), 67.5);
});
