package com.eps.shared.interfaces.api;

import com.eps.shared.interfaces.services.IGetService;
import com.eps.shared.models.HeaderContext;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

public interface IGetApi<E, ID, RES> {

  IGetService<E, ID, RES> getGetService();

  /**
   * Lấy thông tin một entity theo ID.
   *
   * @param context HeaderContext từ request
   * @param id ID của entity cần lấy
   * @return RES Thông tin entity
   */
  @GetMapping(path = "/{id}")
  default RES getById(
      @Parameter(hidden = true) HeaderContext context,
      @RequestHeader Map<String, Object> headers,
      @Parameter(description = "ID of the user to retrieve", required = true)
          @PathVariable(name = "id")
          ID id) {
    if (getGetService() == null) {
      return null;
    }
    return getGetService().getById(context, id);
  }
}
