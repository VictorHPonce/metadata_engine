package com.devexup.fabric.domain.model;

import lombok.Builder;
import lombok.Getter;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonRawValue;
import java.time.OffsetDateTime;

@Getter
@Builder(toBuilder = true)
public class DynamicRecord {
    private final UUID id;
    private final UUID tenantId;
    private final UUID definitionId;
    private final String nodeName;
    @JsonRawValue
    private final String data;
    private final OffsetDateTime createdAt;
    private final OffsetDateTime updatedAt;
    private final OffsetDateTime deletedAt;
}