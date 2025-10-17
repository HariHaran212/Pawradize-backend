package com.pawradise.api.dto;

import lombok.Data;

@Data
public class SystemSettingsDto {
    private boolean newUserEmail;
    private boolean newOrderEmail;
    private boolean lowStockAlerts;

    private String storeAddress;
    private String storeHours;
    private String storePhone;
    private String storeEmail;
}