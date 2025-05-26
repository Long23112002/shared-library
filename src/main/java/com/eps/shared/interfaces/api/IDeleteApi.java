package com.eps.shared.interfaces.api;

import com.eps.shared.interfaces.services.IDeleteService;
import com.eps.shared.models.HeaderContext;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

public interface IDeleteApi<E, ID> {

  IDeleteService<E, ID> getDeleteService();

  /**
   * Xoá một entity theo ID.
   *
   * @param context HeaderContext từ request
   * @param id ID của entity cần xoá
   */
  @DeleteMapping(path = "/{id}")
  default ResponseEntity<?> delete(
      @Parameter(hidden = true) HeaderContext context,
      @RequestHeader Map<String, Object> headers,
      @PathVariable(name = "id") ID id) {
    if (getDeleteService() == null) {
      return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }
    getDeleteService().delete(context, id);
    return ResponseEntity.noContent().build();
  }
}
