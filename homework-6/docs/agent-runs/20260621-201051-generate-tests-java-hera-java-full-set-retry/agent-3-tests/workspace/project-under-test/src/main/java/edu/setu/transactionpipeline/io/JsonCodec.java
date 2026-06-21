package edu.setu.transactionpipeline.io;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class JsonCodec {
    private final ObjectMapper mapper;

    public JsonCodec() {
        this.mapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .enable(SerializationFeature.INDENT_OUTPUT)
                .enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
                .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
                .enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public <T> T read(Path path, Class<T> targetType) throws IOException {
        return mapper.readValue(path.toFile(), targetType);
    }

    public <T> T read(Path path, TypeReference<T> targetType) throws IOException {
        return mapper.readValue(path.toFile(), targetType);
    }

    public JsonNode readTree(Path path) throws IOException {
        return mapper.readTree(path.toFile());
    }

    public void write(Path path, Object value) throws IOException {
        if (path.getParent() != null) {
            Files.createDirectories(path.getParent());
        }
        mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), value);
    }
}
