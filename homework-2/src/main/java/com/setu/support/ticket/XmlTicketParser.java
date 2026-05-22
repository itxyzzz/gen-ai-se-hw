package com.setu.support.ticket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class XmlTicketParser implements TicketParser {
    private final XmlMapper xmlMapper = new XmlMapper();

    @Override
    public List<CreateTicketRequest> parse(String content) {
        try {
            JsonNode root = xmlMapper.readTree(content);
            JsonNode tickets = root.get("ticket");
            if (tickets == null) {
                tickets = root;
            }
            List<CreateTicketRequest> requests = new ArrayList<>();
            if (tickets.isArray()) {
                tickets.forEach(node -> requests.add(toRequest(node)));
            } else {
                requests.add(toRequest(tickets));
            }
            return requests;
        } catch (JsonProcessingException exception) {
            throw new MalformedImportException("XML import must be a valid tickets document");
        }
    }

    private CreateTicketRequest toRequest(JsonNode node) {
        JsonNode metadata = node.get("metadata");
        return new CreateTicketRequest(
            text(node, "customer_id"),
            text(node, "customer_email"),
            text(node, "customer_name"),
            text(node, "subject"),
            text(node, "description"),
            text(node, "category"),
            text(node, "priority"),
            text(node, "status"),
            instant(text(node, "resolved_at")),
            text(node, "assigned_to"),
            tags(node.get("tags")),
            new TicketMetadata(
                metadata == null ? null : text(metadata, "source"),
                metadata == null ? null : text(metadata, "browser"),
                metadata == null ? null : text(metadata, "device_type")
            )
        );
    }

    private String text(JsonNode node, String field) {
        JsonNode value = node == null ? null : node.get(field);
        if (value == null || value.isNull()) {
            return null;
        }
        String text = value.asText();
        return text.isBlank() ? null : text;
    }

    private List<String> tags(JsonNode node) {
        if (node == null || node.isNull()) {
            return List.of();
        }
        if (node.isTextual()) {
            return splitTags(node.asText());
        }
        if (node.isArray()) {
            List<String> tags = new ArrayList<>();
            node.forEach(tag -> tags.add(tag.asText()));
            return tags.stream().filter(tag -> !tag.isBlank()).toList();
        }
        JsonNode tag = node.get("tag");
        if (tag == null) {
            return List.of();
        }
        if (tag.isArray()) {
            List<String> tags = new ArrayList<>();
            tag.forEach(item -> tags.add(item.asText()));
            return tags.stream().filter(value -> !value.isBlank()).toList();
        }
        return splitTags(tag.asText());
    }

    private List<String> splitTags(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return List.of(value.split(";")).stream().map(String::trim).filter(tag -> !tag.isBlank()).toList();
    }

    private Instant instant(String value) {
        if (value == null) {
            return null;
        }
        try {
            return Instant.parse(value);
        } catch (RuntimeException exception) {
            return null;
        }
    }
}
