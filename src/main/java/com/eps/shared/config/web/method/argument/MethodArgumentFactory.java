package com.eps.shared.config.web.method.argument;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Getter
@Component
public class MethodArgumentFactory {

  public final List<IMethodArgument> methodArgumentList;
}
