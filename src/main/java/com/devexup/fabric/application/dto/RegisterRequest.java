package com.devexup.fabric.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class RegisterRequest {
    private String companyName;
    private String email;
    private String password;
}