package edu.setu.transactionpipeline.agent;

import edu.setu.transactionpipeline.model.AuditEvent;
import edu.setu.transactionpipeline.model.PipelineMessage;
import edu.setu.transactionpipeline.model.ProcessingStatus;
import edu.setu.transactionpipeline.model.ReasonCode;
import edu.setu.transactionpipeline.model.TransactionRecord;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class TransactionValidator {
    private static final Set<String> SUPPORTED_CURRENCIES = Set.of("USD", "EUR", "GBP");

    public PipelineMessage processMessage(PipelineMessage message) {
        List<String> reasons = validate(message.transaction());
        if (reasons.isEmpty()) {
            return message.withState(
                    "transaction_validator",
                    "fraud_detector",
                    ProcessingStatus.VALIDATED,
                    List.of(),
                    null,
                    audit(message, "validated", null));
        }
        return message.withState(
                "transaction_validator",
                "reporting_agent",
                ProcessingStatus.REJECTED,
                reasons,
                null,
                audit(message, "rejected", reasons.get(0)));
    }

    public List<String> validate(TransactionRecord record) {
        List<String> reasons = new ArrayList<>();
        if (blank(record.transactionId()) || blank(record.timestamp()) || blank(record.amountText())
                || blank(record.currency()) || blank(record.transactionType())
                || !record.sourcePresent() || !record.destinationPresent()) {
            reasons.add(ReasonCode.MISSING_REQUIRED_FIELD);
        }
        try {
            Instant.parse(record.timestamp());
        } catch (RuntimeException ex) {
            reasons.add(ReasonCode.INVALID_TIMESTAMP);
        }
        try {
            BigDecimal amount = new BigDecimal(record.amountText());
            record.setParsedAmount(amount);
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                reasons.add(ReasonCode.NON_POSITIVE_AMOUNT);
            }
        } catch (RuntimeException ex) {
            reasons.add(ReasonCode.INVALID_AMOUNT);
        }
        if (record.currency() == null || !SUPPORTED_CURRENCIES.contains(record.currency())) {
            reasons.add(ReasonCode.UNSUPPORTED_CURRENCY);
        }
        return List.copyOf(reasons);
    }

    private static boolean blank(String value) {
        return value == null || value.isBlank();
    }

    private static AuditEvent audit(PipelineMessage message, String outcome, String reasonCode) {
        return new AuditEvent(Instant.now(), "TransactionValidator", message.transaction().transactionId(), outcome, reasonCode);
    }
}
