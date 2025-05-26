package com.eps.shared.config.feign.client;

import com.eps.shared.utils.HeaderKeys;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.slf4j.MDC;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
public class FeignClientConfig {

  @Bean
  public RequestInterceptor requestInterceptor() {
    return new RequestInterceptor() {
      @Override
      public void apply(RequestTemplate template) {
        template.header(HeaderKeys.TRACE_ID, MDC.get(HeaderKeys.TRACE_ID));
      }
    };
  }

  @Bean
  Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL; // Phải FULL để log body
  }
}
