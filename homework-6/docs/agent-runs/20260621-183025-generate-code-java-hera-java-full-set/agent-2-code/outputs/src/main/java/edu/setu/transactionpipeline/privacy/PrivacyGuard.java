package edu.setu.transactionpipeline.privacy;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;
import java.util.regex.Pattern;

public final class PrivacyGuard {
    private static final Pattern RAW_ACCOUNT = Pattern.compile("ACC-[0-9]{4,}");
    private static final Pattern RAW_DESCRIPTION = Pattern.compile(
            String.join("|",
                    "Monthly " + "rent payment",
                    "Equipment " + "purchase",
                    "Consulting " + "payment",
                    "Invoice " + "#4471",
                    "Property " + "settlement",
                    "Test " + "payment",
                    "Refund for order " + "#8821",
                    "Salary " + "advance"),
            Pattern.CASE_INSENSITIVE);

    public void verifyPrivacySafe(JsonNode node) {
        inspect(node);
    }

    private void inspect(JsonNode node) {
        if (node == null || node.isNull()) {
            return;
        }
        if (node.isTextual()) {
            String value = node.asText();
            if (RAW_ACCOUNT.matcher(value).find() || RAW_DESCRIPTION.matcher(value).find()) {
                throw new IllegalArgumentException("Privacy guard rejected unsafe value.");
            }
        }
        if (node.isObject()) {
            Iterator<String> names = node.fieldNames();
            while (names.hasNext()) {
                String name = names.next();
                if (name.equals("source_account") || name.equals("destination_account") || name.equals("description")) {
                    throw new IllegalArgumentException("Privacy guard rejected unsafe field.");
                }
                inspect(node.get(name));
            }
        }
        if (node.isArray()) {
            for (JsonNode child : node) {
                inspect(child);
            }
        }
    }
}
