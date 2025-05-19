package com.eps.shared.interfaces.api;

import com.eps.shared.interfaces.services.IBaseService;
import com.eps.shared.interfaces.services.ICrudService;
import com.eps.shared.interfaces.services.IGetAllService;

public interface IBaseApi<E, ID, RES, REQ, PAGE_RES>
    extends IGetAllApi<E, PAGE_RES>, ICrudApi<E, ID, RES, REQ> {

  IBaseService<E, ID, RES, REQ, PAGE_RES> getService();

  @Override
  default ICrudService<E, ID, RES, REQ> getCrudService() {
    return getService();
  }

  @Override
  default IGetAllService<E, PAGE_RES> getGetAllService() {
    return getService();
  }
}
