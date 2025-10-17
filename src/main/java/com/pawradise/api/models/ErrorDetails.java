package com.pawradise.api.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorDetails {
    private Long timestamp;
    private String description;
    private String message;
}
