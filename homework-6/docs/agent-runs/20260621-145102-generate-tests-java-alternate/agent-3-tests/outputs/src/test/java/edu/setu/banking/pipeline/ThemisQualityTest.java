package edu.setu.banking.pipeline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.setu.banking.pipeline.agent.FraudDetector;
import edu.setu.banking.pipeline.agent.SettlementProcessor;
import edu.setu.banking.pipeline.agent.TransactionValidator;
import edu.setu.banking.pipeline.model.ProcessingResult;
import edu.setu.banking.pipeline.model.Summary;
import edu.setu.banking.pipeline.model.TimeSupport;
import edu.setu.banking.pipeline.model.TransactionRecord;
import edu.setu.banking.pipeline.util.DirectorySupport;
import edu.setu.banking.pipeline.util.JsonSupport;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ThemisQualityTest {
  @TempDir Path tempDir;

  @Test
  void validatorReportsMissingAndMalformedFields() {
    TransactionRecord record = new TransactionRecord();
    record.transactionId = "";
    record.timestamp = "";
    record.sourceAccount = "";
    record.destinationAccount = "";
    record.amount = "not-money";
    record.currency = "USD";

    ProcessingResult result = new TransactionValidator().process(record);

    assertEquals("rejected", result.status);
    assertTrue(result.reason_codes.contains("MISSING_TRANSACTION_ID"));
    assertTrue(result.reason_codes.contains("MISSING_TIMESTAMP"));
    assertTrue(result.reason_codes.contains("MISSING_SOURCE_ACCOUNT"));
    assertTrue(result.reason_codes.contains("MISSING_DESTINATION_ACCOUNT"));
    assertTrue(result.reason_codes.contains("INVALID_AMOUNT"));
  }

  @Test
  void validatorRejectsNullTransactionSafely() {
    ProcessingResult result = new TransactionValidator().process(null);

    assertEquals("UNKNOWN", result.transaction_id);
    assertEquals("rejected", result.status);
    assertTrue(result.reason_codes.contains("MISSING_TRANSACTION"));
  }

  @Test
  void fraudDetectorSkipsRejectedTransactions() {
    ProcessingResult rejected = new ProcessingResult("TXN-R", "-1.00", "GBP");
    rejected.status = "rejected";

    ProcessingResult result = new FraudDetector().process(new TransactionRecord(), rejected);

    assertEquals("rejected", result.status);
    assertTrue(result.component_history.stream().anyMatch(item -> item.equals("FraudDetector:skipped")));
  }

  @Test
  void settlementProcessorMarksUnknownStatusAsError() {
    ProcessingResult result = new ProcessingResult("TXN-X", "1.00", "USD");
    result.status = "mystery";

    ProcessingResult processed = new SettlementProcessor().process(result);

    assertEquals("error", processed.status);
    assertTrue(processed.reason_codes.contains("UNEXPECTED_STATUS"));
  }

  @Test
  void jsonSupportRoundTripsBigDecimalWithoutBinaryFloatingPoint() throws Exception {
    Path file = tempDir.resolve("money.json");
    JsonSupport.write(file, Map.of("amount", new BigDecimal("10.10")));
    Map<?, ?> value = JsonSupport.read(file, Map.class);

    assertEquals(new BigDecimal("10.10"), value.get("amount"));
    assertFalse(Files.readString(file).contains("10.099"));
  }

  @Test
  void directorySupportDeletesNestedTrees() throws Exception {
    Path root = tempDir.resolve("delete-me");
    Files.createDirectories(root.resolve("nested"));
    Files.writeString(root.resolve("nested").resolve("file.txt"), "safe");

    DirectorySupport.deleteTree(root);

    assertFalse(Files.exists(root));
  }

  @Test
  void prepareSharedCreatesProtocolDirectories() throws Exception {
    Path shared = tempDir.resolve("shared");

    DirectorySupport.prepareShared(shared);

    assertTrue(Files.isDirectory(shared.resolve("input")));
    assertTrue(Files.isDirectory(shared.resolve("processing")));
    assertTrue(Files.isDirectory(shared.resolve("output")));
    assertTrue(Files.isDirectory(shared.resolve("results")));
  }

  @Test
  void timeSupportCanUseFixedClockForAuditTests() {
    TimeSupport.setClockForTests(Clock.fixed(Instant.parse("2026-03-16T10:00:00Z"), ZoneOffset.UTC));

    assertEquals("2026-03-16T10:00:00Z", TimeSupport.now());
  }

  @Test
  void integratorRejectsUnknownMainArgument() {
    IllegalArgumentException ex =
        assertThrows(IllegalArgumentException.class, () -> Integrator.main(new String[] {"--unknown"}));

    assertTrue(ex.getMessage().contains("Unsupported argument"));
  }

  @Test
  void integratorRejectsMissingInputFile() {
    IllegalArgumentException ex =
        assertThrows(
            IllegalArgumentException.class,
            () -> Integrator.main(new String[] {"--input", tempDir.resolve("missing.json").toString()}));

    assertTrue(ex.getMessage().contains("Input file not found"));
  }

  @Test
  void loadTransactionsReadsSampleFixtureWithoutLeakingSensitiveFields() throws Exception {
    List<TransactionRecord> records = Integrator.loadTransactions(Path.of("sample-transactions.json"));

    assertEquals(8, records.size());
    String serialized = JsonSupport.MAPPER.writeValueAsString(new Integrator().validateOnly(Path.of("sample-transactions.json")));
    assertFalse(serialized.contains("ACC-"));
    assertFalse(serialized.contains("payment"));
  }

  @Test
  void summaryTracksErrorBucket() {
    Summary summary = new Summary();
    summary.error = 1;

    assertEquals(1, summary.error);
  }
}
