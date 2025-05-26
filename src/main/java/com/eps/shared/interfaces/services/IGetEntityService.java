package com.eps.shared.interfaces.services;

import com.eps.shared.interfaces.persistence.IGetPersistence;
import com.eps.shared.models.HeaderContext;
import jakarta.validation.constraints.NotNull;

public interface IGetEntityService<E, ID> {

  @NotNull
  IGetPersistence<E, ID> getGetPersistence();

  /** Tìm entity theo ID, ném lỗi 404 nếu không tìm thấy. */
  default E getEntityById(HeaderContext context, ID id) {
    if (getGetPersistence() == null) {
      throw new RuntimeException("getGetPersistence() has not been called");
    }
    return getGetPersistence().getById(id);
  }
}
