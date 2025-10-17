package com.pawradise.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VisitRequestDto {
    @NotBlank
    private String petId;
    @NotBlank
    private String fullName;
    @NotBlank
    private String email;
    @NotBlank
    private String phone;
    @NotBlank
    private String address;
    @NotBlank
    private String reason;
}