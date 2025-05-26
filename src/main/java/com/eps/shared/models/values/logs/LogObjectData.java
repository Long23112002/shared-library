package com.eps.shared.models.values.logs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.eps.shared.models.enums.LogAction;
import com.eps.shared.models.enums.LogStatus;
import com.eps.shared.models.values.logs.base.LogData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LogObjectData extends LogData {
  @JsonProperty("user_id")
  private Long userId;

  private Integer version;

  @JsonProperty("object_name")
  private String objectName;

  @JsonProperty("object_id")
  private Long objectId;

  private LogAction action;
  private Object data;
  private LogStatus status;
}
