package com.eps.shared.utils;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

public class ValidatorUtils {

  private static Validator validator;

  public static Validator getValidator() {

    if (validator == null) {
      validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    return validator;
  }
}
