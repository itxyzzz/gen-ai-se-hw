package edu.setu.transactionpipeline;

import edu.setu.transactionpipeline.model.PipelineMessage;
import edu.setu.transactionpipeline.model.TransactionRecord;

import java.time.Instant;

public final class TestFixtures {
    private TestFixtures() {
    }

    public static TransactionRecord lowRisk() {
        return record("TXNLOW", "2026-03-16T09:00:00Z", "1500.00", "USD", "transfer", "online", "US");
    }

    public static TransactionRecord unsupportedCurrency() {
        return record("TXNBADCUR", "2026-03-16T10:05:00Z", "200.00", "XYZ", "transfer", "online", "US");
    }

    public static TransactionRecord nonPositiveAmount() {
        return record("TXNBADAMT", "2026-03-16T10:10:00Z", "-100.00", "GBP", "refund", "online", "GB");
    }

    public static TransactionRecord highValue() {
        return record("TXNHIGH", "2026-03-16T09:15:00Z", "25000.00", "USD", "wire_transfer", "branch", "US");
    }

    public static TransactionRecord oddHour() {
        return record("TXNODD", "2026-03-16T02:47:00Z", "500.00", "EUR", "transfer", "api", "DE");
    }

    public static TransactionRecord record(String id, String timestamp, String amount, String currency, String type, String channel, String country) {
        return new TransactionRecord(id, timestamp, amount, currency, type, channel, country, true, true);
    }

    public static PipelineMessage message(TransactionRecord record) {
        return PipelineMessage.initial(record, Instant.parse("2026-03-16T10:00:00Z"));
    }
}
