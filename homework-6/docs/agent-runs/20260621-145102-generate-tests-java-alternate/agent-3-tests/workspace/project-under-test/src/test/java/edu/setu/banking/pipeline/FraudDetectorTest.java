package edu.setu.banking.pipeline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.setu.banking.pipeline.agent.FraudDetector;
import edu.setu.banking.pipeline.agent.TransactionValidator;
import edu.setu.banking.pipeline.model.ProcessingResult;
import edu.setu.banking.pipeline.model.TransactionRecord;
import java.util.Map;
import org.junit.jupiter.api.Test;

class FraudDetectorTest {
  private final TransactionValidator validator = new TransactionValidator();
  private final FraudDetector fraudDetector = new FraudDetector();

  @Test
  void flagsHighValueAndOddHourSignals() {
    TransactionRecord record = TransactionValidatorTest.validRecord();
    record.amount = "25000.00";
    record.timestamp = "2026-03-16T02:47:00Z";

    ProcessingResult result = fraudDetector.process(record, validator.process(record));

    assertEquals("review_required", result.status);
    assertTrue(result.reason_codes.contains("REVIEW_HIGH_VALUE"));
    assertTrue(result.reason_codes.contains("REVIEW_ODD_HOUR"));
  }

  @Test
  void flagsCrossBorderSignal() {
    TransactionRecord record = TransactionValidatorTest.validRecord();
    record.metadata = Map.of("country", "DE");

    ProcessingResult result = fraudDetector.process(record, validator.process(record));

    assertEquals("review_required", result.status);
    assertTrue(result.reason_codes.contains("REVIEW_CROSS_BORDER"));
  }
}
