package edu.setu.banking.pipeline.model;

public record AuditEvent(
    String timestamp,
    String component,
    String transaction_id,
    String outcome,
    String reason_code) {}
