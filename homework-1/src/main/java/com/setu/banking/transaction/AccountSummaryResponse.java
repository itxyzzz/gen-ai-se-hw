package com.setu.banking.transaction;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

public record AccountSummaryResponse(
    String accountId,
    Map<String, BigDecimal> totalDeposits,
    Map<String, BigDecimal> totalWithdrawals,
    Map<String, BigDecimal> totalIncomingTransfers,
    Map<String, BigDecimal> totalOutgoingTransfers,
    long transactionCount,
    Instant mostRecentTransactionDate
) {
}
