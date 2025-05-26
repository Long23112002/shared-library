package com.eps.shared.config.web.exceptions;

import com.eps.shared.models.exceptions.CommonErrorMessage;
import com.eps.shared.models.exceptions.ErrorResponse;
import com.eps.shared.models.exceptions.ResponseException;
import com.eps.shared.services.ElkService;
import com.eps.shared.services.SendMessageTelegram;
import com.eps.shared.utils.HeaderKeys;
import com.eps.shared.utils.KeywordReplacer;
import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final ElkService elkService;
  private final SendMessageTelegram sendMessageTelegram;

  // Handle validation
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    BindingResult bindingResult = ex.getBindingResult();

    List<ErrorResponse.FieldErrorDetail> fieldErrors =
        bindingResult.getFieldErrors().stream()
            .map(
                fieldError ->
                    new ErrorResponse.FieldErrorDetail(
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getRejectedValue()))
            .collect(Collectors.toList());

    ErrorResponse response = new ErrorResponse();
    response.setTimestamp(LocalDateTime.now());
    response.setStatus(HttpStatus.BAD_REQUEST.value());
    response.setMessageCode(HttpStatus.BAD_REQUEST.name());
    response.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
    response.setMessage(CommonErrorMessage.VALIDATION_FAILED.val());
    response.setPath(request.getRequestURI());
    response.setErrors(fieldErrors);
    response.setTraceId(getTraceId());

    return ResponseEntity.badRequest().body(response);
  }

  // Handle bussiness error
  @ExceptionHandler(ResponseException.class)
  public ResponseEntity<ErrorResponse> handleBusinessException(
      ResponseException ex, HttpServletRequest request) {
    ErrorResponse response = new ErrorResponse();
    response.setTimestamp(LocalDateTime.now());
    response.setStatus(ex.getStatusCode().value());
    response.setMessageCode(ex.getStatusCode().name());
    response.setError(HttpStatus.valueOf(ex.getStatusCode().value()).getReasonPhrase());
    response.setMessage(ex.getMessage());
    response.setPath(request.getRequestURI());
    response.setTraceId(getTraceId());

    return ResponseEntity.status(ex.getStatusCode()).body(response);
  }

  @ExceptionHandler(FeignException.class)
  public ResponseEntity<ErrorResponse> handleFeignException(
      FeignException ex, HttpServletRequest request) {
    ErrorResponse response = new ErrorResponse();
    response.setTimestamp(LocalDateTime.now());
    response.setStatus(ex.status());
    response.setMessageCode(HttpStatus.valueOf(ex.status()).name());
    response.setError(HttpStatus.valueOf(ex.status()).getReasonPhrase());
    response.setMessage(ex.getMessage());
    response.setPath(request.getRequestURI());

    elkService.whiteLogException(getTraceId(), response, request);
    sendNotification(ex, request);
    response.setTraceId(getTraceId());

    return ResponseEntity.status(ex.status()).body(response);
  }

  //   Handle general error
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneralException(
      Exception ex, HttpServletRequest request) {
    ErrorResponse response = new ErrorResponse();
    response.setTimestamp(LocalDateTime.now());
    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    response.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
    response.setMessageCode(HttpStatus.INTERNAL_SERVER_ERROR.name());

    response.setMessage(ex.getMessage());

    response.setPath(request.getRequestURI());

    elkService.whiteLogException(getTraceId(), response, request);
    sendNotification(ex, request);

    response.setMessage(CommonErrorMessage.INTERNAL_SERVER.val());
    response.setTraceId(getTraceId());
    return ResponseEntity.internalServerError().body(response);
  }

  // Handle sort field
  @ExceptionHandler(PropertyReferenceException.class)
  public ResponseEntity<ErrorResponse> handleGeneralException(
      PropertyReferenceException ex, HttpServletRequest request) {
    ErrorResponse response = new ErrorResponse();
    response.setTimestamp(LocalDateTime.now());
    response.setStatus(HttpStatus.BAD_REQUEST.value());
    response.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
    response.setMessageCode(HttpStatus.BAD_REQUEST.name());
    response.setMessage(
        KeywordReplacer.replaceKeywords(
            CommonErrorMessage.FIELD_CANT_SORT.val(),
            new HashMap<>() {
              {
                put("fieldname", ex.getPropertyName());
              }
            }));
    response.setPath(request.getRequestURI());

    response.setTraceId(getTraceId());

    return ResponseEntity.badRequest().body(response);
  }

  public String getTraceId() {
    return MDC.get(HeaderKeys.TRACE_ID);
  }

  private void sendNotification(Exception ex, HttpServletRequest request) {
    try {
      String logLevel = "ERROR";
      StringBuilder msg = new StringBuilder();
      if (ex instanceof ResponseException errorResponse) {
        if (errorResponse.getStatusCode().value() < 500) {
          return;
        } else {
          msg.append("<b>ERROR</b> : ")
              .append(ex.getMessage())
              .append(" (")
              .append(errorResponse.getStatusCode().value())
              .append(") \n");
        }
      } else {
        msg.append("<b>ERROR</b> : ").append(ex.getMessage()).append(" \n");
      }
      try {
        msg.append("<b>METHOD</b> : ").append(request.getMethod()).append(" \n");
      } catch (Exception ignored) {
      }
      try {
        msg.append("<b>URI</b> : ").append(request.getRequestURL().toString()).append(" \n");
      } catch (Exception ignored) {
      }
      try {
        msg.append("<b>BODY</b> : ").append("");
      } catch (Exception ignored) {
      }
      sendMessageTelegram.send(getTraceId(), msg.toString());
    } catch (Exception ignore) {
    }
  }
}
