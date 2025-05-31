package com.eps.shared.interfaces.services;

import com.eps.shared.interfaces.persistence.ICreatePersistence;
import com.eps.shared.models.HeaderContext;
import com.eps.shared.models.enums.PositionType;
import com.eps.shared.utils.FnCommon;
import com.eps.shared.utils.GenericTypeUtils;
import java.util.function.BiFunction;
import org.apache.logging.log4j.util.TriConsumer;
import org.springframework.transaction.annotation.Transactional;

public interface ICreateService<E, ID, RES, REQ> extends IResponseMapper<E, RES> {

  ICreatePersistence<E, ID> getCreatePersistence();

  /**
   * Tạo entity mới từ request và lưu vào database. Có hỗ trợ validation và mapping tùy chỉnh.
   *
   * @param context Header context (chứa thông tin người dùng, locale, ...)
   * @param request Dữ liệu từ client gửi lên
   * @param validationCreateHandler Hàm callback kiểm tra dữ liệu đầu vào (có thể throw lỗi)
   * @param mappingEntityHandler Hàm callback để gán dữ liệu tùy chỉnh vào entity
   * @return Entity sau khi được lưu vào database
   */
  @Transactional
  default RES create(
      HeaderContext context,
      REQ request,
      TriConsumer<HeaderContext, E, REQ> validationCreateHandler,
      TriConsumer<HeaderContext, E, REQ> mappingEntityHandler,
      TriConsumer<HeaderContext, E, REQ> postHandler,
      BiFunction<HeaderContext, E, RES> mappingResponseHandler) {

    if (getCreatePersistence() == null) {
      throw new IllegalArgumentException("createPersistence must not be null");
    }

    E entity = GenericTypeUtils.getNewInstance(this, ICreateService.class, PositionType.FIRST);

    if (validationCreateHandler != null) {
      validationCreateHandler.accept(context, entity, request);
    }

    if (mappingEntityHandler != null) {
      mappingEntityHandler.accept(context, entity, request); // Gọi mapping tùy chỉnh
    }

    entity = getCreatePersistence().create(context, entity);

    postHandler.accept(context, entity, request);

    if (mappingResponseHandler == null) {
      throw new IllegalArgumentException("mappingResponseHandler must not be null");
    }
    return mappingResponseHandler.apply(context, entity); // Lưu entity vào DB
  }

  @Transactional
  /** Hàm tạo mặc định nếu không truyền vào hàm validate/mapping riêng. */
  default RES create(HeaderContext context, REQ request) {
    if (getCreatePersistence() == null) {
      throw new IllegalArgumentException("createPersistence must not be null");
    }
    return create(
        context,
        request,
        this::validateCreateRequest,
        this::mappingCreateEntity,
        this::postCreateHandler,
        this::mappingResponse);
  }

  /** Hàm validate mặc định (không làm gì) — override trong implementation nếu cần. */
  default void validateCreateRequest(HeaderContext context, E entity, REQ request) {}

  /** Hàm mapping mặc định (không làm gì) — override nếu cần xử lý riêng. */
  default void mappingCreateEntity(HeaderContext context, E entity, REQ request) {
    FnCommon.copyProperties(entity, request); // Copy các field giống nhau từ request sang entity
  }

  default void postCreateHandler(HeaderContext context, E entity, REQ request) {}
}
