package com.eps.shared.interfaces.services;

import com.eps.shared.interfaces.persistence.IJpaGetAllPersistence;
import com.eps.shared.models.HeaderContext;
import com.eps.shared.models.enums.PositionType;
import com.eps.shared.utils.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.metamodel.Attribute;
import java.sql.Timestamp;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public interface IGetAllService<E, RES> {

  IJpaGetAllPersistence<E> getJpaGetAllPersistence();

  @SuppressWarnings("unchecked")
  default Page<RES> getAll(
      HeaderContext context,
      String search,
      Integer page,
      Integer pageSize,
      String sort,
      String filter,
      BiFunction<HeaderContext, E, RES> mappingResponseHandler) {
    Map<String, Object> filterMap = new HashMap<>();

    if (StringUtils.hasLength(filter)) {
      try {
        filterMap = JsonParserUtils.entity(filter, Map.class);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    Pageable pageable = PageableUtils.convertPageable(page, pageSize, sort);

    Specification<E> query = buildQuery(context, search, filterMap);
    Page<E> data = getJpaGetAllPersistence().getAll(query, pageable);

    return data.map(item -> mappingResponseHandler.apply(context, item));
  }

  default Page<RES> getAll(
      HeaderContext context,
      String search,
      Integer page,
      Integer pageSize,
      String sort,
      String filter) {

    return getAll(context, search, page, pageSize, sort, filter, this::mappingPageResponse);
  }

  default RES mappingPageResponse(HeaderContext context, E item) {
    RES resItem = GenericTypeUtils.getNewInstance(this, IGetAllService.class, PositionType.LAST);

    FnCommon.copyProperties(resItem, item);

    return resItem;
  }

  default Specification<E> buildQuery(
      HeaderContext context, String search, Map<String, Object> filter) {

    return (root, query, cb) -> {
      List<Predicate> predicates = new ArrayList<>();
      List<Predicate> searchPredicates =
          buildSearchQuery(root.getModel().getAttributes(), root, query, cb, search);
      List<Predicate> entityPredicates =
          buildEntityQuery(root.getModel().getAttributes(), root, query, cb, filter);
      List<Predicate> filterPredicates = buildFilterQuery(root, query, cb, filter);

      predicates.addAll(searchPredicates);
      predicates.addAll(entityPredicates);
      predicates.addAll(filterPredicates);
      return cb.and(predicates.toArray(new Predicate[0]));
    };
  }

  default List<Predicate> buildFilterQuery(
      Root<E> root, CriteriaQuery<?> query, CriteriaBuilder cb, Map<String, Object> filter) {
    return new ArrayList<>();
  }

  default List<Predicate> buildEntityQuery(
      Set<Attribute<? super E, ?>> fieldNames,
      Root<E> root,
      CriteriaQuery<?> query,
      CriteriaBuilder cb,
      Map<String, Object> filter) {

    List<Class<?>> supportedTypes =
        List.of(
            boolean.class,
            Boolean.class,
            String.class,
            Integer.class,
            int.class,
            long.class,
            Long.class,
            UUID.class,
            float.class,
            Float.class,
            double.class,
            Double.class,
            Timestamp.class,
            Enum.class);

    List<Predicate> predicates = new ArrayList<>();

    for (Attribute<? super E, ?> attribute : fieldNames) {
      String fieldName = attribute.getName();
      Object value = filter.get(fieldName);

      if (value == null) {
        continue;
      }

      Class<?> fieldType = attribute.getJavaType();

      if (fieldType.isEnum() && value instanceof String) {
        Object enumValue = EnumUtils.convertEnum(fieldType, value);
        predicates.add(cb.equal(root.get(fieldName), enumValue));
        continue;
      }

      if (!supportedTypes.contains(fieldType)) {
        continue;
      }

      if (fieldType.equals(Timestamp.class)) {
        String fromKey = ParamsKeys.getFieldName(ParamsKeys.PREFIX_FROM, fieldName);
        String toKey = ParamsKeys.getFieldName(ParamsKeys.PREFIX_TO, fieldName);

        Object fromValue = filter.get(fromKey);
        Object toValue = filter.get(toKey);

        if (fromValue != null) {
          predicates.add(cb.greaterThanOrEqualTo(root.get(fieldName), (Timestamp) fromValue));
        }

        if (toValue != null) {
          predicates.add(cb.lessThanOrEqualTo(root.get(fieldName), (Timestamp) toValue));
        }

      } else {
        predicates.add(cb.equal(root.get(fieldName), value));
      }
    }

    return predicates;
  }

  default String[] getSearchFieldNames() {

    return new String[] {"name"};
  }

  default List<Predicate> buildSearchQuery(
      Set<Attribute<? super E, ?>> fieldNames,
      Root<E> root,
      CriteriaQuery<?> query,
      CriteriaBuilder cb,
      String search) {
    String[] searchFieldNames = getSearchFieldNames();
    List<Predicate> searchPredicates = new ArrayList<>();

    Set<String> fieldNameSet =
        fieldNames.stream().map(Attribute::getName).collect(Collectors.toSet());

    for (String fieldName : searchFieldNames) {
      boolean isSearch = fieldNameSet.contains(fieldName);
      if (isSearch && StringUtils.hasLength(search)) {
        Predicate searchPredicate =
            cb.like(cb.lower(root.get(fieldName)), "%" + search.toLowerCase() + "%");

        searchPredicates.add(searchPredicate);
      }
    }

    return searchPredicates;
  }
}
