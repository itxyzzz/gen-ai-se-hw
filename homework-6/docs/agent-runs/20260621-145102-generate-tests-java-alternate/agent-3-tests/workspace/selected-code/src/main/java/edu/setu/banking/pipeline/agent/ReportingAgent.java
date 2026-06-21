package edu.setu.banking.pipeline.agent;

import edu.setu.banking.pipeline.model.ProcessingResult;
import edu.setu.banking.pipeline.model.Summary;
import java.util.List;

public class ReportingAgent {
  public Summary summarize(List<ProcessingResult> results) {
    Summary summary = new Summary();
    summary.total_transactions = results.size();
    for (ProcessingResult result : results) {
      switch (result.status) {
        case "settled" -> summary.settled++;
        case "rejected" -> summary.rejected++;
        case "review_required" -> summary.review_required++;
        default -> summary.error++;
      }
      for (String reasonCode : result.reason_codes) {
        summary.reason_code_groups.merge(reasonCode, 1, Integer::sum);
      }
    }
    return summary;
  }
}
