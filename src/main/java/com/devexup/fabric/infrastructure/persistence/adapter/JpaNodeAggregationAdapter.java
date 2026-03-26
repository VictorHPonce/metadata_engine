package com.devexup.fabric.infrastructure.persistence.adapter;

import com.devexup.fabric.domain.repository.NodeAggregationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class JpaNodeAggregationAdapter implements NodeAggregationRepository {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public BigDecimal aggregate(String nodeName, String fieldName, String operation) {
        String sql = String.format(
                "SELECT %s((data ->> '%s')::numeric) FROM node_records " +
                        "WHERE node_name = ? AND deleted_at IS NULL",
                operation.toUpperCase(),
                fieldName);
        return jdbcTemplate.queryForObject(sql, BigDecimal.class, nodeName);
    }

    @Override
    public BigDecimal aggregateWithFilter(String nodeName, String fieldName, String operation, String filterField,
            String filterValue) {
        // SQL que filtra por un valor dentro del JSONB antes de sumar
        String sql = String.format(
                "SELECT %s((data ->> '%s')::numeric) FROM node_records " +
                        "WHERE node_name = ? AND data ->> '%s' = ?",
                operation.toUpperCase(),
                fieldName,
                filterField,
                filterValue);

        return jdbcTemplate.queryForObject(sql, BigDecimal.class, nodeName, filterValue);
    }
}