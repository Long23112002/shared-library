package com.eps.shared.config.web.filters;

import com.eps.shared.utils.HeaderKeys;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class TraceIdFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    try {
      // Lấy traceId từ header nếu có, hoặc tự tạo
      String traceId = ((HttpServletRequest) request).getHeader(HeaderKeys.TRACE_ID);
      if (traceId == null || traceId.isEmpty()) {
        traceId = UUID.randomUUID().toString();
      }
      MDC.put(HeaderKeys.TRACE_ID, traceId); // gắn vào MDC
      chain.doFilter(request, response);
    } finally {
      MDC.remove(HeaderKeys.TRACE_ID); // tránh memory leak
    }
  }
}
