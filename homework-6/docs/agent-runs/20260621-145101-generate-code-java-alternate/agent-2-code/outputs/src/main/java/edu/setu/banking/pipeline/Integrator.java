package edu.setu.banking.pipeline;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.setu.banking.pipeline.agent.FraudDetector;
import edu.setu.banking.pipeline.agent.ReportingAgent;
import edu.setu.banking.pipeline.agent.SettlementProcessor;
import edu.setu.banking.pipeline.agent.TransactionValidator;
import edu.setu.banking.pipeline.model.ProcessingResult;
import edu.setu.banking.pipeline.model.Summary;
import edu.setu.banking.pipeline.model.TransactionRecord;
import edu.setu.banking.pipeline.util.DirectorySupport;
import edu.setu.banking.pipeline.util.JsonSupport;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Integrator {
  private final TransactionValidator validator = new TransactionValidator();
  private final FraudDetector fraudDetector = new FraudDetector();
  private final SettlementProcessor settlementProcessor = new SettlementProcessor();
  private final ReportingAgent reportingAgent = new ReportingAgent();

  public static void main(String[] args) throws Exception {
    Options options = Options.parse(args);
    Integrator integrator = new Integrator();
    if (options.dryRun) {
      integrator.validateOnly(options.input);
    } else {
      Summary summary = integrator.run(options.input, options.sharedDir);
      System.out.printf(
          "Processed %d transactions: settled=%d rejected=%d review_required=%d error=%d%n",
          summary.total_transactions, summary.settled, summary.rejected, summary.review_required, summary.error);
    }
  }

  public Summary run(Path input, Path sharedDir) throws IOException {
    DirectorySupport.prepareShared(sharedDir);
    writeProvenance(sharedDir);
    List<TransactionRecord> records = loadTransactions(input);
    List<ProcessingResult> results = new ArrayList<>();
    for (TransactionRecord record : records) {
      writeStage(sharedDir.resolve("input").resolve(record.transactionId + ".json"), safeInputStage(record));
      ProcessingResult result = validator.process(record);
      writeStage(sharedDir.resolve("processing").resolve(record.transactionId + "-validated.json"), result);
      result = fraudDetector.process(record, result);
      writeStage(sharedDir.resolve("output").resolve(record.transactionId + "-risk.json"), result);
      result = settlementProcessor.process(result);
      JsonSupport.write(sharedDir.resolve("results").resolve(record.transactionId + ".json"), safeResult(result));
      results.add(result);
    }
    Summary summary = reportingAgent.summarize(results);
    JsonSupport.write(sharedDir.resolve("results").resolve("summary.json"), summary);
    return summary;
  }

  public Map<String, Object> validateOnly(Path input) throws IOException {
    List<TransactionRecord> records = loadTransactions(input);
    List<Map<String, Object>> safeResults = new ArrayList<>();
    int valid = 0;
    int invalid = 0;
    for (TransactionRecord record : records) {
      ProcessingResult result = validator.process(record);
      if ("validated".equals(result.status)) {
        valid++;
      } else {
        invalid++;
      }
      safeResults.add(
          Map.of(
              "transaction_id", result.transaction_id,
              "status", "validated".equals(result.status) ? "valid" : "invalid",
              "reason_codes", result.reason_codes));
    }
    Map<String, Object> response = new LinkedHashMap<>();
    response.put("total", records.size());
    response.put("valid", valid);
    response.put("invalid", invalid);
    response.put("results", safeResults);
    System.out.println(JsonSupport.MAPPER.writeValueAsString(response));
    return response;
  }

  public static List<TransactionRecord> loadTransactions(Path input) throws IOException {
    return JsonSupport.MAPPER.readValue(input.toFile(), new TypeReference<List<TransactionRecord>>() {});
  }

  private static void writeStage(Path path, Object value) throws IOException {
    JsonSupport.write(path, value);
  }

  private static Map<String, Object> safeResult(ProcessingResult result) {
    Map<String, Object> safe = new LinkedHashMap<>();
    safe.put("transaction_id", result.transaction_id);
    safe.put("amount", result.amount);
    safe.put("currency", result.currency);
    safe.put("status", result.status);
    safe.put("reason_codes", result.reason_codes);
    safe.put("settlement_reference", result.settlement_reference);
    safe.put("component_history_count", result.component_history.size());
    safe.put("audit_event_count", result.audit_events.size());
    return safe;
  }

  private static Map<String, Object> safeInputStage(TransactionRecord record) {
    Map<String, Object> safe = new LinkedHashMap<>();
    safe.put("transaction_id", record.transactionId);
    safe.put("amount", record.amount);
    safe.put("currency", record.currency);
    safe.put("transaction_type", record.transactionType);
    return safe;
  }

  private static void writeProvenance(Path sharedDir) throws IOException {
    Map<String, String> provenance = new LinkedHashMap<>();
    provenance.put("source_athena_run_id", "20260621-145100-write-spec-java-alternate");
    provenance.put("source_spec_path", "agent-1-spec/outputs/specification.md");
    provenance.put("hephaestus_run_id", "20260621-145101-generate-code-java-alternate");
    JsonSupport.write(sharedDir.resolve("run-provenance.json"), provenance);
  }

  private record Options(Path input, Path sharedDir, boolean dryRun) {
    static Options parse(String[] args) {
      Path input = Path.of("sample-transactions.json");
      Path sharedDir = Path.of("shared");
      boolean dryRun = false;
      for (int i = 0; i < args.length; i++) {
        switch (args[i]) {
          case "--input" -> input = Path.of(args[++i]);
          case "--shared-dir" -> sharedDir = Path.of(args[++i]);
          case "--dry-run" -> dryRun = true;
          default -> throw new IllegalArgumentException("Unsupported argument: " + args[i]);
        }
      }
      if (!Files.exists(input)) {
        throw new IllegalArgumentException("Input file not found: " + input);
      }
      return new Options(input, sharedDir, dryRun);
    }
  }
}
