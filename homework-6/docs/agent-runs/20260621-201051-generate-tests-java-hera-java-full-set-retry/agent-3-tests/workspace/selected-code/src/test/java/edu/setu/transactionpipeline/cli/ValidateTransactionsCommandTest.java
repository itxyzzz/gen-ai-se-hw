package edu.setu.transactionpipeline.cli;

import edu.setu.transactionpipeline.model.ReasonCode;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidateTransactionsCommandTest {
    @Test
    void reportsOnlyCountsAndInvalidTransactionIds() throws Exception {
        var report = new ValidateTransactionsCommand().validateOnly(Path.of("sample-transactions.json"));
        assertEquals(8, report.total());
        assertEquals(6, report.valid());
        assertEquals(2, report.invalid());
        assertTrue(report.invalidTransactionIds().contains("TXN006"));
        assertTrue(report.invalidTransactionIds().contains("TXN007"));
        assertEquals(1, report.reasonCodeCounts().get(ReasonCode.UNSUPPORTED_CURRENCY));
        assertEquals(1, report.reasonCodeCounts().get(ReasonCode.NON_POSITIVE_AMOUNT));
    }
}
