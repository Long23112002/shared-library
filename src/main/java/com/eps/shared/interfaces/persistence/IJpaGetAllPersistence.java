package com.eps.shared.interfaces.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface IJpaGetAllPersistence<E> {

  JpaSpecificationExecutor<E> getJpaSpecificationExecutor();

  default Page<E> getAll(Specification<E> specification, Pageable pageable) {

    if (getJpaSpecificationExecutor() == null) {
      return null;
    }

    return getJpaSpecificationExecutor().findAll(specification, pageable);
  }
}
