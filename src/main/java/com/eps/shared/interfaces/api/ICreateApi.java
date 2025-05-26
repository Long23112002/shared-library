package com.eps.shared.interfaces.api;

import com.eps.shared.interfaces.services.ICreateService;
import com.eps.shared.models.HeaderContext;
import com.eps.shared.models.values.requests.OnCreate;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface ICreateApi<E, ID, RES, REQ> {

  ICreateService<E, ID, RES, REQ> getCreateService();

  /**
   * Tạo mới một entity.
   *
   * @param context HeaderContext tự động resolve từ request (thường chứa: token, locale, userId...)
   * @param request Dữ liệu gửi lên từ client để tạo entity
   * @return RES Đối tượng trả về sau khi tạo (thường là bản ghi vừa tạo)
   */
  @PostMapping
  default ResponseEntity<RES> create(
      @Parameter(hidden = true) HeaderContext context,
      @RequestHeader Map<String, Object> headers,
      @RequestBody @Validated(OnCreate.class) REQ request) {
    if (getCreateService() == null) {
      return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
    }
    return ResponseEntity.ok(getCreateService().create(context, request));
  }
}
