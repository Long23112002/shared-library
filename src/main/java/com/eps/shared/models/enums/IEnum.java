package com.eps.shared.models.enums;

import com.eps.shared.utils.EnumUtils;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod(EnumUtils.class)
public interface IEnum {
  byte getValue();
}
