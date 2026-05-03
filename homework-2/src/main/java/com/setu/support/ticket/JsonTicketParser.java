package com.setu.support.ticket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class JsonTicketParser implements TicketParser {
    private final ObjectMapper objectMapper;

    public JsonTicketParser() {
        this.objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Override
    public List<CreateTicketRequest> parse(String content) {
        try {
            CreateTicketRequest[] requests = objectMapper.readValue(content, CreateTicketRequest[].class);
            return Arrays.asList(requests);
        } catch (JsonProcessingException exception) {
            throw new MalformedImportException("JSON import must be a valid array of ticket objects");
        }
    }
}
