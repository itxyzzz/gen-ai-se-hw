package edu.setu.transactionpipeline;

import com.fasterxml.jackson.databind.JsonNode;
import edu.setu.transactionpipeline.cli.PipelineOptions;
import edu.setu.transactionpipeline.cli.ValidateTransactionsCommand;
import edu.setu.transactionpipeline.io.JsonCodec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.w3c.dom.Document;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilderFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ThemisQualityTest {
    @TempDir
    Path tempDir;

    @Test
    void validationOnlyReportsSafeCountsWithoutMutatingSharedProtocolFolders() throws Exception {
        Path shared = tempDir.resolve("shared");
        Path archive = tempDir.resolve("archive");

        var report = new ValidateTransactionsCommand().validateOnly(Path.of("sample-transactions.json"));

        assertEquals(8, report.total());
        assertEquals(6, report.valid());
        assertEquals(2, report.invalid());
        assertTrue(report.invalidTransactionIds().contains("TXN006"));
        assertTrue(report.invalidTransactionIds().contains("TXN007"));
        assertFalse(Files.exists(shared));
        assertFalse(Files.exists(archive));
        assertFalse(report.toString().contains("ACC-" + "1001"));
        assertFalse(report.toString().contains("Monthly " + "rent payment"));
    }

    @Test
    void fullRunResultsAndArchiveEvidenceRemainReviewSafe() throws Exception {
        PipelineOptions options = new PipelineOptions(
                Path.of("sample-transactions.json"),
                tempDir.resolve("shared"),
                tempDir.resolve("archive"),
                "20260621-180826-write-spec-java-hera-java-full-set",
                "docs/agent-runs/20260621-180826-write-spec-java-hera-java-full-set/agent-1-spec/outputs/specification.md",
                "2FC5B597915CFD59D5786271DB0CDE96B06417DEEC1433D056B76D7023234CFC",
                "20260621-183025-generate-code-java-hera-java-full-set",
                "docs/agent-runs/20260621-183025-generate-code-java-hera-java-full-set/agent-2-code/outputs/inventory.md",
                "248ba67b199104cbe06b4b0869cf3143de85ff1dd88da7bf4a13bc85a58ed883");

        assertEquals(0, new Integrator().run(options));
        assertEquals(0, new Integrator().run(options));

        JsonCodec codec = new JsonCodec();
        JsonNode summary = codec.readTree(tempDir.resolve("shared/results/summary.json"));
        JsonNode status = codec.readTree(tempDir.resolve("shared/results/pipeline-status.json"));
        assertEquals(8, summary.path("total").asInt());
        assertTrue(summary.path("complete").asBoolean());
        assertTrue(status.path("ready").asBoolean());
        assertTrue(Files.exists(tempDir.resolve("archive/shared-001/run-provenance.json")));

        String safeEvidence;
        try (Stream<Path> files = Files.walk(tempDir)) {
            safeEvidence = files
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".json"))
                    .filter(path -> !path.getFileName().toString().contains("transaction-validator"))
                    .map(path -> {
                        try {
                            return Files.readString(path);
                        } catch (Exception ex) {
                            throw new IllegalStateException(ex);
                        }
                    })
                    .reduce("", String::concat);
        }
        assertFalse(safeEvidence.contains("ACC-" + "1001"));
        assertFalse(safeEvidence.contains("Monthly " + "rent payment"));
        assertFalse(safeEvidence.contains("source_account"));
        assertFalse(safeEvidence.contains("description"));
    }

    @Test
    void jacocoCoverageGateIsConfiguredForThemisEightyPercentFloor() throws Exception {
        Document document = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder()
                .parse(Path.of("pom.xml").toFile());
        String pomText = document.getDocumentElement().getTextContent();

        assertTrue(pomText.contains("jacoco-maven-plugin"));
        assertTrue(pomText.contains("COVEREDRATIO"));
        assertTrue(pomText.contains("0.80"));
    }
}
