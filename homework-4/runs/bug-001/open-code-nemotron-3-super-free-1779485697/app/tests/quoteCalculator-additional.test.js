import assert from "node:assert/strict";
import test from "node:test";
import { calculateLineTotal, applyDiscount, calculateQuote } from "../src/quoteCalculator.js";

test("calculateLineTotal throws error for non-positive quantity", () => {
  assert.throws(
    () => calculateLineTotal({ sku: "TEST", quantity: 0, unitPrice: 10 }),
    /Quantity must be a positive integer/
  );
  
  assert.throws(
    () => calculateLineTotal({ sku: "TEST", quantity: -1, unitPrice: 10 }),
    /Quantity must be a positive integer/
  );
  
  assert.throws(
    () => calculateLineTotal({ sku: "TEST", quantity: 1.5, unitPrice: 10 }),
    /Quantity must be a positive integer/
  );
});

test("calculateLineTotal throws error for negative unit price", () => {
  assert.throws(
    () => calculateLineTotal({ sku: "TEST", quantity: 2, unitPrice: -5 }),
    /Unit price must be a non-negative number/
  );
  
  assert.throws(
    () => calculateLineTotal({ sku: "TEST", quantity: 2, unitPrice: NaN }),
    /Unit price must be a non-negative number/
  );
});

test("applyDiscount throws error for unknown discount code", () => {
  assert.throws(
    () => applyDiscount(100, "INVALID"),
    /Unknown discount code: INVALID/
  );
});

test("applyDiscount returns unchanged subtotal for undefined discount code", () => {
  assert.equal(applyDiscount(100, undefined), 100);
  assert.equal(applyDiscount(100, null), 100);
  assert.equal(applyDiscount(100, ""), 100);
});

test("applyDiscount correctly rounds currency values", () => {
  // Test case that would produce repeating decimal
  assert.equal(applyDiscount(1, "SAVE10"), 0.9); // 1 * 0.9 = 0.9
  assert.equal(applyDiscount(10, "SAVE10"), 9); // 10 * 0.9 = 9
  assert.equal(applyDiscount(15, "SAVE10"), 13.5); // 15 * 0.9 = 13.5
});

// Test calculateQuote with various inputs
test("calculateQuote handles single item correctly (no discount)", () => {
  const quote = calculateQuote([
    { sku: "WIDGET", quantity: 3, unitPrice: 25 }
  ]);
  
  assert.equal(quote.lines[0].lineTotal, 75);
  assert.equal(quote.subtotal, 75);
  assert.equal(quote.total, 75); // No discount applied
});

test("calculateQuote handles single item with SAVE10 discount", () => {
  const quote = calculateQuote([
    { sku: "WIDGET", quantity: 3, unitPrice: 25 }
  ], { discountCode: "SAVE10" });
  
  assert.equal(quote.lines[0].lineTotal, 75);
  assert.equal(quote.subtotal, 75);
  assert.equal(quote.total, 67.5); // 75 * 0.9 = 67.5
});

test("calculateQuote handles multiple items", () => {
  const quote = calculateQuote([
    { sku: "WIDGET", quantity: 2, unitPrice: 10 },
    { sku: "GADGET", quantity: 1, unitPrice: 20 }
  ], { discountCode: "SAVE10" });
  
  assert.equal(quote.lines[0].lineTotal, 20); // 2 * 10
  assert.equal(quote.lines[1].lineTotal, 20); // 1 * 20
  assert.equal(quote.subtotal, 40); // 20 + 20
  assert.equal(quote.total, 36); // 40 * 0.9
});

test("calculateQuote works without discount", () => {
  const quote = calculateQuote([
    { sku: "WIDGET", quantity: 3, unitPrice: 25 }
  ], {});
  
  assert.equal(quote.lines[0].lineTotal, 75);
  assert.equal(quote.subtotal, 75);
  assert.equal(quote.total, 75); // No discount applied
});

test("calculateQuote throws error for empty items array", () => {
  assert.throws(
    () => calculateQuote([]),
    /At least one quote item is required/
  );
});

test("calculateQuote throws error for null items", () => {
  assert.throws(
    () => calculateQuote(null),
    /At least one quote item is required/
  );
});

