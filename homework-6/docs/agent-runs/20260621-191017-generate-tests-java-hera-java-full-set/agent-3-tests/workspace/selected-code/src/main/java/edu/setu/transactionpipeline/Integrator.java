package edu.setu.transactionpipeline;

import edu.setu.transactionpipeline.agent.FraudDetector;
import edu.setu.transactionpipeline.agent.ReportingAgent;
import edu.setu.transactionpipeline.agent.SettlementProcessor;
import edu.setu.transactionpipeline.agent.TransactionValidator;
import edu.setu.transactionpipeline.cli.PipelineOptions;
import edu.setu.transactionpipeline.io.InputMessageWriter;
import edu.setu.transactionpipeline.io.InputTransactionLoader;
import edu.setu.transactionpipeline.io.JsonCodec;
import edu.setu.transactionpipeline.io.ProtocolFileMover;
import edu.setu.transactionpipeline.io.ProtocolPaths;
import edu.setu.transactionpipeline.io.ResultWriter;
import edu.setu.transactionpipeline.io.RunProvenanceWriter;
import edu.setu.transactionpipeline.io.SharedDirectoryManager;
import edu.setu.transactionpipeline.model.AuditEvent;
import edu.setu.transactionpipeline.model.PipelineMessage;
import edu.setu.transactionpipeline.model.PipelineSummary;
import edu.setu.transactionpipeline.model.ProcessingStatus;
import edu.setu.transactionpipeline.model.ReasonCode;
import edu.setu.transactionpipeline.model.TransactionRecord;
import edu.setu.transactionpipeline.privacy.PrivacyGuard;

import java.time.Clock;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class Integrator {
    public static void main(String[] args) throws Exception {
        int code = new Integrator().run(PipelineOptions.fromArgs(args));
        if (code != 0) {
            System.exit(code);
        }
    }

    public int run(PipelineOptions options) {
        JsonCodec codec = new JsonCodec();
        PrivacyGuard privacyGuard = new PrivacyGuard();
        try {
            ProtocolPaths paths = new SharedDirectoryManager().prepareFreshSharedTree(options.sharedRoot(), options.archiveRoot());
            new RunProvenanceWriter(codec, privacyGuard, Clock.systemUTC())
                    .write(paths.sharedRoot(), new RunProvenanceWriter(codec, privacyGuard, Clock.systemUTC()).fromOptions(options));

            List<TransactionRecord> records = new InputTransactionLoader(codec).load(options.samplePath());
            InputMessageWriter inputWriter = new InputMessageWriter(codec);
            ProtocolFileMover fileMover = new ProtocolFileMover(codec);
            TransactionValidator validator = new TransactionValidator();
            FraudDetector fraudDetector = new FraudDetector();
            SettlementProcessor settlementProcessor = new SettlementProcessor();
            ReportingAgent reportingAgent = new ReportingAgent(new ResultWriter(codec, privacyGuard));
            List<PipelineMessage> terminalMessages = new ArrayList<>();

            int sequence = 1;
            for (TransactionRecord record : records) {
                PipelineMessage initial = PipelineMessage.initial(record, Instant.now());
                inputWriter.writeInitialMessage(paths, initial, sequence);
                terminalMessages.add(processTransaction(initial, validator, fraudDetector, settlementProcessor, fileMover, paths, sequence));
                sequence++;
            }
            PipelineSummary summary = reportingAgent.writeResults(paths, terminalMessages, records.size());
            return summary.complete() ? 0 : 1;
        } catch (Exception ex) {
            return 1;
        }
    }

    PipelineMessage processTransaction(
            PipelineMessage initial,
            TransactionValidator validator,
            FraudDetector fraudDetector,
            SettlementProcessor settlementProcessor,
            ProtocolFileMover fileMover,
            ProtocolPaths paths,
            int sequence) throws Exception {
        try {
            PipelineMessage validated = validator.processMessage(initial);
            fileMover.writeProcessingState(paths, validated, "transaction-validator", sequence);
            if (validated.status() == ProcessingStatus.REJECTED) {
                return validated;
            }
            PipelineMessage scored = fraudDetector.processMessage(validated);
            fileMover.writeOutputState(paths, scored, "fraud-detector", sequence);
            PipelineMessage settled = settlementProcessor.processMessage(scored);
            fileMover.writeOutputState(paths, settled, "settlement-processor", sequence);
            return settled;
        } catch (Exception ex) {
            return initial.withState(
                    "integrator",
                    "reporting_agent",
                    ProcessingStatus.ERROR,
                    List.of(ReasonCode.PROCESSING_ERROR),
                    null,
                    new AuditEvent(Instant.now(), "Integrator", initial.transaction().transactionId(), "error", ReasonCode.PROCESSING_ERROR));
        }
    }
}
