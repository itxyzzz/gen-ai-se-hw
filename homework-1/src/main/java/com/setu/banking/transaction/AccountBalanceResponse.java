package com.setu.banking.transaction;

import java.math.BigDecimal;
import java.util.Map;

public record AccountBalanceResponse(
    String accountId,
    Map<String, BigDecimal> balances
) {
}
