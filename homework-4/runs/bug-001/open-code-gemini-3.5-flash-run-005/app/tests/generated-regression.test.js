import assert from "node:assert/strict";
import test from "node:test";
import { calculateLineTotal, applyDiscount } from "../src/quoteCalculator.js";
import { loadCatalog } from "../src/catalogRepository.js";

test("calculateLineTotal calculates correct line total with various quantities and decimal prices", () => {
  // Case 1: Integer quantity, integer price
  assert.equal(calculateLineTotal({ quantity: 3, unitPrice: 25 }), 75);

  // Case 2: Integer quantity, decimal price
  assert.equal(calculateLineTotal({ quantity: 3, unitPrice: 1.5 }), 4.5);

  // Case 3: Quantity of 1, decimal price
  assert.equal(calculateLineTotal({ quantity: 1, unitPrice: 0.99 }), 0.99);

  // Case 4: Larger quantity, decimal price
  assert.equal(calculateLineTotal({ quantity: 10, unitPrice: 12.34 }), 123.4);
});

test("applyDiscount calculates SAVE10 with decimal subtotal rounding", () => {
  // Case 1: Simple subtotal
  assert.equal(applyDiscount(100, "SAVE10"), 90);

  // Case 2: Subtotal that results in a decimal requiring rounding (.995 -> .00)
  assert.equal(applyDiscount(75.55, "SAVE10"), 68);

  // Case 3: Subtotal that results in a decimal requiring rounding (.135 -> .14)
  assert.equal(applyDiscount(10.15, "SAVE10"), 9.14);
});

test("applyDiscount throws an error for invalid discount codes", () => {
  assert.throws(
    () => applyDiscount(100, "INVALID_CODE"),
    /Unknown discount code: INVALID_CODE/
  );
  assert.throws(
    () => applyDiscount(50, "SPAM"),
    /Unknown discount code: SPAM/
  );
});

test("loadCatalog throws an error if catalogName is not alphanumeric (with hyphens and underscores)", async () => {
  const invalidNames = [
    "invalid.json",
    "sub/directory",
    "invalid!",
    "with spaces",
    ""
  ];

  for (const name of invalidNames) {
    await assert.rejects(
      () => loadCatalog(name),
      /Invalid catalog name: must contain only alphanumeric characters, hyphens, and underscores/
    );
  }
});

test("loadCatalog prevents path traversal escaping catalog directory", async () => {
  // Test that attempting path traversal throws an error
  await assert.rejects(
    () => loadCatalog("../default"),
    /Invalid catalog name/
  );

  await assert.rejects(
    () => loadCatalog("../../etc/passwd"),
    /Invalid catalog name/
  );
});

test("loadCatalog successfully loads a valid catalog", async () => {
  const catalog = await loadCatalog("default");
  assert.equal(catalog.currency, "USD");
  assert.ok(Array.isArray(catalog.items));
  assert.equal(catalog.items.length, 2);
  assert.equal(catalog.items[0].sku, "WIDGET");
  assert.equal(catalog.items[0].unitPrice, 25);
  assert.equal(catalog.items[1].sku, "GADGET");
  assert.equal(catalog.items[1].unitPrice, 12.5);
});
