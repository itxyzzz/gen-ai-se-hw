package edu.setu.transactionpipeline.io;

import com.fasterxml.jackson.databind.JsonNode;
import edu.setu.transactionpipeline.agent.FraudDetector;
import edu.setu.transactionpipeline.agent.ReportingAgent;
import edu.setu.transactionpipeline.agent.SettlementProcessor;
import edu.setu.transactionpipeline.agent.TransactionValidator;
import edu.setu.transactionpipeline.privacy.PrivacyGuard;
import edu.setu.transactionpipeline.TestFixtures;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class JsonAndResultSchemaTest {
    @TempDir
    Path tempDir;

    @Test
    void writesStackNeutralResultJson() throws Exception {
        JsonCodec codec = new JsonCodec();
        ProtocolPaths paths = ProtocolPaths.under(tempDir.resolve("shared"));
        java.nio.file.Files.createDirectories(paths.results());
        var message = new SettlementProcessor().processMessage(
                new FraudDetector().processMessage(
                        new TransactionValidator().processMessage(TestFixtures.message(TestFixtures.lowRisk()))));
        ReportingAgent reporter = new ReportingAgent(new ResultWriter(codec, new PrivacyGuard()));
        reporter.writeResults(paths, List.of(message), 1);

        JsonNode result = codec.readTree(paths.results().resolve("TXNLOW.json"));
        new ResultSchema().validateTransactionResult(result);
        assertEquals("settled", result.path("status").asText());
        assertEquals("1500.00", result.path("amount").asText());
        new ResultSchema().validateSummary(codec.readTree(paths.results().resolve("summary.json")));
    }
}
