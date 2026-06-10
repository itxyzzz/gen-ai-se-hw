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

test("sums multiplied line totals across quote items", () => {
  const quote = calculateQuote([
    { sku: "WIDGET", quantity: 2, unitPrice: 12.5 },
    { sku: "SERVICE", quantity: 4, unitPrice: 7.25 }
  ]);

  assert.deepEqual(
    quote.lines.map((line) => line.lineTotal),
    [25, 29]
  );
  assert.equal(quote.subtotal, 54);
});

test("applies SAVE10 as ten percent discount", () => {
  assert.equal(applyDiscount(100, "SAVE10"), 90);
  assert.equal(applyDiscount(75, "SAVE10"), 67.5);
});

test("rounds SAVE10 percentage discounts to currency precision", () => {
  assert.equal(applyDiscount(19.99, "SAVE10"), 17.99);
});
