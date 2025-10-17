package com.pawradise.api.dto;

import lombok.Data;

@Data
public class OtpRequestDto {
    private String field; // "email" or "phone"
    private String value; // the new email address or phone number
}