package edu.setu.banking.pipeline.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class JsonSupport {
  public static final ObjectMapper MAPPER =
      new ObjectMapper()
          .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
          .enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN)
          .enable(SerializationFeature.INDENT_OUTPUT)
          .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

  private JsonSupport() {}

  public static <T> T read(Path path, Class<T> type) throws IOException {
    return MAPPER.readValue(path.toFile(), type);
  }

  public static void write(Path path, Object value) throws IOException {
    Files.createDirectories(path.getParent());
    MAPPER.writeValue(path.toFile(), value);
  }
}
