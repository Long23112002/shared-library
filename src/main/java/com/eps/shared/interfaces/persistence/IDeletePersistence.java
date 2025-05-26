package com.eps.shared.interfaces.persistence;

import org.springframework.transaction.annotation.Transactional;

public interface IDeletePersistence<E, ID> extends ICrudPersistenceProvider<E, ID> {

  @Transactional
  default void delete(E entity) {
    getCrudRepository().delete(entity);
  }
}
