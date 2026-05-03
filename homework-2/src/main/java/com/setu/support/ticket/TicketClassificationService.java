package com.setu.support.ticket;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class TicketClassificationService {
    private static final Map<String, List<KeywordPattern>> CATEGORY_KEYWORDS = new LinkedHashMap<>();
    private static final List<PriorityRule> PRIORITY_RULES = List.of(
        new PriorityRule("urgent", 4, List.of(
            keyword("can't access", "can't access", "cannot access"),
            keyword("critical"),
            keyword("production down"),
            keyword("security")
        )),
        new PriorityRule("high", 3, List.of(
            keyword("important"),
            keyword("blocking"),
            keyword("asap")
        )),
        new PriorityRule("low", 1, List.of(
            keyword("minor"),
            keyword("cosmetic"),
            keyword("suggestion")
        ))
    );

    static {
        CATEGORY_KEYWORDS.put("account_access", List.of(
            keyword("login", "login", "log in"),
            keyword("password"),
            keyword("reset"),
            keyword("2fa"),
            keyword("two-factor"),
            keyword("authenticator"),
            keyword("can't access", "can't access", "cannot access"),
            keyword("account locked")
        ));
        CATEGORY_KEYWORDS.put("technical_issue", List.of(
            keyword("error"),
            keyword("crash", "crash", "crashing"),
            keyword("timeout"),
            keyword("failed"),
            keyword("failure"),
            keyword("not working"),
            keyword("not loading"),
            keyword("unavailable"),
            keyword("500")
        ));
        CATEGORY_KEYWORDS.put("billing_question", List.of(
            keyword("billing"),
            keyword("payment"),
            keyword("invoice"),
            keyword("refund"),
            keyword("charge"),
            keyword("subscription"),
            keyword("receipt"),
            keyword("card")
        ));
        CATEGORY_KEYWORDS.put("feature_request", List.of(
            keyword("feature"),
            keyword("enhancement"),
            keyword("suggestion"),
            keyword("request"),
            keyword("add"),
            keyword("improve"),
            keyword("would like")
        ));
        CATEGORY_KEYWORDS.put("bug_report", List.of(
            keyword("bug"),
            keyword("defect"),
            keyword("reproduce"),
            keyword("reproduction"),
            keyword("steps to reproduce"),
            keyword("expected"),
            keyword("actual")
        ));
    }

    public ClassificationSuggestion classify(CreateTicketRequest request) {
        String text = searchableText(request);
        Match categoryMatch = bestCategory(text);
        Match priorityMatch = bestPriority(text);
        List<String> keywords = new ArrayList<>();
        keywords.addAll(categoryMatch.keywords());
        keywords.addAll(priorityMatch.keywords());
        double confidence = confidence(categoryMatch, priorityMatch);
        String reasoning = reasoning(categoryMatch, priorityMatch);

        return new ClassificationSuggestion(
            categoryMatch.value(),
            priorityMatch.value(),
            confidence,
            reasoning,
            keywords.stream().distinct().toList()
        );
    }

    public ClassificationSuggestion classify(Ticket ticket) {
        return classify(new CreateTicketRequest(
            ticket.customerId(),
            ticket.customerEmail(),
            ticket.customerName(),
            ticket.subject(),
            ticket.description(),
            ticket.category(),
            ticket.priority(),
            ticket.status(),
            ticket.resolvedAt(),
            ticket.assignedTo(),
            ticket.tags(),
            ticket.metadata()
        ));
    }

    private Match bestCategory(String text) {
        return CATEGORY_KEYWORDS.entrySet().stream()
            .map(entry -> new Match(entry.getKey(), matchedKeywords(text, entry.getValue()), 0))
            .max(Comparator.comparingInt(match -> match.keywords().size()))
            .filter(match -> !match.keywords().isEmpty())
            .orElse(new Match("other", List.of(), 0));
    }

    private Match bestPriority(String text) {
        List<String> allMatchedKeywords = PRIORITY_RULES.stream()
            .flatMap(rule -> matchedKeywords(text, rule.keywords()).stream())
            .distinct()
            .toList();
        Match selectedPriority = PRIORITY_RULES.stream()
            .map(rule -> new Match(rule.priority(), matchedKeywords(text, rule.keywords()), rule.rank()))
            .filter(match -> !match.keywords().isEmpty())
            .max(Comparator.comparingInt(Match::rank))
            .orElse(new Match("medium", List.of(), 2));
        return new Match(selectedPriority.value(), allMatchedKeywords, selectedPriority.rank());
    }

    private List<String> matchedKeywords(String text, List<KeywordPattern> patterns) {
        return patterns.stream()
            .filter(pattern -> pattern.aliases().stream().anyMatch(text::contains))
            .map(KeywordPattern::canonical)
            .distinct()
            .toList();
    }

    private double confidence(Match categoryMatch, Match priorityMatch) {
        if (categoryMatch.keywords().isEmpty() && priorityMatch.keywords().isEmpty()) {
            return 0.35;
        }
        double score = 0.45 + (categoryMatch.keywords().size() * 0.08) + (priorityMatch.keywords().size() * 0.08);
        return Math.min(1.0, Math.round(score * 100.0) / 100.0);
    }

    private String reasoning(Match categoryMatch, Match priorityMatch) {
        String categoryReason = categoryMatch.keywords().isEmpty()
            ? "No category keyword matched; category set to other"
            : "Matched category " + categoryMatch.value() + " from keywords " + categoryMatch.keywords();
        String priorityReason = priorityMatch.keywords().isEmpty()
            ? "No priority keyword matched; priority set to medium"
            : "Matched priority " + priorityMatch.value() + " from keywords " + priorityMatch.keywords();
        return categoryReason + ". " + priorityReason + ".";
    }

    private String searchableText(CreateTicketRequest request) {
        if (request == null) {
            return "";
        }
        List<String> parts = new ArrayList<>();
        parts.add(request.subject());
        parts.add(request.description());
        if (request.tags() != null) {
            parts.addAll(request.tags());
        }
        return parts.stream()
            .filter(value -> value != null && !value.isBlank())
            .map(value -> value.toLowerCase(Locale.ROOT))
            .reduce("", (left, right) -> left + " " + right);
    }

    private static KeywordPattern keyword(String canonical, String... aliases) {
        List<String> values = aliases.length == 0 ? List.of(canonical) : List.of(aliases);
        return new KeywordPattern(canonical, values);
    }

    private record KeywordPattern(String canonical, List<String> aliases) {
    }

    private record PriorityRule(String priority, int rank, List<KeywordPattern> keywords) {
    }

    private record Match(String value, List<String> keywords, int rank) {
    }
}
