package com.eps.shared.interfaces.persistence;

import com.eps.shared.models.HeaderContext;
import com.eps.shared.utils.GenericTypeUtils;
import com.github.f4b6a3.uuid.UuidCreator;
import jakarta.transaction.Transactional;
import java.util.function.BiConsumer;

public interface ICreatePersistence<E, ID> extends ICrudPersistenceProvider<E, ID> {

  @Transactional
  default E create(HeaderContext context, E model) {

    return create(context, model, this::mappingCreateAuditingEntity);
  }

  @Transactional
  default E create(
      HeaderContext context, E entity, BiConsumer<HeaderContext, E> mappingCreateAuditingEntity) {

    if (mappingCreateAuditingEntity != null) {
      mappingCreateAuditingEntity.accept(context, entity);
    }
    return getCrudRepository().save(entity);
  }

  default void mappingCreateAuditingEntity(HeaderContext context, E entity) {
    if (context != null) {
      GenericTypeUtils.updateData(entity, "id", UuidCreator.getTimeOrderedEpoch());
      GenericTypeUtils.updateData(entity, "nguoiTao", context.getTen());
      GenericTypeUtils.updateData(entity, "nguoiChinhSua", context.getTen());
    }
  }
}
