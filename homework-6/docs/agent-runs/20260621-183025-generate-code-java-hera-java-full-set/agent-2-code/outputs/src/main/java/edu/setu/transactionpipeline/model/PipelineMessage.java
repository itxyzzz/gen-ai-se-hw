package edu.setu.transactionpipeline.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public final class PipelineMessage {
    private final String messageId;
    private final Instant timestamp;
    private final String sourceAgent;
    private final String targetAgent;
    private final String messageType;
    private final TransactionRecord transaction;
    private final ProcessingStatus status;
    private final List<String> reasonCodes;
    private final FraudAssessment fraudAssessment;
    private final List<AuditEvent> auditEvents;

    public PipelineMessage(
            String messageId,
            Instant timestamp,
            String sourceAgent,
            String targetAgent,
            String messageType,
            TransactionRecord transaction,
            ProcessingStatus status,
            List<String> reasonCodes,
            FraudAssessment fraudAssessment,
            List<AuditEvent> auditEvents) {
        this.messageId = messageId;
        this.timestamp = timestamp;
        this.sourceAgent = sourceAgent;
        this.targetAgent = targetAgent;
        this.messageType = messageType;
        this.transaction = transaction;
        this.status = status;
        this.reasonCodes = List.copyOf(reasonCodes == null ? List.of() : reasonCodes);
        this.fraudAssessment = fraudAssessment;
        this.auditEvents = List.copyOf(auditEvents == null ? List.of() : auditEvents);
    }

    public static PipelineMessage initial(TransactionRecord record, Instant timestamp) {
        String idSeed = record.transactionId() == null ? UUID.randomUUID().toString() : record.transactionId();
        return new PipelineMessage(
                UUID.nameUUIDFromBytes(idSeed.getBytes()).toString(),
                timestamp,
                "integrator",
                "transaction_validator",
                "transaction",
                record,
                null,
                List.of(),
                null,
                List.of());
    }

    public PipelineMessage withState(
            String sourceAgent,
            String targetAgent,
            ProcessingStatus status,
            List<String> reasonCodes,
            FraudAssessment assessment,
            AuditEvent auditEvent) {
        List<String> nextReasons = new ArrayList<>(this.reasonCodes);
        if (reasonCodes != null) {
            for (String reasonCode : reasonCodes) {
                if (!nextReasons.contains(reasonCode)) {
                    nextReasons.add(reasonCode);
                }
            }
        }
        List<AuditEvent> nextAudit = new ArrayList<>(this.auditEvents);
        if (auditEvent != null) {
            nextAudit.add(auditEvent);
        }
        return new PipelineMessage(
                messageId,
                Instant.now(),
                sourceAgent,
                targetAgent,
                messageType,
                transaction,
                status,
                nextReasons,
                assessment == null ? fraudAssessment : assessment,
                nextAudit);
    }

    @JsonProperty("message_id")
    public String messageId() {
        return messageId;
    }

    @JsonProperty("timestamp")
    public Instant timestamp() {
        return timestamp;
    }

    @JsonProperty("source_agent")
    public String sourceAgent() {
        return sourceAgent;
    }

    @JsonProperty("target_agent")
    public String targetAgent() {
        return targetAgent;
    }

    @JsonProperty("message_type")
    public String messageType() {
        return messageType;
    }

    @JsonProperty("data")
    public TransactionRecord transaction() {
        return transaction;
    }

    @JsonProperty("status")
    public ProcessingStatus status() {
        return status;
    }

    @JsonProperty("reason_codes")
    public List<String> reasonCodes() {
        return reasonCodes;
    }

    @JsonProperty("fraud_assessment")
    public FraudAssessment fraudAssessment() {
        return fraudAssessment;
    }

    @JsonProperty("audit_events")
    public List<AuditEvent> auditEvents() {
        return auditEvents;
    }
}
