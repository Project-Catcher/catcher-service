package com.catcher.infrastructure.utils;

import com.catcher.core.domain.GeneralSearchFilterType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class CriteriaUtil {

    private static final String DEFAULT_SEARCH_DATE_COLUMN = "createdAt";

    public static <T> Specification<T> generateQueryBuilder(final GeneralSearchFilterType filterType, final LocalDate startDate, final LocalDate endDate, final String query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (filterType != null && filterType != filterType.getDefaultField() && query != null) {
                predicates.add(criteriaBuilder.like(root.get(filterType.getMatchedField()), "%" + query + "%")); // 검색 쿼리 적용
            }

            if (startDate != null && endDate != null) {
                predicates.add(criteriaBuilder.between(
                        root.get(DEFAULT_SEARCH_DATE_COLUMN),
                        startDate.atStartOfDay(ZoneId.systemDefault()),
                        endDate.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault())
                ));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    public static <T> Specification<T> generateQueryBuilderWithoutDate(final GeneralSearchFilterType filterType, final String query) {
        return generateQueryBuilder(filterType, null, null, query);
    }
}
