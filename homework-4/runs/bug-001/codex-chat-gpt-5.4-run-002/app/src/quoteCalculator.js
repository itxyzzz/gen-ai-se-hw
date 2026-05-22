export function calculateLineTotal(item) {
  assertPositiveQuantity(item.quantity);
  assertNonNegativePrice(item.unitPrice);
  return item.quantity * item.unitPrice;
}

export function applyDiscount(subtotal, discountCode) {
  if (!discountCode) {
    return subtotal;
  }

  if (discountCode === "SAVE10") {
    return subtotal * 0.9;
  }

  throw new Error(`Unknown discount code: ${discountCode}`);
}

export function calculateQuote(items, options = {}) {
  if (!Array.isArray(items) || items.length === 0) {
    throw new Error("At least one quote item is required.");
  }

  const lines = items.map((item) => ({
    sku: item.sku,
    quantity: item.quantity,
    unitPrice: item.unitPrice,
    lineTotal: calculateLineTotal(item)
  }));

  const subtotal = roundCurrency(lines.reduce((sum, line) => sum + line.lineTotal, 0));
  const total = roundCurrency(applyDiscount(subtotal, options.discountCode));

  return {
    currency: options.currency ?? "USD",
    lines,
    subtotal,
    total
  };
}

function assertPositiveQuantity(quantity) {
  if (!Number.isInteger(quantity) || quantity <= 0) {
    throw new Error("Quantity must be a positive integer.");
  }
}

function assertNonNegativePrice(price) {
  if (typeof price !== "number" || Number.isNaN(price) || price < 0) {
    throw new Error("Unit price must be a non-negative number.");
  }
}

function roundCurrency(value) {
  return Math.round(value * 100) / 100;
}
