package com.eps.shared.models.values.logs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.eps.shared.models.values.logs.base.LogData;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

@EqualsAndHashCode(callSuper = true)
@Data
public class LogApiData extends LogData {
  private String url;
  private HttpMethod method;
  private Object auth;
  private Object body;
  private Object response;
  private HttpStatus status;

  @JsonProperty("user_id")
  private Long userId;
}
