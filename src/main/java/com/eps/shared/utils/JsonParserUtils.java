package com.eps.shared.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.HashMap;
import java.util.Map;

public class JsonParserUtils {
  private static ObjectMapper mapper = null;

  public static ObjectMapper getObjectMapper() {
    if (mapper == null) {
      mapper = new ObjectMapper();
      mapper.registerModule(new JavaTimeModule());
      mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    return mapper;
  }

  public static Map<String, String> toStringMap(String mapString) {
    try {
      return getObjectMapper().readValue(mapString, new TypeReference<>() {});
    } catch (Exception e) {
      return new HashMap<>();
    }
  }

  /**
   * @param object
   * @return
   */
  public static Map<String, Object> toObjectMap(String mapString) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      Map<String, Object> map =
          objectMapper.readValue(mapString, new TypeReference<Map<String, Object>>() {});
      return map;
    } catch (Exception e) {
      return new HashMap<>();
    }
  }

  public static <T> T entity(String json, Class<T> tClass) {
    try {

      return getObjectMapper().readValue(json, tClass);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Writes specified object as string
   *
   * @param object object to write
   * @return result json
   */
  public static String toJson(Object object) {
    try {
      return getObjectMapper().writeValueAsString(object);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
