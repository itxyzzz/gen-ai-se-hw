package edu.setu.transactionpipeline.agent;

import edu.setu.transactionpipeline.io.ProtocolPaths;
import edu.setu.transactionpipeline.io.ResultWriter;
import edu.setu.transactionpipeline.model.PipelineMessage;
import edu.setu.transactionpipeline.model.PipelineStatusReport;
import edu.setu.transactionpipeline.model.PipelineSummary;
import edu.setu.transactionpipeline.model.ProcessingStatus;
import edu.setu.transactionpipeline.model.RiskTier;
import edu.setu.transactionpipeline.model.TransactionResult;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class ReportingAgent {
    private final ResultWriter resultWriter;

    public ReportingAgent(ResultWriter resultWriter) {
        this.resultWriter = resultWriter;
    }

    public TransactionResult toTransactionResult(PipelineMessage terminalMessage) {
        RiskTier tier = terminalMessage.fraudAssessment() == null ? RiskTier.LOW : terminalMessage.fraudAssessment().riskTier();
        String settlementReference = terminalMessage.status() == ProcessingStatus.SETTLED
                ? "SIM-" + terminalMessage.transaction().transactionId()
                : null;
        return new TransactionResult(
                "1.0",
                terminalMessage.transaction().transactionId(),
                terminalMessage.status(),
                terminalMessage.reasonCodes(),
                terminalMessage.transaction().amountText(),
                terminalMessage.transaction().currency(),
                Instant.now(),
                tier,
                terminalMessage.auditEvents().size() + 1,
                terminalMessage.auditEvents().size(),
                settlementReference);
    }

    public PipelineSummary summarize(List<TransactionResult> results, int expectedInputCount) {
        int settled = 0;
        int rejected = 0;
        int reviewRequired = 0;
        int error = 0;
        Map<String, Integer> reasonCounts = new LinkedHashMap<>();
        for (TransactionResult result : results) {
            if (result.status() == ProcessingStatus.SETTLED) {
                settled++;
            } else if (result.status() == ProcessingStatus.REJECTED) {
                rejected++;
            } else if (result.status() == ProcessingStatus.REVIEW_REQUIRED) {
                reviewRequired++;
            } else if (result.status() == ProcessingStatus.ERROR) {
                error++;
            }
            for (String reason : result.reasonCodes()) {
                reasonCounts.merge(reason, 1, Integer::sum);
            }
        }
        return new PipelineSummary(
                "1.0",
                Instant.now(),
                results.size(),
                settled,
                rejected,
                reviewRequired,
                error,
                results.size() == expectedInputCount,
                reasonCounts);
    }

    public PipelineStatusReport buildStatusReport(PipelineSummary summary, ProtocolPaths paths) {
        return new PipelineStatusReport(
                "1.0",
                Instant.now(),
                summary.complete(),
                paths.results().resolve("summary.json").toString(),
                paths.results().toString(),
                summary.total(),
                summary.settled(),
                summary.rejected(),
                summary.reviewRequired(),
                summary.error(),
                summary.complete());
    }

    public PipelineSummary writeResults(ProtocolPaths paths, List<PipelineMessage> terminalMessages, int expectedInputCount) throws IOException {
        List<TransactionResult> results = new ArrayList<>();
        for (PipelineMessage message : terminalMessages) {
            if (message.status() == null || !message.status().isTerminal()) {
                throw new IllegalArgumentException("Reporting requires terminal messages only.");
            }
            results.add(toTransactionResult(message));
        }
        PipelineSummary summary = summarize(results, expectedInputCount);
        resultWriter.writeResults(paths.results(), results, summary, buildStatusReport(summary, paths));
        return summary;
    }
}
