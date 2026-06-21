package edu.setu.banking.pipeline.agent;

import edu.setu.banking.pipeline.model.ProcessingResult;
import edu.setu.banking.pipeline.model.TransactionRecord;
import java.math.BigDecimal;
import java.util.Set;

public class TransactionValidator {
  private static final Set<String> SUPPORTED_CURRENCIES = Set.of("USD", "EUR", "GBP", "JPY");

  public ProcessingResult process(TransactionRecord record) {
    ProcessingResult result =
        new ProcessingResult(
            safeTransactionId(record), record == null ? null : record.amount, record == null ? null : record.currency);
    if (record == null) {
      reject(result, "MISSING_TRANSACTION");
      return result;
    }
    require(result, record.transactionId, "MISSING_TRANSACTION_ID");
    require(result, record.timestamp, "MISSING_TIMESTAMP");
    require(result, record.sourceAccount, "MISSING_SOURCE_ACCOUNT");
    require(result, record.destinationAccount, "MISSING_DESTINATION_ACCOUNT");
    require(result, record.amount, "MISSING_AMOUNT");
    require(result, record.currency, "MISSING_CURRENCY");

    BigDecimal amount = parseAmount(record.amount);
    if (amount == null) {
      result.reason_codes.add("INVALID_AMOUNT");
    } else if (amount.compareTo(BigDecimal.ZERO) <= 0) {
      result.reason_codes.add("NON_POSITIVE_AMOUNT");
    }

    if (record.currency != null && !SUPPORTED_CURRENCIES.contains(record.currency)) {
      result.reason_codes.add("UNSUPPORTED_CURRENCY");
    }

    if (result.reason_codes.isEmpty()) {
      result.status = "validated";
      result.addAudit("TransactionValidator", "validated", "VALID");
    } else {
      result.status = "rejected";
      result.addAudit("TransactionValidator", "rejected", result.reason_codes.get(0));
    }
    return result;
  }

  public boolean isValid(TransactionRecord record) {
    return "validated".equals(process(record).status);
  }

  private static void require(ProcessingResult result, String value, String reasonCode) {
    if (value == null || value.isBlank()) {
      result.reason_codes.add(reasonCode);
    }
  }

  private static void reject(ProcessingResult result, String reasonCode) {
    result.status = "rejected";
    result.reason_codes.add(reasonCode);
    result.addAudit("TransactionValidator", "rejected", reasonCode);
  }

  private static BigDecimal parseAmount(String value) {
    if (value == null || value.isBlank()) {
      return null;
    }
    try {
      return new BigDecimal(value);
    } catch (NumberFormatException ex) {
      return null;
    }
  }

  private static String safeTransactionId(TransactionRecord record) {
    return record == null || record.transactionId == null ? "UNKNOWN" : record.transactionId;
  }
}
