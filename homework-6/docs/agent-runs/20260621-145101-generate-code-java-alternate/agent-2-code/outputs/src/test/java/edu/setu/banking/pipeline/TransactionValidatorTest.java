package edu.setu.banking.pipeline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.setu.banking.pipeline.agent.TransactionValidator;
import edu.setu.banking.pipeline.model.ProcessingResult;
import edu.setu.banking.pipeline.model.TransactionRecord;
import org.junit.jupiter.api.Test;

class TransactionValidatorTest {
  private final TransactionValidator validator = new TransactionValidator();

  @Test
  void rejectsUnsupportedCurrency() {
    TransactionRecord record = validRecord();
    record.currency = "XYZ";

    ProcessingResult result = validator.process(record);

    assertEquals("rejected", result.status);
    assertTrue(result.reason_codes.contains("UNSUPPORTED_CURRENCY"));
  }

  @Test
  void rejectsNonPositiveAmount() {
    TransactionRecord record = validRecord();
    record.amount = "-100.00";

    ProcessingResult result = validator.process(record);

    assertEquals("rejected", result.status);
    assertTrue(result.reason_codes.contains("NON_POSITIVE_AMOUNT"));
  }

  @Test
  void validatesSupportedTransaction() {
    ProcessingResult result = validator.process(validRecord());

    assertEquals("validated", result.status);
    assertTrue(result.reason_codes.isEmpty());
  }

  static TransactionRecord validRecord() {
    TransactionRecord record = new TransactionRecord();
    record.transactionId = "TXN-TEST";
    record.timestamp = "2026-03-16T10:00:00Z";
    record.sourceAccount = "SRC";
    record.destinationAccount = "DST";
    record.amount = "100.00";
    record.currency = "USD";
    record.transactionType = "transfer";
    return record;
  }
}
