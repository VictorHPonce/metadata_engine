package com.devexup.fabric.domain.repository;

import java.math.BigDecimal;

public interface NodeAggregationRepository {
    BigDecimal aggregate(String nodeName, String fieldName, String operation);
    BigDecimal aggregateWithFilter(String nodeName, String fieldName, String operation, String filterField, String filterValue);
}