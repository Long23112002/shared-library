package com.eps.shared.interfaces.persistence;

public interface IGetPersistence<E, ID> extends ICrudPersistenceProvider<E, ID> {
  default E getById(ID id) {

    return getCrudRepository().findById(id).orElse(null);
  }
}
