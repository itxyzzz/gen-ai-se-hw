package edu.setu.banking.pipeline;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import edu.setu.banking.pipeline.agent.ReportingAgent;
import edu.setu.banking.pipeline.agent.SettlementProcessor;
import edu.setu.banking.pipeline.model.ProcessingResult;
import edu.setu.banking.pipeline.model.Summary;
import java.util.List;
import org.junit.jupiter.api.Test;

class SettlementAndReportingTest {
  @Test
  void settlesLowRiskTransactions() {
    ProcessingResult result = new ProcessingResult("TXN-LOW", "25.00", "USD");
    result.status = "low_risk";

    ProcessingResult settled = new SettlementProcessor().process(result);

    assertEquals("settled", settled.status);
    assertNotNull(settled.settlement_reference);
  }

  @Test
  void summarizesStatusCountsAndReasonCodes() {
    ProcessingResult settled = new ProcessingResult("TXN-S", "25.00", "USD");
    settled.status = "settled";
    ProcessingResult rejected = new ProcessingResult("TXN-R", "-1.00", "GBP");
    rejected.status = "rejected";
    rejected.reason_codes.add("NON_POSITIVE_AMOUNT");

    Summary summary = new ReportingAgent().summarize(List.of(settled, rejected));

    assertEquals(2, summary.total_transactions);
    assertEquals(1, summary.settled);
    assertEquals(1, summary.rejected);
    assertEquals(1, summary.reason_code_groups.get("NON_POSITIVE_AMOUNT"));
  }
}
