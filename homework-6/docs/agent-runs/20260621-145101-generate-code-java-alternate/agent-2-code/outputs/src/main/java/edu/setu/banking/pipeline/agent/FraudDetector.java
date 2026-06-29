package edu.setu.banking.pipeline.agent;

import edu.setu.banking.pipeline.model.ProcessingResult;
import edu.setu.banking.pipeline.model.TransactionRecord;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Map;

public class FraudDetector {
  private static final BigDecimal HIGH_VALUE = new BigDecimal("10000.00");
  private static final BigDecimal VERY_HIGH_VALUE = new BigDecimal("50000.00");

  public ProcessingResult process(TransactionRecord record, ProcessingResult result) {
    if (!"validated".equals(result.status)) {
      result.addAudit("FraudDetector", "skipped", "VALIDATION_NOT_PASSED");
      return result;
    }
    int reviewSignals = 0;
    BigDecimal amount = new BigDecimal(record.amount);
    if (amount.compareTo(VERY_HIGH_VALUE) >= 0) {
      result.reason_codes.add("REVIEW_VERY_HIGH_VALUE");
      reviewSignals++;
    } else if (amount.compareTo(HIGH_VALUE) > 0) {
      result.reason_codes.add("REVIEW_HIGH_VALUE");
      reviewSignals++;
    }
    if (isOddHour(record.timestamp)) {
      result.reason_codes.add("REVIEW_ODD_HOUR");
      reviewSignals++;
    }
    if (isCrossBorder(record.metadata)) {
      result.reason_codes.add("REVIEW_CROSS_BORDER");
      reviewSignals++;
    }
    if (record.destinationAccount != null && record.destinationAccount.endsWith("9999")) {
      result.reason_codes.add("REVIEW_DESTINATION_PATTERN");
      reviewSignals++;
    }
    result.status = reviewSignals == 0 ? "low_risk" : "review_required";
    result.addAudit("FraudDetector", result.status, reviewSignals == 0 ? "LOW_RISK" : "REVIEW_SIGNAL");
    return result;
  }

  private static boolean isOddHour(String timestamp) {
    try {
      int hour = Instant.parse(timestamp).atZone(ZoneOffset.UTC).getHour();
      return hour < 5;
    } catch (RuntimeException ex) {
      return false;
    }
  }

  private static boolean isCrossBorder(Map<String, Object> metadata) {
    if (metadata == null) {
      return false;
    }
    Object country = metadata.get("country");
    return country instanceof String text && !"US".equalsIgnoreCase(text);
  }
}
