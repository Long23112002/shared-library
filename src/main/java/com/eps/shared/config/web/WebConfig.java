package com.eps.shared.config.web;

import com.eps.shared.config.web.interceptors.InterceptorFactory;
import com.eps.shared.config.web.method.argument.MethodArgumentFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final InterceptorFactory interceptor;
  private final MethodArgumentFactory methodArgument;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    WebMvcConfigurer.super.addArgumentResolvers(resolvers);
    resolvers.addAll(methodArgument.getMethodArgumentList());
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {}

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    interceptor
        .getMethodArgumentList()
        .forEach(interceptor -> registry.addInterceptor(interceptor).addPathPatterns("/**"));
  }
}
