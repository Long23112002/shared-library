package com.eps.shared.interfaces.persistence;

import com.eps.shared.interfaces.repository.IBaseRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

public interface IBasePersistence<E, ID> extends IJpaGetAllPersistence<E>, ICrudPersistence<E, ID> {
  IBaseRepository<E, ID> getIBaseRepository();

  @Override
  default CrudRepository<E, ID> getCrudRepository() {
    return getIBaseRepository();
  }

  @Override
  default JpaSpecificationExecutor<E> getJpaSpecificationExecutor() {
    return getIBaseRepository();
  }
}
