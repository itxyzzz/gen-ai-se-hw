package edu.setu.transactionpipeline;

import com.fasterxml.jackson.databind.JsonNode;
import edu.setu.transactionpipeline.cli.PipelineOptions;
import edu.setu.transactionpipeline.io.JsonCodec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IntegrationPipelineTest {
    @TempDir
    Path tempDir;

    @Test
    void fullPipelineProcessesSamplesAndArchivesSecondRun() throws Exception {
        Path shared = tempDir.resolve("shared");
        Path archive = tempDir.resolve("archive");
        PipelineOptions options = new PipelineOptions(
                Path.of("sample-transactions.json"),
                shared,
                archive,
                "20260621-180826-write-spec-java-hera-java-full-set",
                "source-specification.md",
                "2FC5B597915CFD59D5786271DB0CDE96B06417DEEC1433D056B76D7023234CFC",
                "20260621-183025-generate-code-java-hera-java-full-set",
                "inventory.md",
                "test-fingerprint");

        assertEquals(0, new Integrator().run(options));
        assertEquals(0, new Integrator().run(options));
        assertTrue(Files.exists(archive.resolve("shared-001").resolve("results").resolve("summary.json")));

        JsonCodec codec = new JsonCodec();
        JsonNode summary = codec.readTree(shared.resolve("results").resolve("summary.json"));
        assertEquals(8, summary.path("total").asInt());
        assertEquals(2, summary.path("settled").asInt());
        assertEquals(2, summary.path("rejected").asInt());
        assertEquals(4, summary.path("review_required").asInt());
        assertEquals(0, summary.path("error").asInt());

        JsonNode txn006 = codec.readTree(shared.resolve("results").resolve("TXN006.json"));
        assertEquals("rejected", txn006.path("status").asText());
        assertTrue(txn006.path("reason_codes").toString().contains("UNSUPPORTED_CURRENCY"));
        JsonNode txn007 = codec.readTree(shared.resolve("results").resolve("TXN007.json"));
        assertTrue(txn007.path("reason_codes").toString().contains("NON_POSITIVE_AMOUNT"));
        JsonNode txn004 = codec.readTree(shared.resolve("results").resolve("TXN004.json"));
        assertEquals("review_required", txn004.path("status").asText());
        assertTrue(txn004.path("reason_codes").toString().contains("ODD_HOUR"));
        JsonNode txn001 = codec.readTree(shared.resolve("results").resolve("TXN001.json"));
        assertEquals("settled", txn001.path("status").asText());
        assertTrue(txn001.has("settlement_reference"));

        String allResults = Files.readString(shared.resolve("results").resolve("summary.json"))
                + Files.readString(shared.resolve("run-provenance.json"));
        assertFalse(allResults.contains("ACC-" + "1001"));
        assertFalse(allResults.contains("Monthly " + "rent payment"));
    }
}
