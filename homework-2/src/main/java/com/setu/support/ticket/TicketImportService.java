package com.setu.support.ticket;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class TicketImportService {
    private final TicketService ticketService;
    private final TicketValidator validator;
    private final CsvTicketParser csvTicketParser;
    private final JsonTicketParser jsonTicketParser;
    private final XmlTicketParser xmlTicketParser;

    public TicketImportService(
        TicketService ticketService,
        TicketValidator validator,
        CsvTicketParser csvTicketParser,
        JsonTicketParser jsonTicketParser,
        XmlTicketParser xmlTicketParser
    ) {
        this.ticketService = ticketService;
        this.validator = validator;
        this.csvTicketParser = csvTicketParser;
        this.jsonTicketParser = jsonTicketParser;
        this.xmlTicketParser = xmlTicketParser;
    }

    public TicketImportSummary importFile(MultipartFile file, String requestedFormat) {
        if (file == null || file.isEmpty()) {
            throw new MalformedImportException("Import file is required");
        }
        String format = resolveFormat(file, requestedFormat);
        List<CreateTicketRequest> records = parser(format).parse(content(file));
        List<UUID> createdIds = new ArrayList<>();
        List<TicketImportError> errors = new ArrayList<>();
        int failedRecords = 0;

        for (int index = 0; index < records.size(); index++) {
            CreateTicketRequest request = records.get(index);
            List<ValidationErrorResponse.FieldError> validationErrors = validator.validateFields(request);
            if (validationErrors.isEmpty()) {
                createdIds.add(ticketService.create(request).id());
            } else {
                failedRecords++;
                int recordNumber = index + 1;
                validationErrors.forEach(error -> errors.add(new TicketImportError(recordNumber, error.field(), error.message())));
            }
        }

        return new TicketImportSummary(records.size(), createdIds.size(), failedRecords, createdIds, errors);
    }

    private TicketParser parser(String format) {
        return switch (format) {
            case "csv" -> csvTicketParser;
            case "json" -> jsonTicketParser;
            case "xml" -> xmlTicketParser;
            default -> throw new MalformedImportException("Format must be csv, json, or xml");
        };
    }

    private String content(MultipartFile file) {
        try {
            return new String(file.getBytes(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new MalformedImportException("Import file could not be read");
        }
    }

    private String resolveFormat(MultipartFile file, String requestedFormat) {
        if (requestedFormat != null && !requestedFormat.isBlank()) {
            return normalize(requestedFormat);
        }
        String filename = file.getOriginalFilename();
        if (filename != null && filename.contains(".")) {
            return normalize(filename.substring(filename.lastIndexOf('.') + 1));
        }
        String contentType = file.getContentType();
        if (contentType != null) {
            if (contentType.contains("json")) {
                return "json";
            }
            if (contentType.contains("xml")) {
                return "xml";
            }
            if (contentType.contains("csv") || contentType.contains("text/plain")) {
                return "csv";
            }
        }
        throw new MalformedImportException("Unable to determine import format; provide format=csv, json, or xml");
    }

    private String normalize(String value) {
        String format = value.toLowerCase(Locale.ROOT).trim();
        if (!List.of("csv", "json", "xml").contains(format)) {
            throw new MalformedImportException("Format must be csv, json, or xml");
        }
        return format;
    }
}
