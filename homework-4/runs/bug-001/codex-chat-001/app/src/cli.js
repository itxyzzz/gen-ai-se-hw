#!/usr/bin/env node
import { calculateQuote } from "./quoteCalculator.js";
import { findCatalogItem, loadCatalog } from "./catalogRepository.js";

async function main() {
  const args = parseArgs(process.argv.slice(2));
  const catalog = await loadCatalog(args.catalog);
  const items = args.items.map(({ sku, quantity }) => {
    const catalogItem = findCatalogItem(catalog, sku);
    return {
      sku,
      quantity,
      unitPrice: catalogItem.unitPrice
    };
  });

  const quote = calculateQuote(items, {
    discountCode: args.discount,
    currency: catalog.currency
  });

  process.stdout.write(`${JSON.stringify(quote, null, 2)}\n`);
}

function parseArgs(argv) {
  const parsed = {
    catalog: "default",
    discount: undefined,
    items: []
  };

  for (let index = 0; index < argv.length; index += 1) {
    const current = argv[index];
    const next = argv[index + 1];

    if (current === "--catalog") {
      parsed.catalog = requireValue(current, next);
      index += 1;
    } else if (current === "--discount") {
      parsed.discount = requireValue(current, next);
      index += 1;
    } else if (current === "--item") {
      parsed.items.push(parseItem(requireValue(current, next)));
      index += 1;
    } else {
      throw new Error(`Unknown argument: ${current}`);
    }
  }

  if (parsed.items.length === 0) {
    throw new Error("At least one --item SKU:quantity argument is required.");
  }

  return parsed;
}

function parseItem(rawItem) {
  const [sku, quantityText] = rawItem.split(":");
  const quantity = Number.parseInt(quantityText, 10);

  if (!sku || !Number.isInteger(quantity)) {
    throw new Error(`Invalid item argument: ${rawItem}`);
  }

  return { sku, quantity };
}

function requireValue(flag, value) {
  if (!value || value.startsWith("--")) {
    throw new Error(`${flag} requires a value.`);
  }
  return value;
}

main().catch((error) => {
  process.stderr.write(`${error.message}\n`);
  process.exitCode = 1;
});
