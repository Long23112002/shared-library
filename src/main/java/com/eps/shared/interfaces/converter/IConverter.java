package com.eps.shared.interfaces.converter;

import com.eps.shared.models.enums.IEnum;
import com.eps.shared.utils.EnumUtils;
import jakarta.persistence.AttributeConverter;
import java.lang.reflect.ParameterizedType;

public interface IConverter<E extends Enum<E> & IEnum> extends AttributeConverter<E, Byte> {
  @Override
  default Byte convertToDatabaseColumn(E attribute) {
    if (attribute == null) {
      return null;
    }
    return attribute.getValue();
  }

  @Override
  default E convertToEntityAttribute(Byte dbData) {
    if (dbData == null) {
      return null;
    }
    return EnumUtils.fromValue(getGenericEnumClass(), dbData);
  }

  @SuppressWarnings("unchecked")
  default Class<E> getGenericEnumClass() {
    ParameterizedType type = (ParameterizedType) getClass().getGenericInterfaces()[0];
    return (Class<E>) type.getActualTypeArguments()[0];
  }
}
