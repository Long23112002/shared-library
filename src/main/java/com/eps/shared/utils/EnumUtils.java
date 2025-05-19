package com.eps.shared.utils;

import com.eps.shared.models.exceptions.CommonErrorMessage;
import com.eps.shared.models.exceptions.ResponseException;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

public class EnumUtils {

  @SuppressWarnings("unchecked")
  public static Object convertEnum(Class<?> clazz, Object value) {
    if (value == null || !StringUtils.hasText(value.toString())) {
      return null;
    }
    try {
      return Enum.valueOf((Class<Enum>) clazz.asSubclass(Enum.class), value.toString());
    } catch (Exception ignore) {

    }

    throw new ResponseException(
        HttpStatus.BAD_REQUEST,
        CommonErrorMessage.ENUM_FAILED,
        new HashMap<>() {
          {
            put("value", value.toString());
            put("type", clazz.getSimpleName());
          }
        });
  }
}
