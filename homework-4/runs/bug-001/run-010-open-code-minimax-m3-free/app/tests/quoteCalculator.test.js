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

test("sums line totals across multiple items into the subtotal", () => {
  const quote = calculateQuote([
    { sku: "WIDGET", quantity: 2, unitPrice: 25 },
    { sku: "GADGET", quantity: 4, unitPrice: 12.5 }
  ]);

  assert.equal(quote.lines.length, 2);
  assert.equal(quote.lines[0].lineTotal, 50);
  assert.equal(quote.lines[1].lineTotal, 50);
  assert.equal(quote.subtotal, 100);
  assert.equal(quote.total, 100);
});

test("rounds SAVE10 discount to two decimal places", () => {
  assert.equal(applyDiscount(19.99, "SAVE10"), 17.99);
});
