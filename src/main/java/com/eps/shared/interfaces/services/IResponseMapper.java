package com.eps.shared.interfaces.services;

import com.eps.shared.models.HeaderContext;
import com.eps.shared.utils.FnCommon;
import com.eps.shared.utils.GenericTypeUtils;

public interface IResponseMapper<E, RES> {

  default RES mappingResponse(HeaderContext context, E entity) {

    RES res = GenericTypeUtils.getNewInstance(this);

    FnCommon.copyProperties(res, entity);

    return res;
  }
}
