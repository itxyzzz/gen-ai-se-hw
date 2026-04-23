package com.setu.banking.transaction;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Repository
public class TransactionRepository {
    private final List<Transaction> transactions = new CopyOnWriteArrayList<>();

    public Transaction save(Transaction transaction) {
        transactions.add(transaction);
        return transaction;
    }

    public List<Transaction> findAll() {
        return new ArrayList<>(transactions);
    }

    public Optional<Transaction> findById(String id) {
        return transactions.stream()
            .filter(transaction -> transaction.id().equals(id))
            .findFirst();
    }

    public void clear() {
        transactions.clear();
    }
}
