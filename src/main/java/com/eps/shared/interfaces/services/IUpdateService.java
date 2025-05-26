package com.eps.shared.interfaces.services;

import com.eps.shared.interfaces.persistence.IUpdatePersistence;
import com.eps.shared.models.HeaderContext;
import com.eps.shared.utils.FnCommon;
import com.eps.shared.utils.functions.PentaConsumer;
import com.eps.shared.utils.functions.QuadConsumer;
import java.util.function.BiFunction;
import org.apache.logging.log4j.util.TriConsumer;
import org.springframework.transaction.annotation.Transactional;

public interface IUpdateService<E, ID, RES, REQ>
    extends IResponseMapper<E, RES>, IGetEntityService<E, ID> {

  IUpdatePersistence<E, ID> getUpdatePersistence();

  /**
   * Cập nhật entity hiện có theo ID, có validate và mapping tuỳ chỉnh.
   *
   * @param context Header context
   * @param id ID của entity
   * @param request Request chứa thông tin cần cập nhật
   * @param validationHandler Hàm kiểm tra hợp lệ trước khi update
   * @param mappingHandler Hàm mapping dữ liệu request vào entity
   * @return Entity đã cập nhật
   */
  @Transactional
  default RES update(
      HeaderContext context,
      ID id,
      REQ request,
      QuadConsumer<HeaderContext, ID, E, REQ> validationHandler,
      TriConsumer<HeaderContext, E, REQ> mappingHandler,
      PentaConsumer<HeaderContext, E, E, ID, REQ> postHandler,
      BiFunction<HeaderContext, E, RES> mappingResponseHandler) {

    if (getUpdatePersistence() == null) {
      throw new IllegalArgumentException("updatePersistence must not be null");
    }
    E entity = getEntityById(context, id); // Lấy entity từ DB, nếu không có thì ném lỗi 404

    E originalEntity = (E) FnCommon.copyProperties(entity.getClass(), entity);

    if (validationHandler != null) {
      validationHandler.accept(context, id, entity, request); // Kiểm tra hợp lệ
    }

    if (mappingHandler != null) {
      mappingHandler.accept(context, entity, request); // Gọi hàm mapping tùy chỉnh
    }

    entity = getUpdatePersistence().update(context, id, entity);

    if (postHandler != null) {
      postHandler.accept(context, originalEntity, entity, id, request);
    }

    if (mappingResponseHandler == null) {
      throw new IllegalArgumentException("mappingResponseHandler must not be null");
    }

    return mappingResponseHandler.apply(context, entity); // Lưu lại vào DB
  }

  @Transactional
  /** Cập nhật mặc định nếu không cần validate/mapping riêng. */
  default RES update(HeaderContext context, ID id, REQ request) {

    if (getUpdatePersistence() == null) {
      throw new IllegalArgumentException("updatePersistence must not be null");
    }

    return update(
        context,
        id,
        request,
        this::validateUpdateRequest,
        this::mappingUpdateEntity,
        this::postUpdateHandler,
        this::mappingResponse);
  }

  // Validate mặc định khi update
  default void validateUpdateRequest(HeaderContext context, ID id, E entity, REQ request) {}

  // Mapping mặc định khi update
  default void mappingUpdateEntity(HeaderContext context, E entity, REQ request) {
    FnCommon.copyProperties(entity, request); // Gán dữ liệu chung từ request
  }

  default void postUpdateHandler(
      HeaderContext context, E originalEntity, E entity, ID id, REQ request) {}
}
