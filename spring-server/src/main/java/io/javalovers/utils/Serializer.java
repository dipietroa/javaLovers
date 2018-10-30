package io.javalovers.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public abstract class Serializer {
  /**
   * Converts java object into JSON String format
   * 
   * @param obj
   * @return
   */
  public static String asJsonString(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
