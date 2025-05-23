package com.eps.shared.config.web.method.argument.impl;

import com.eps.shared.config.web.method.argument.IMethodArgument;
import com.eps.shared.models.HeaderContext;
import com.eps.shared.models.exceptions.CommonErrorMessage;
import com.eps.shared.models.exceptions.ResponseException;
import com.eps.shared.utils.HeaderKeys;
import com.eps.shared.utils.JsonParserUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@Slf4j
public class HeaderContextResolver implements IMethodArgument {

  @Override
  public boolean supportsParameter(MethodParameter parameter) {
    return parameter.getParameterType().equals(HeaderContext.class);
  }

  @Override
  public Object resolveArgument(
      MethodParameter parameter,
      ModelAndViewContainer mavContainer,
      NativeWebRequest webRequest,
      WebDataBinderFactory binderFactory) {

    HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

    String user = request.getHeader(HeaderKeys.USER);
    try {
      HeaderContext context = new HeaderContext();

      context.setExtraData(JsonParserUtils.toMap(user));

      context.setTaiKhoanId(context.getExtraData().get("taiKhoanId"));
      context.setTaiKhoan(context.getExtraData().get("taiKhoan"));
      context.setTen(context.getExtraData().get("ten"));
      context.setEmail(context.getExtraData().get("email"));

      log.info("Triển khai ánh xạ dữ liệu user vào HeaderContext thành công");
      return context;
    } catch (Exception e) {

      throw new ResponseException(HttpStatus.FORBIDDEN, CommonErrorMessage.FORBIDDEN);
    }
  }
}
