package com.eps.shared.models.exceptions;

import com.eps.shared.utils.KeywordReplacer;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
public class ResponseException extends RuntimeException {
  HttpStatus statusCode;
  String messageCode;

  public ResponseException(HttpStatus statusCode, BaseErrorMessage msg) {
    super(msg.val());
    this.statusCode = statusCode;
    messageCode = msg.toString();
  }

  public ResponseException(HttpStatus statusCode, BaseErrorMessage msg, Map<String, String> data) {
    super(KeywordReplacer.replaceKeywords(msg.val(), data));
    this.statusCode = statusCode;
    messageCode = msg.toString();
  }

  public ResponseException(HttpStatus statusCode, String msg) {
    super(msg);
    this.statusCode = statusCode;
  }
}
