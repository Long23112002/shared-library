package com.eps.shared.models.values.logs;

import com.eps.shared.models.enums.LogAction;
import com.eps.shared.models.values.logs.base.LogData;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LogEventData extends LogData {
  private Long userId;
  private Long mainId;
  private Boolean isSystem = false;
  private LogAction action;
  private String objectName;
  private Object preValue;
  private Object value;
}
