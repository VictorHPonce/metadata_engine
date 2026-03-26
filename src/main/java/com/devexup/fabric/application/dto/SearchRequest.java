package com.devexup.fabric.application.dto;

import lombok.Data;

@Data
public class SearchRequest {
    private String field;    // Ejemplo: "temp"
    private String operator; // Ejemplo: ">"
    private Object value;    // Ejemplo: 30
}