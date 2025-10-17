package com.pawradise.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PasswordChangeDto {

    @NotBlank(message = "Old password cannot be empty")
    private String oldPassword;

    @NotBlank(message = "New password cannot be empty")
    @Size(min = 8, message = "New password must be at least 8 characters long")
    private String newPassword;
}