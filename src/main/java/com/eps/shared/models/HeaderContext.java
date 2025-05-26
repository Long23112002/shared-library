package com.eps.shared.models;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class HeaderContext {
  private String taiKhoanId;
  private String ten;
  private String taiKhoan;
  private String email;
  private String traceId;

  private Map<String, String> extraData = new HashMap<>();
}
