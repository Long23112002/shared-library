package com.eps.shared.utils;

import org.springframework.http.HttpHeaders;

public class HeaderKeys {

  public static final String USER = "X-User";
  public static final String TRACE_ID = "X-Trace-Id";
  public static final String NAME = "X-User-Name";
  public static final String AUTHORIZATION = HttpHeaders.AUTHORIZATION.toLowerCase();
}
