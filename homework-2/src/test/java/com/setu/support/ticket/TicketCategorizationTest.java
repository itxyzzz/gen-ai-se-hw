package com.setu.support.ticket;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TicketCategorizationTest {
    private final TicketClassificationService service = new TicketClassificationService();

    @ParameterizedTest
    @CsvSource({
        "'Login and password reset', 'I cannot login after a password reset', account_access",
        "'Checkout error', 'The app shows an error and keeps crashing', technical_issue",
        "'Invoice refund', 'I need a refund for this subscription charge', billing_question",
        "'Feature suggestion', 'I would like a feature request to improve reports', feature_request",
        "'Bug with reproduction', 'Bug report with steps to reproduce expected and actual results', bug_report"
    })
    void assignsCategoryFromTicketKeywords(String subject, String description, String expectedCategory) {
        ClassificationSuggestion result = service.classify(ticket(subject, description));

        assertThat(result.category()).isEqualTo(expectedCategory);
        assertThat(result.keywordsFound()).isNotEmpty();
        assertThat(result.reasoning()).contains(expectedCategory);
    }

    @ParameterizedTest
    @CsvSource({
        "'Production down security incident', urgent",
        "'This is important and blocking us asap', high",
        "'Minor cosmetic suggestion for a button', low",
        "'General question about the dashboard', medium"
    })
    void assignsPriorityFromPriorityKeywords(String description, String expectedPriority) {
        ClassificationSuggestion result = service.classify(ticket("Priority check", description));

        assertThat(result.priority()).isEqualTo(expectedPriority);
    }

    @Test
    void usesHighestPriorityWhenMultiplePriorityKeywordsMatch() {
        ClassificationSuggestion result = service.classify(ticket(
            "Suggestion but production down",
            "This is a minor suggestion, but production down makes it critical."
        ));

        assertThat(result.priority()).isEqualTo("urgent");
        assertThat(result.keywordsFound()).contains("production down", "critical", "minor", "suggestion");
    }

    @Test
    void defaultsToOtherAndMediumWithLowerConfidenceWhenNoKeywordsMatch() {
        ClassificationSuggestion result = service.classify(ticket(
            "General message",
            "I have a question about my account experience today."
        ));

        assertThat(result.category()).isEqualTo("other");
        assertThat(result.priority()).isEqualTo("medium");
        assertThat(result.confidenceScore()).isBetween(0.0, 0.5);
        assertThat(result.reasoning()).contains("No category keyword");
    }

    @Test
    void includesTagsInClassificationText() {
        ClassificationSuggestion result = service.classify(new CreateTicketRequest(
            "CUST-TAG",
            "tag@example.com",
            "Tag Customer",
            "Question",
            "This description is long enough for validation.",
            null,
            null,
            "new",
            null,
            null,
            List.of("billing", "invoice"),
            new TicketMetadata("api", null, "desktop")
        ));

        assertThat(result.category()).isEqualTo("billing_question");
        assertThat(result.keywordsFound()).contains("billing", "invoice");
    }

    private CreateTicketRequest ticket(String subject, String description) {
        return new CreateTicketRequest(
            "CUST-CLASSIFY",
            "classify@example.com",
            "Classify Customer",
            subject,
            description,
            null,
            null,
            "new",
            null,
            null,
            List.of(),
            new TicketMetadata("api", null, "desktop")
        );
    }
}
