package com.pawradise.api.dto;

import lombok.Data;

@Data
public class OtpVerifyDto {
    private String field; // "email" or "phone"
    private String value;
    private String otp;
}