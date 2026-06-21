package edu.setu.transactionpipeline.io;

import com.fasterxml.jackson.databind.JsonNode;
import edu.setu.transactionpipeline.model.TransactionRecord;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public final class InputTransactionLoader {
    private final JsonCodec codec;

    public InputTransactionLoader(JsonCodec codec) {
        this.codec = codec;
    }

    public List<TransactionRecord> load(Path samplePath) throws IOException {
        JsonNode root = codec.readTree(samplePath);
        if (!root.isArray()) {
            throw new IllegalArgumentException("Sample input must be a JSON array.");
        }
        List<TransactionRecord> records = new ArrayList<>();
        for (JsonNode node : root) {
            JsonNode metadata = node.path("metadata");
            records.add(new TransactionRecord(
                    text(node, "transaction_id"),
                    text(node, "timestamp"),
                    text(node, "amount"),
                    text(node, "currency"),
                    text(node, "transaction_type"),
                    text(metadata, "channel"),
                    text(metadata, "country"),
                    hasNonBlank(node, "source_account"),
                    hasNonBlank(node, "destination_account")));
        }
        return records;
    }

    private static String text(JsonNode node, String fieldName) {
        JsonNode value = node.get(fieldName);
        if (value == null || value.isNull()) {
            return null;
        }
        return value.asText();
    }

    private static boolean hasNonBlank(JsonNode node, String fieldName) {
        String value = text(node, fieldName);
        return value != null && !value.isBlank();
    }
}
