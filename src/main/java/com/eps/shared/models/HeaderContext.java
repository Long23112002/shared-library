package com.eps.shared.models;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class HeaderContext {
  private String accountId;
  private String name;
  private String username;
  private String email;
  private String traceId;

  private Map<String, String> extraData = new HashMap<>();
}
