package com.pawradise.api.dto;

import lombok.Data;

@Data
public class SettingsUpdateDto {
    private boolean emailPromotions;
    private boolean emailOrderUpdates;
    private boolean emailPetUpdates;
}