package com.eps.shared.interfaces.services;

import com.eps.shared.interfaces.persistence.*;

/**
 * Giao diện chuẩn hóa cho thao tác CRUD, có thể tái sử dụng cho nhiều entity khác nhau.
 *
 * @param <E> Entity class (VD: User, Product)
 * @param <ID> Kiểu ID (VD: Long, UUID)
 * @param <REQ> Request DTO từ client (VD: UserCreateRequest)
 */
public interface ICrudService<E, ID, RES, REQ>
    extends ICreateService<E, ID, RES, REQ>,
        IUpdateService<E, ID, RES, REQ>,
        IGetService<E, ID, RES>,
        IDeleteService<E, ID>,
        IPersistenceProvider<E, ID> {

  @Override
  default ICreatePersistence<E, ID> getCreatePersistence() {
    return getCrudPersistence();
  }

  @Override
  default IDeletePersistence<E, ID> getDeletePersistence() {
    return getCrudPersistence();
  }

  @Override
  default IUpdatePersistence<E, ID> getUpdatePersistence() {
    return getCrudPersistence();
  }

  @Override
  default IGetPersistence<E, ID> getGetPersistence() {
    return getCrudPersistence();
  }
}
