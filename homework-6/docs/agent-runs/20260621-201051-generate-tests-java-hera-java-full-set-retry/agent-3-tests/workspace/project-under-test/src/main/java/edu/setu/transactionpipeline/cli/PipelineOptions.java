package edu.setu.transactionpipeline.cli;

import java.nio.file.Path;

public record PipelineOptions(
        Path samplePath,
        Path sharedRoot,
        Path archiveRoot,
        String sourceSpecRunId,
        String sourceSpecPath,
        String sourceSpecFingerprint,
        String pipelineVersionId,
        String pipelineInventoryPath,
        String pipelinePackageFingerprint) {

    public static PipelineOptions fromArgs(String[] args) {
        Path sample = Path.of("sample-transactions.json");
        Path shared = Path.of("shared");
        for (int index = 0; index < args.length; index++) {
            if ("--input".equals(args[index]) && index + 1 < args.length) {
                sample = Path.of(args[++index]);
            } else if ("--shared-dir".equals(args[index]) && index + 1 < args.length) {
                shared = Path.of(args[++index]);
            }
        }
        Path archive = shared.toAbsolutePath().getParent() == null
                ? Path.of("archive")
                : shared.toAbsolutePath().getParent().resolve("archive");
        return new PipelineOptions(
                sample,
                shared,
                archive,
                "20260621-180826-write-spec-java-hera-java-full-set",
                "docs/agent-runs/20260621-180826-write-spec-java-hera-java-full-set/agent-1-spec/outputs/specification.md",
                "2FC5B597915CFD59D5786271DB0CDE96B06417DEEC1433D056B76D7023234CFC",
                "20260621-183025-generate-code-java-hera-java-full-set",
                "docs/agent-runs/20260621-183025-generate-code-java-hera-java-full-set/agent-2-code/outputs/inventory.md",
                "pending-inventory-fingerprint");
    }
}
