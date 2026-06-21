package edu.setu.banking.pipeline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import edu.setu.banking.pipeline.model.Summary;
import edu.setu.banking.pipeline.util.JsonSupport;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class IntegratorPipelineTest {
  @TempDir Path tempDir;

  @Test
  void processesAllSampleTransactionsAndArchivesRepeatedRun() throws Exception {
    Path input = Path.of("sample-transactions.json");
    Path shared = tempDir.resolve("shared");
    Integrator integrator = new Integrator();

    Summary first = integrator.run(input, shared);
    Summary second = integrator.run(input, shared);

    assertEquals(8, first.total_transactions);
    assertEquals(2, first.settled);
    assertEquals(2, first.rejected);
    assertEquals(4, first.review_required);
    assertEquals(8, second.total_transactions);
    assertTrue(Files.exists(tempDir.resolve("archive").resolve("shared-001")));
    assertTrue(Files.exists(shared.resolve("results").resolve("summary.json")));
    assertTrue(Files.exists(shared.resolve("results").resolve("TXN006.json")));
  }

  @Test
  void dryRunReturnsSafeValidationCounts() throws Exception {
    Object response = new Integrator().validateOnly(Path.of("sample-transactions.json"));
    String json = JsonSupport.MAPPER.writeValueAsString(response);

    assertTrue(json.contains("\"total\" : 8"));
    assertTrue(json.contains("\"invalid\" : 2"));
    assertFalse(json.contains("ACC-"));
    assertFalse(json.contains("Monthly"));
  }
}
