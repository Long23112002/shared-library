package com.eps.shared.annotations;

import com.eps.shared.constants.SystemConstant;
import com.eps.shared.models.exceptions.CommonErrorMessage;
import com.eps.shared.models.exceptions.ResponseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class RolesAllowedAspect {
  private static final Logger logger = LoggerFactory.getLogger(RolesAllowedAspect.class);
  private final ObjectMapper objectMapper;

  public RolesAllowedAspect(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Around("@annotation(rolesAllowed)")
  public Object checkUserRoles(ProceedingJoinPoint joinPoint, RolesAllowed rolesAllowed)
      throws Throwable {
    // Lấy request hiện tại
    HttpServletRequest request = getCurrentRequest();

    // Xác thực và lấy danh sách vai trò người dùng
    Set<String> userRoles = validateRequestAndRoles(rolesAllowed, request);

    // Kiểm tra vai trò được phép
    Set<String> allowedRoles = validateAndGetAllowedRoles(rolesAllowed);
    if (Collections.disjoint(userRoles, allowedRoles)) {
      logger.warn(
          "Phương thức {}: Vai trò người dùng {} không khớp với vai trò được phép {}",
          joinPoint.getSignature().toShortString(),
          userRoles,
          allowedRoles);
      throw new ResponseException(HttpStatus.FORBIDDEN, CommonErrorMessage.FORBIDDEN.val());
    }

    // Tiếp tục thực thi phương thức gốc
    return joinPoint.proceed();
  }

  private Set<String> validateRequestAndRoles(
      RolesAllowed rolesAllowed, HttpServletRequest request) {
    // Kiểm tra annotation
    validateAnnotation(rolesAllowed);

    // Kiểm tra header người dùng
    String userHeader = request.getHeader(SystemConstant.USER_HEADER);
    if (userHeader == null || userHeader.isEmpty()) {
      logger.warn("Header {} bị thiếu hoặc rỗng", SystemConstant.USER_HEADER);
      throw new ResponseException(HttpStatus.FORBIDDEN, CommonErrorMessage.FORBIDDEN.val());
    }

    // Phân tích header người dùng
    JsonNode userNode = parseUserHeader(userHeader);
    Set<String> userRoles = extractUserRoles(userNode);

    // Kiểm tra danh sách vai trò
    if (userRoles.isEmpty()) {
      logger.warn("Không tìm thấy vai trò hợp lệ trong header người dùng");
      throw new ResponseException(HttpStatus.FORBIDDEN, CommonErrorMessage.FORBIDDEN.val());
    }

    return userRoles;
  }

  private void validateAnnotation(RolesAllowed rolesAllowed) {
    if (rolesAllowed == null) {
      logger.error("Annotation RolesAllowed không hợp lệ: annotation bị null");
      throw new ResponseException(
          HttpStatus.INTERNAL_SERVER_ERROR, CommonErrorMessage.INTERNAL_SERVER.val());
    }

    String[] allowedRolesArray = rolesAllowed.value();
    if (allowedRolesArray.length == 0
        || Arrays.stream(allowedRolesArray)
            .allMatch(role -> role == null || role.trim().isEmpty())) {
      logger.error(
          "Annotation RolesAllowed không hợp lệ: không có vai trò hợp lệ được chỉ định, roles={}",
          Arrays.toString(allowedRolesArray));
      throw new ResponseException(
          HttpStatus.INTERNAL_SERVER_ERROR, CommonErrorMessage.INTERNAL_SERVER.val());
    }
  }

  private Set<String> validateAndGetAllowedRoles(RolesAllowed rolesAllowed) {
    return Arrays.stream(rolesAllowed.value())
        .filter(role -> role != null && !role.trim().isEmpty())
        .collect(Collectors.toCollection(HashSet::new));
  }

  private HttpServletRequest getCurrentRequest() {
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
    return attributes.getRequest();
  }

  private JsonNode parseUserHeader(String userHeader) {
    try {
      return objectMapper.readTree(userHeader);
    } catch (JsonProcessingException e) {
      logger.error("Lỗi khi phân tích header người dùng: {}", userHeader, e);
      throw new ResponseException(HttpStatus.FORBIDDEN, CommonErrorMessage.FORBIDDEN.val());
    }
  }

  private Set<String> extractUserRoles(JsonNode userNode) {
    JsonNode rolesNode = userNode.get(SystemConstant.ROLES_FIELD);
    if (rolesNode == null || !rolesNode.isArray()) {
      logger.warn("Trường vai trò không hợp lệ hoặc bị thiếu trong header người dùng");
      throw new ResponseException(HttpStatus.FORBIDDEN, CommonErrorMessage.FORBIDDEN.val());
    }

    return StreamSupport.stream(rolesNode.spliterator(), false)
        .map(JsonNode::asText)
        .filter(role -> !role.isEmpty())
        .collect(Collectors.toCollection(HashSet::new));
  }
}
