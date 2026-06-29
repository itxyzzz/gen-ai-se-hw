package edu.setu.transactionpipeline.io;

import edu.setu.transactionpipeline.model.PipelineStatusReport;
import edu.setu.transactionpipeline.model.PipelineSummary;
import edu.setu.transactionpipeline.model.TransactionResult;
import edu.setu.transactionpipeline.privacy.PrivacyGuard;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public final class ResultWriter {
    private final JsonCodec codec;
    private final PrivacyGuard privacyGuard;

    public ResultWriter(JsonCodec codec, PrivacyGuard privacyGuard) {
        this.codec = codec;
        this.privacyGuard = privacyGuard;
    }

    public void writeResults(
            Path resultsDir,
            List<TransactionResult> results,
            PipelineSummary summary,
            PipelineStatusReport statusReport) throws IOException {
        for (TransactionResult result : results) {
            Path target = resultsDir.resolve(result.transactionId() + ".json");
            codec.write(target, result);
            privacyGuard.verifyPrivacySafe(codec.readTree(target));
        }
        Path summaryPath = resultsDir.resolve("summary.json");
        codec.write(summaryPath, summary);
        privacyGuard.verifyPrivacySafe(codec.readTree(summaryPath));
        Path statusPath = resultsDir.resolve("pipeline-status.json");
        codec.write(statusPath, statusReport);
        privacyGuard.verifyPrivacySafe(codec.readTree(statusPath));
    }
}
