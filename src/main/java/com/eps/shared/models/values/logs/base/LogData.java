package com.eps.shared.models.values.logs.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.eps.shared.models.enums.LogType;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class LogData {
  protected LogType type;

  @JsonProperty("created_at")
  protected LocalDateTime createdAt;
}
