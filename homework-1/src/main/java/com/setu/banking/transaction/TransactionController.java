package com.setu.banking.transaction;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
public class TransactionController {
    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @PostMapping("/transactions")
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction create(@RequestBody CreateTransactionRequest request) {
        return service.create(request);
    }

    @GetMapping("/transactions")
    public List<Transaction> list(
        @RequestParam(required = false) String accountId,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String from,
        @RequestParam(required = false) String to
    ) {
        return service.findFiltered(accountId, type, from, to);
    }

    @GetMapping("/transactions/{id}")
    public Transaction get(@PathVariable String id) {
        return service.findById(id);
    }

    @GetMapping("/accounts/{accountId}/balance")
    public Map<String, Object> balance(@PathVariable String accountId) {
        BigDecimal balance = service.balanceFor(accountId);
        return Map.of(
            "accountId", accountId,
            "balance", balance,
            "currency", "USD"
        );
    }

    @GetMapping("/accounts/{accountId}/summary")
    public AccountSummaryResponse summary(@PathVariable String accountId) {
        return service.summaryFor(accountId);
    }
}
