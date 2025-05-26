package com.eps.shared.interfaces.services;

import com.eps.shared.models.HeaderContext;

public interface IGetService<E, ID, RES> extends IResponseMapper<E, RES>, IGetEntityService<E, ID> {

  /** Tìm entity theo ID, ném lỗi 404 nếu không tìm thấy. */
  default RES getById(HeaderContext context, ID id) {

    E entity = getEntityById(context, id);

    return mappingResponse(context, entity);
  }
}
