package edu.setu.banking.pipeline.agent;

import edu.setu.banking.pipeline.model.ProcessingResult;

public class SettlementProcessor {
  public ProcessingResult process(ProcessingResult result) {
    if ("low_risk".equals(result.status)) {
      result.status = "settled";
      result.settlement_reference = "SIM-" + result.transaction_id;
      result.addAudit("SettlementProcessor", "settled", "SIMULATED_SETTLEMENT");
    } else if ("review_required".equals(result.status)) {
      result.addAudit("SettlementProcessor", "held_for_review", "MANUAL_REVIEW_REQUIRED");
    } else if ("rejected".equals(result.status)) {
      result.addAudit("SettlementProcessor", "not_settled", "REJECTED");
    } else {
      result.status = "error";
      result.reason_codes.add("UNEXPECTED_STATUS");
      result.addAudit("SettlementProcessor", "error", "UNEXPECTED_STATUS");
    }
    return result;
  }
}
