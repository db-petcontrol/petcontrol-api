package br.com.db.petcontrol.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonUtils {
  private static ObjectMapper objectMapper =
      new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  public static String convertToJson(Object object) {
    try {
      return objectMapper.writeValueAsString(object);

    } catch (JsonProcessingException e) {
      throw new RuntimeException("Error converting to JSON", e);
    }
  }

  private JsonUtils() {}
}
