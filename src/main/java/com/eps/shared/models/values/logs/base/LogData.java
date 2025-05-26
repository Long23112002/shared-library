package com.eps.shared.models.values.logs.base;

import com.eps.shared.models.enums.LogType;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class LogData {
  protected String traceId;
  protected LogType type;

  @JsonProperty("created_at")
  protected LocalDateTime createdAt;
}
