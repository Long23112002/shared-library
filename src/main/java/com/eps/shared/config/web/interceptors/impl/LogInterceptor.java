package com.eps.shared.config.web.interceptors.impl;

import com.eps.shared.config.web.interceptors.IInterceptor;
import com.eps.shared.services.ElkService;
import com.eps.shared.utils.HeaderKeys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

@Component
@RequiredArgsConstructor
public class LogInterceptor implements IInterceptor {

  private final ElkService elkService;

  @Override
  public boolean preHandle(
      HttpServletRequest request, HttpServletResponse response, Object handler) {
    long startTime = System.currentTimeMillis();
    request.setAttribute("startTime", startTime);
    return true; // Proceed with the execution chain  }
  }

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView)
      throws Exception {
    //    HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
    long startTime = (long) request.getAttribute("startTime");
    long endTime = System.currentTimeMillis();
    long timeTaken = endTime - startTime;
    elkService.whiteLogRequest(MDC.get(HeaderKeys.TRACE_ID), request, response, timeTaken);
  }
}
