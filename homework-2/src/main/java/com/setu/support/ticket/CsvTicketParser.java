package com.setu.support.ticket;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CsvTicketParser implements TicketParser {
    @Override
    public List<CreateTicketRequest> parse(String content) {
        try (CSVParser parser = CSVFormat.DEFAULT.builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .setTrim(true)
            .build()
            .parse(new StringReader(content))) {
            List<CreateTicketRequest> requests = new ArrayList<>();
            for (CSVRecord record : parser) {
                requests.add(new CreateTicketRequest(
                    value(record, "customer_id"),
                    value(record, "customer_email"),
                    value(record, "customer_name"),
                    value(record, "subject"),
                    value(record, "description"),
                    value(record, "category"),
                    value(record, "priority"),
                    value(record, "status"),
                    instant(value(record, "resolved_at")),
                    value(record, "assigned_to"),
                    tags(value(record, "tags")),
                    new TicketMetadata(
                        value(record, "metadata_source"),
                        value(record, "metadata_browser"),
                        value(record, "metadata_device_type")
                    )
                ));
            }
            return requests;
        } catch (IllegalArgumentException | IOException | UncheckedIOException exception) {
            throw new MalformedImportException("CSV file must contain a valid header row and records");
        }
    }

    private String value(CSVRecord record, String field) {
        return record.isMapped(field) ? blankToNull(record.get(field)) : null;
    }

    private List<String> tags(String value) {
        if (value == null) {
            return List.of();
        }
        return Arrays.stream(value.split(";")).map(String::trim).filter(tag -> !tag.isBlank()).toList();
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

    private String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
