package com.pawradise.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "First Name cannot be empty")
    String firstName;

    @NotBlank(message = "Last Name cannot be empty")
    String lastName;

//    @NotBlank(message = "Username cannot be empty")
//    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
//    private String username;

    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Please provide a valid email address")
    private String email;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password must contain at least 8 characters, including uppercase, lowercase, number, and special character"
    )
    private String password;

}