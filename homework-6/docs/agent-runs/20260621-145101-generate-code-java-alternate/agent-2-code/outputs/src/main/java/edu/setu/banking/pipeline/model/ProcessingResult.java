package edu.setu.banking.pipeline.model;

import java.util.ArrayList;
import java.util.List;

public class ProcessingResult {
  public String transaction_id;
  public String amount;
  public String currency;
  public String status;
  public List<String> reason_codes = new ArrayList<>();
  public List<AuditEvent> audit_events = new ArrayList<>();
  public List<String> component_history = new ArrayList<>();
  public String settlement_reference;

  public ProcessingResult() {}

  public ProcessingResult(String transactionId, String amount, String currency) {
    this.transaction_id = transactionId;
    this.amount = amount;
    this.currency = currency;
    this.status = "received";
  }

  public void addAudit(String component, String outcome, String reasonCode) {
    audit_events.add(new AuditEvent(TimeSupport.now(), component, transaction_id, outcome, reasonCode));
    component_history.add(component + ":" + outcome);
  }
}
