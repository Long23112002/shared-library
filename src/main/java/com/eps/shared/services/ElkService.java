package com.eps.shared.services;

import com.eps.shared.models.enums.LogAction;
import com.eps.shared.models.enums.LogStatus;
import com.eps.shared.models.enums.LogType;
import com.eps.shared.models.exceptions.ErrorResponse;
import com.eps.shared.models.values.logs.*;
import com.eps.shared.utils.JsonParserUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ElkService {

  @Async
  public void whiteLogObject(
      Long userId, String className, Long objectId, LogAction logAction, Object data) {
    LogObjectData logObjectData = new LogObjectData();
    logObjectData.setUserId(userId);
    logObjectData.setVersion(1);
    logObjectData.setType(LogType.OBJECT);
    logObjectData.setObjectName(className);
    logObjectData.setObjectId(objectId);
    logObjectData.setAction(logAction);
    logObjectData.setData(data);
    logObjectData.setStatus(LogStatus.SUCCESS);
    logObjectData.setCreatedAt(LocalDateTime.now());
    log.debug(JsonParserUtils.toJson(logObjectData));
  }

  @Async
  public void whiteLogException(
      String traceId, ErrorResponse exceptionResponse, HttpServletRequest request) {
    LogExceptionData logExceptionData = new LogExceptionData();
    logExceptionData.setTraceId(traceId);
    logExceptionData.setDescription(exceptionResponse.getMessage());
    logExceptionData.setTimestamp(exceptionResponse.getTimestamp());
    logExceptionData.setParams(request.getParameterMap());
    logExceptionData.setHeaders(getHeaderMap(request));
    logExceptionData.setPath(exceptionResponse.getPath());
    logExceptionData.setMessage(exceptionResponse.getMessage());
    logExceptionData.setMessageCode(exceptionResponse.getMessageCode());
    logExceptionData.setStatus(exceptionResponse.getStatus());
    logExceptionData.setType(LogType.EXCEPTION);
    logExceptionData.setCreatedAt(LocalDateTime.now());
    log.error(JsonParserUtils.toJson(logExceptionData));
  }

  @Async
  public void whiteLogRequest(
      String traceId, HttpServletRequest request, HttpServletResponse response, Long timeTaken)
      throws IOException {
    if (request.getMethod().equals("OPTIONS")) {
      return;
    }
    LogRequestData logRequestData = new LogRequestData();
    logRequestData.setTraceId(traceId);
    logRequestData.setStatus(HttpStatus.valueOf(response.getStatus()));
    logRequestData.setIntStatus(response.getStatus());
    logRequestData.setPath(request.getServletPath());
    logRequestData.setMethod(request.getMethod());
    logRequestData.setParams(request.getParameterMap());
    logRequestData.setResponse(response.getTrailerFields());
    Map<String, String> headers = getHeaderMap(request);
    logRequestData.setHeader(headers);
    logRequestData.setType(LogType.REQUEST);
    logRequestData.setCreatedAt(LocalDateTime.now());
    logRequestData.setBody(getBody(request));
    logRequestData.setTimeTaken(timeTaken);

    log.info(JsonParserUtils.toJson(logRequestData));
  }

  @Async
  public void whiteLogEventSystem(
      Long userId,
      Long enterpriseId,
      Class<?> objectName,
      LogAction action,
      Object preValue,
      Object value,
      Boolean isSystem) {
    LogEventData data = new LogEventData();
    data.setAction(action);
    data.setMainId(enterpriseId);
    data.setValue(value);
    data.setIsSystem(isSystem);
    data.setPreValue(preValue);
    data.setUserId(userId);
    data.setObjectName(objectName.getSimpleName().toUpperCase());
    data.setType(LogType.EVENT);
    data.setCreatedAt(LocalDateTime.now());
    log.info(JsonParserUtils.toJson(data));
  }

  @Async
  public void whiteLogEvent(
      Long userId,
      Long enterpriseId,
      Class<?> objectName,
      LogAction action,
      Object preValue,
      Object value) {
    LogEventData data = new LogEventData();
    data.setAction(action);
    data.setIsSystem(false);
    data.setMainId(enterpriseId);
    data.setValue(value);
    data.setPreValue(preValue);
    data.setUserId(userId);
    data.setObjectName(objectName.getSimpleName().toUpperCase());
    data.setType(LogType.EVENT);
    data.setCreatedAt(LocalDateTime.now());
    log.trace(JsonParserUtils.toJson(data));
  }

  private Map<String, String> getHeaderMap(HttpServletRequest request) {
    Map<String, String> headers = new HashMap<>();
    Enumeration<String> headerNames = request.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      String headerName = headerNames.nextElement();
      String headerValue = request.getHeader(headerName);
      headers.put(headerName, headerValue);
    }
    return headers;
  }

  private String getBody(HttpServletRequest request) throws IOException {
    StringBuilder body = new StringBuilder();

    // Optional: remove trailing newline
    String requestBody = body.toString().trim();

    return requestBody;
  }
}
