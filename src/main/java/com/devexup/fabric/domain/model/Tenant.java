package com.devexup.fabric.domain.model;

import java.util.UUID;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Tenant {
    private final UUID id;
    private final String name;
    private final String subdomain;
    private final boolean active;
}
