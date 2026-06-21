package edu.setu.transactionpipeline.agent;

import edu.setu.transactionpipeline.model.AuditEvent;
import edu.setu.transactionpipeline.model.FraudAssessment;
import edu.setu.transactionpipeline.model.PipelineMessage;
import edu.setu.transactionpipeline.model.ProcessingStatus;
import edu.setu.transactionpipeline.model.ReasonCode;
import edu.setu.transactionpipeline.model.RiskTier;

import java.time.Instant;
import java.util.List;

public final class SettlementProcessor {
    public PipelineMessage processMessage(PipelineMessage message) {
        ProcessingStatus finalStatus = mapToFinalStatus(message);
        String reason = finalStatus == ProcessingStatus.ERROR ? ReasonCode.PROCESSING_ERROR : null;
        return message.withState(
                "settlement_processor",
                "reporting_agent",
                finalStatus,
                reason == null ? List.of() : List.of(reason),
                message.fraudAssessment(),
                new AuditEvent(Instant.now(), "SettlementProcessor", message.transaction().transactionId(), finalStatus.jsonValue(), reason));
    }

    public ProcessingStatus mapToFinalStatus(PipelineMessage message) {
        if (message.status() == ProcessingStatus.REJECTED) {
            return ProcessingStatus.REJECTED;
        }
        FraudAssessment assessment = message.fraudAssessment();
        if (assessment == null) {
            return ProcessingStatus.ERROR;
        }
        if (assessment.riskTier() == RiskTier.LOW) {
            return ProcessingStatus.SETTLED;
        }
        return ProcessingStatus.REVIEW_REQUIRED;
    }
}
