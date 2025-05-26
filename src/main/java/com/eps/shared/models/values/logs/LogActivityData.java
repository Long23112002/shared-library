package com.eps.shared.models.values.logs;

import com.eps.shared.models.enums.LogAction;
import com.eps.shared.models.values.logs.base.LogData;
import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LogActivityData extends LogData {
  private Long userId;
  private Long mainId;
  private Boolean isSystem = false;
  private LogAction action;
  private Object relatedObject;
  //  private String ipAddress;
  //  private String device;
  //  private String location;
  private Object performedBy;
  private Map<String, Object> extraFields;
}
