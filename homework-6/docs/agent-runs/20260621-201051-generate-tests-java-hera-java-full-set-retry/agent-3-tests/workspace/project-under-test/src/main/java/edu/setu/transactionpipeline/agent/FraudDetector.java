package edu.setu.transactionpipeline.agent;

import edu.setu.transactionpipeline.model.AuditEvent;
import edu.setu.transactionpipeline.model.FraudAssessment;
import edu.setu.transactionpipeline.model.PipelineMessage;
import edu.setu.transactionpipeline.model.ProcessingStatus;
import edu.setu.transactionpipeline.model.ReasonCode;
import edu.setu.transactionpipeline.model.RiskTier;
import edu.setu.transactionpipeline.model.TransactionRecord;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public final class FraudDetector {
    private static final BigDecimal HIGH_VALUE_THRESHOLD = new BigDecimal("10000.00");
    private static final BigDecimal VERY_HIGH_VALUE_THRESHOLD = new BigDecimal("50000.00");

    public PipelineMessage processMessage(PipelineMessage message) {
        if (message.status() == ProcessingStatus.REJECTED) {
            return message;
        }
        FraudAssessment assessment = assess(message.transaction());
        return message.withState(
                "fraud_detector",
                "settlement_processor",
                ProcessingStatus.RISK_SCORED,
                assessment.reasonCodes(),
                assessment,
                new AuditEvent(Instant.now(), "FraudDetector", message.transaction().transactionId(), assessment.riskTier().jsonValue(), null));
    }

    public FraudAssessment assess(TransactionRecord record) {
        int score = 0;
        List<String> reasons = new ArrayList<>();
        BigDecimal amount = record.parsedAmount() == null ? new BigDecimal(record.amountText()) : record.parsedAmount();
        if (amount.compareTo(HIGH_VALUE_THRESHOLD) >= 0) {
            score += 50;
            reasons.add(ReasonCode.HIGH_VALUE);
        }
        if (amount.compareTo(VERY_HIGH_VALUE_THRESHOLD) >= 0) {
            score += 40;
            reasons.add(ReasonCode.VERY_HIGH_VALUE);
        }
        int hour = Instant.parse(record.timestamp()).atZone(ZoneOffset.UTC).getHour();
        if (hour < 5) {
            score += 30;
            reasons.add(ReasonCode.ODD_HOUR);
        }
        if ("wire_transfer".equalsIgnoreCase(record.transactionType())) {
            score += 10;
            reasons.add(ReasonCode.WIRE_TRANSFER_TYPE);
        }
        if ("api".equalsIgnoreCase(record.channel()) || "mobile".equalsIgnoreCase(record.channel())) {
            score += 25;
            reasons.add(ReasonCode.CHANNEL_RISK);
        }
        if (record.country() != null && !"US".equalsIgnoreCase(record.country())) {
            score += 25;
            reasons.add(ReasonCode.COUNTRY_RISK);
        }
        return new FraudAssessment(score, classifyRisk(score), List.copyOf(reasons));
    }

    public RiskTier classifyRisk(int score) {
        if (score >= 80) {
            return RiskTier.VERY_HIGH;
        }
        if (score >= 50) {
            return RiskTier.HIGH;
        }
        if (score >= 25) {
            return RiskTier.MEDIUM;
        }
        return RiskTier.LOW;
    }
}
