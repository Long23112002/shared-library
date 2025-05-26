package com.eps.shared.interfaces.services;

import com.eps.shared.interfaces.persistence.IDeletePersistence;
import com.eps.shared.models.HeaderContext;
import org.apache.logging.log4j.util.TriConsumer;
import org.springframework.transaction.annotation.Transactional;

public interface IDeleteService<E, ID> extends IGetEntityService<E, ID> {

    IDeletePersistence<E, ID> getDeletePersistence();

    /**
     * Xóa entity theo ID, có thể validate trước khi xóa.
     */
    @Transactional
    default void delete(
            HeaderContext context, ID id,
            TriConsumer<HeaderContext, ID, E> validationHandler,
            TriConsumer<HeaderContext, ID, E> postDeleteHandler
    ) {
        E entity = getEntityById(context, id); // Lấy entity từ DB

        if (validationHandler != null) {
            validationHandler.accept(context, id, entity); // Kiểm tra hợp lệ trước khi xóa
        }
        postDeleteHandler.accept(context, id, entity);
        getDeletePersistence().delete(entity); // Xóa khỏi DB
    }

    @Transactional
    /** Hàm xóa mặc định không cần validate riêng. */
    default void delete(HeaderContext context, ID id) {
        if (getDeletePersistence() == null) {
            throw new IllegalArgumentException("deletePersistence must not be null");
        }

        delete(context,
                id,
                this::validateDelete,
                this::postDeleteHandler);
    }

    // Hàm validate mặc định khi xóa
    default void validateDelete(HeaderContext context, ID id, E entity) {
    }

    default void postDeleteHandler(HeaderContext context, ID id, E entity) {
    }
}
