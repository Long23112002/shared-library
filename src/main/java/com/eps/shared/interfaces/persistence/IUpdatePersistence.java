package com.eps.shared.interfaces.persistence;

import com.eps.shared.models.HeaderContext;
import com.eps.shared.utils.GenericTypeUtils;
import jakarta.transaction.Transactional;
import java.util.function.BiConsumer;

public interface IUpdatePersistence<E, ID> extends ICrudPersistenceProvider<E, ID> {

  @Transactional
  default E update(HeaderContext context, ID id, E entity) {

    return update(context, id, entity, this::mappingUpdateAuditingEntity);
  }

  @Transactional
  default E update(
      HeaderContext context,
      ID id,
      E entity,
      BiConsumer<HeaderContext, E> mappingUpdateAuditingEntity) {

    if (mappingUpdateAuditingEntity != null) {
      mappingUpdateAuditingEntity.accept(context, entity);
    }

    return getCrudRepository().save(entity);
  }

  default void mappingUpdateAuditingEntity(HeaderContext context, E entity) {
    if (context != null) {
      GenericTypeUtils.updateData(entity, "nguoiChinhSua", context.getTen());
    }
  }
}
