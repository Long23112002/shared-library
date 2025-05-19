package com.eps.shared.interfaces.services;

import com.eps.shared.interfaces.persistence.IBasePersistence;
import com.eps.shared.interfaces.persistence.ICrudPersistence;
import com.eps.shared.interfaces.persistence.IJpaGetAllPersistence;

public interface IBaseService<E, ID, RES, REQ, PAGE_RES>
    extends ICrudService<E, ID, RES, REQ>, IGetAllService<E, PAGE_RES> {

  IBasePersistence<E, ID> getPersistence();

  @Override
  default ICrudPersistence<E, ID> getCrudPersistence() {
    return getPersistence();
  }

  @Override
  default IJpaGetAllPersistence<E> getJpaGetAllPersistence() {
    return getPersistence();
  }
}
