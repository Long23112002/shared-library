// package com.eps.shared.config.web.exceptions;
//
// import com.eps.shared.models.exceptions.CommonErrorMessage;
// import com.eps.shared.models.exceptions.ErrorResponse;
// import com.eps.shared.models.exceptions.ResponseException;
// import com.eps.shared.utils.KeywordReplacer;
// import jakarta.servlet.http.HttpServletRequest;
// import java.time.LocalDateTime;
// import java.util.HashMap;
// import java.util.List;
// import java.util.stream.Collectors;
// import org.springframework.data.mapping.PropertyReferenceException;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.validation.BindingResult;
// import org.springframework.web.bind.MethodArgumentNotValidException;
// import org.springframework.web.bind.annotation.ExceptionHandler;
// import org.springframework.web.bind.annotation.RestControllerAdvice;
//
// @RestControllerAdvice
// public class GlobalExceptionHandler {
//
//  // Handle validation
//  @ExceptionHandler(MethodArgumentNotValidException.class)
//  public ResponseEntity<ErrorResponse> handleValidationErrors(
//      MethodArgumentNotValidException ex, HttpServletRequest request) {
//    BindingResult bindingResult = ex.getBindingResult();
//
//    List<ErrorResponse.FieldErrorDetail> fieldErrors =
//        bindingResult.getFieldErrors().stream()
//            .map(
//                fieldError ->
//                    new ErrorResponse.FieldErrorDetail(
//                        fieldError.getField(),
//                        fieldError.getDefaultMessage(),
//                        fieldError.getRejectedValue()))
//            .collect(Collectors.toList());
//
//    ErrorResponse response = new ErrorResponse();
//    response.setTimestamp(LocalDateTime.now());
//    response.setStatus(HttpStatus.BAD_REQUEST.value());
//    response.setMessageCode(HttpStatus.BAD_REQUEST.name());
//    response.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
//    response.setMessage(CommonErrorMessage.VALIDATION_FAILED.val());
//    response.setPath(request.getRequestURI());
//    response.setErrors(fieldErrors);
//
//    return ResponseEntity.badRequest().body(response);
//  }
//
//  // Handle bussiness error
//  @ExceptionHandler(ResponseException.class)
//  public ResponseEntity<ErrorResponse> handleBusinessException(
//      ResponseException ex, HttpServletRequest request) {
//    ErrorResponse response = new ErrorResponse();
//    response.setTimestamp(LocalDateTime.now());
//    response.setStatus(ex.getStatusCode().value());
//    response.setMessageCode(ex.getStatusCode().name());
//    response.setError(HttpStatus.valueOf(ex.getStatusCode().value()).getReasonPhrase());
//    response.setMessage(ex.getMessage());
//    response.setPath(request.getRequestURI());
//
//    return ResponseEntity.status(ex.getStatusCode()).body(response);
//  }
//
//  // Handle general error
//  //  @ExceptionHandler(Exception.class)
//  //  public ResponseEntity<ErrorResponse> handleGeneralException(
//  //      Exception ex, HttpServletRequest request) {
//  //    ErrorResponse response = new ErrorResponse();
//  //    response.setTimestamp(LocalDateTime.now());
//  //    response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//  //    response.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
//  //    response.setMessage(CommonErrorMessage.INTERNAL_SERVER.val());
//  //    response.setMessageCode(HttpStatus.INTERNAL_SERVER_ERROR.name());
//  //    response.setPath(request.getRequestURI());
//  //
//  //    return ResponseEntity.internalServerError().body(response);
//  //  }
//
//  // Handle sort field
//  @ExceptionHandler(PropertyReferenceException.class)
//  public ResponseEntity<ErrorResponse> handleGeneralException(
//      PropertyReferenceException ex, HttpServletRequest request) {
//    ErrorResponse response = new ErrorResponse();
//    response.setTimestamp(LocalDateTime.now());
//    response.setStatus(HttpStatus.BAD_REQUEST.value());
//    response.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
//    response.setMessageCode(HttpStatus.BAD_REQUEST.name());
//    response.setMessage(
//        KeywordReplacer.replaceKeywords(
//            CommonErrorMessage.FIELD_CANT_SORT.val(),
//            new HashMap<>() {
//              {
//                put("fieldname", ex.getPropertyName());
//              }
//            }));
//    response.setPath(request.getRequestURI());
//
//    return ResponseEntity.badRequest().body(response);
//  }
// }
