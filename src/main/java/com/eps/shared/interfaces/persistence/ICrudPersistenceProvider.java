package com.eps.shared.interfaces.persistence;

import org.springframework.data.repository.CrudRepository;

public interface ICrudPersistenceProvider<E, ID> {

  CrudRepository<E, ID> getCrudRepository();
}
