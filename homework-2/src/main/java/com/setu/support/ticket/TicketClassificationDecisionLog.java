package com.setu.support.ticket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class TicketClassificationDecisionLog {
    private static final Logger log = LoggerFactory.getLogger(TicketClassificationDecisionLog.class);

    private final CopyOnWriteArrayList<ClassificationDecision> decisions = new CopyOnWriteArrayList<>();

    public void record(ClassificationDecision decision) {
        decisions.add(decision);
        log.info(
            "Classification decision ticketId={} trigger={} suggestedCategory={} suggestedPriority={} appliedCategory={} appliedPriority={} confidence={} manualOverride={}",
            decision.ticketId(),
            decision.trigger(),
            decision.suggestedCategory(),
            decision.suggestedPriority(),
            decision.appliedCategory(),
            decision.appliedPriority(),
            decision.confidenceScore(),
            decision.manualOverrideApplied()
        );
    }

    public List<ClassificationDecision> findAll() {
        return List.copyOf(decisions);
    }

    public void clear() {
        decisions.clear();
    }
}
