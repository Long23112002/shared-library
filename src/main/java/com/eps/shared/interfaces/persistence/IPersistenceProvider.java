package com.eps.shared.interfaces.persistence;

public interface IPersistenceProvider<E, ID> {

  ICrudPersistence<E, ID> getCrudPersistence();
}
