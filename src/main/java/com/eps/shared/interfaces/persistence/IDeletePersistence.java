package com.eps.shared.interfaces.persistence;

public interface IDeletePersistence<E, ID> extends ICrudPersistenceProvider<E, ID> {

  default void delete(E entity) {
    getCrudRepository().delete(entity);
  }
}
